package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
// --- 1. IMPORTA 'R' ---
import com.example.myapplication.R
import com.example.myapplication.data.models.*
import com.example.myapplication.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// --- DEFINICIONES DE ESTADO (Se quedan igual) ---
sealed class HeaderUiState {
    object Loading : HeaderUiState()
    data class Success(
        val userData: UserResponse,
        val formattedBalance: String
    ) : HeaderUiState()
    data class Error(val message: String) : HeaderUiState()
}

sealed class SummaryUiState {
    object Loading : SummaryUiState()
    data class Success(
        val revenueLastWeek: Double,
        val foodLastWeek: Double,
        val savingsProgress: Float,
        val formattedIncome: String,
        val formattedExpense: String
    ) : SummaryUiState()
    data class Error(val message: String) : SummaryUiState()
}
// --- FIN DE DEFINICIONES ---


class HomeViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    // --- 2. AÑADE EL CONTEXTO ---
    private val context = application.applicationContext

    private val repository: TransactionRepository
    private val isGuestString: String = savedStateHandle.get<String>("isGuest") ?: "true"
    val isGuest: Boolean = isGuestString.toBoolean()

    private val currencyFormatter = DecimalFormat("$#,##0.00")

    // ... (Todos tus StateFlows se quedan igual)
    private val _headerState = MutableStateFlow<HeaderUiState>(HeaderUiState.Loading)
    val headerState = _headerState.asStateFlow()
    private val _transactionsState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactionsState = _transactionsState.asStateFlow()
    private val _groupedTransactionsState = MutableStateFlow<Map<String, List<TransactionEntity>>>(emptyMap())
    val groupedTransactionsState = _groupedTransactionsState.asStateFlow()
    private val _summaryState = MutableStateFlow<SummaryUiState>(SummaryUiState.Loading)
    val summaryState = _summaryState.asStateFlow()
    private val _notificationsState = MutableStateFlow<List<NotificationGroup>>(emptyList())
    val notificationsState = _notificationsState.asStateFlow()
    private val _categoriesState = MutableStateFlow<List<Category>>(emptyList())
    val categoriesState = _categoriesState.asStateFlow()


    init {
        repository = TransactionRepository(application.applicationContext)
        _notificationsState.value = sampleNotifications
        _categoriesState.value = sampleCategories

        if (isGuest) {
            loadSampleData()
        } else {
            observeTransactions()
            refreshAllData()
        }
    }

    /**
     * Carga los datos de prueba (sample) en los StateFlows
     */
    private fun loadSampleData() {
        // --- 3. USA LOS STRINGS DEL CONTEXTO ---
        val sampleBalance = 7783.00
        _headerState.value = HeaderUiState.Success(
            userData = UserResponse(
                userId = "guest",
                name = context.getString(R.string.sample_guest_user), // <-- CAMBIO
                email = "guest@email.com",
                balance = sampleBalance,
                creditCard = CreditCard(
                    cardNumber = "0000",
                    cardholderName = context.getString(R.string.sample_guest), // <-- CAMBIO
                    expirationDate = "12/99", cvv = "123",
                    creditLimit = 20000.00, currentBalance = 6000.0, availableBalance = 7000
                ),
                bankAccount = BankAccount(
                    bankName = context.getString(R.string.sample_guest_bank), // <-- CAMBIO
                    accountType = "CVU", cvu = "000",
                    alias = context.getString(R.string.sample_guest_bank_alias), // <-- CAMBIO
                    currency = "ARS"
                )
            ),
            formattedBalance = currencyFormatter.format(sampleBalance)
        )

        // ... (Cargar Transacciones y Summary se queda igual, ya usaban variables)
        _transactionsState.value = sampleTransactionEntities
        _groupedTransactionsState.value = groupAndSortTransactionsByMonth(sampleTransactionEntities)
        val sampleRevenue = 4000.00
        val sampleFood = 100.00
        _summaryState.value = SummaryUiState.Success(
            revenueLastWeek = sampleRevenue,
            foodLastWeek = sampleFood,
            savingsProgress = 0.5f,
            formattedIncome = currencyFormatter.format(sampleRevenue),
            formattedExpense = "-${currencyFormatter.format(sampleFood)}"
        )
    }

    /**
     * Agrupa y ordena las transacciones por mes.
     */
    private fun groupAndSortTransactionsByMonth(transactions: List<TransactionEntity>): Map<String, List<TransactionEntity>> {
        val monthOrder = mapOf(
            "January" to 1, "February" to 2, "March" to 3, "April" to 4, "May" to 5, "June" to 6,
            "July" to 7, "August" to 8, "September" to 9, "October" to 10, "November" to 11, "December" to 12
        )

        val grouped = transactions.groupBy { transaction ->
            monthOrder.keys.find { transaction.date.contains(it, true) }
                ?: context.getString(R.string.label_unknown_month) // <-- CAMBIO
        }

        return grouped.toSortedMap(compareByDescending { month ->
            monthOrder[month] ?: -1
        })
    }


    /**
     * Observa la base de datos de Room.
     */
    private fun observeTransactions() {
        viewModelScope.launch {
            repository.getTransactionsFromDb()
                .catch { e ->
                    // --- 4. USA EL STRING DE ERROR ---
                    _summaryState.value = SummaryUiState.Error(
                        context.getString(R.string.home_vm_error_db)
                    )
                    e.printStackTrace()
                }
                .collect { transactionsList ->
                    _transactionsState.value = transactionsList
                    _groupedTransactionsState.value = groupAndSortTransactionsByMonth(transactionsList)

                    if (!isGuest) {
                        calculateSummary(transactionsList)
                    }
                }
        }
    }

    /**
     * Calcula los valores de Summary.
     */
    private fun calculateSummary(transactions: List<TransactionEntity>) {
        try {
            // ... (lógica de 'revenue' y 'food' se queda igual)
            val revenue = transactions
                .filter { it.type == "credit" }
                .sumOf { it.amount }

            val food = transactions
                .filter { it.category.contains("Pantry", true) || it.category.contains("Groceries", true) }
                .sumOf { it.amount }

            _summaryState.value = SummaryUiState.Success(
                revenueLastWeek = revenue,
                foodLastWeek = food,
                savingsProgress = 0.75f,
                formattedIncome = currencyFormatter.format(revenue),
                formattedExpense = "-${currencyFormatter.format(food)}"
            )
        } catch (e: Exception) {
            // --- 5. USA EL STRING DE ERROR ---
            _summaryState.value = SummaryUiState.Error(
                context.getString(R.string.home_vm_error_summary)
            )
        }
    }

    /**
     * Refresca los datos desde la API.
     */
    fun refreshAllData() {
        val userId = 1

        // 1. Refrescar Header
        viewModelScope.launch {
            _headerState.value = HeaderUiState.Loading
            try {
                val response = repository.getUserData(userId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    _headerState.value = HeaderUiState.Success(
                        userData = user,
                        formattedBalance = currencyFormatter.format(user.balance)
                    )
                } else {
                    // --- 6. USA EL STRING DE ERROR ---
                    _headerState.value = HeaderUiState.Error(
                        context.getString(R.string.home_vm_error_header)
                    )
                }
            } catch (e: Exception) {
                // --- 7. USA EL STRING DE ERROR ---
                val errorMessage = e.message ?: context.getString(R.string.home_vm_error_unknown_network)
                _headerState.value = HeaderUiState.Error(errorMessage)
            }
        }

        // 2. Refrescar Transacciones
        viewModelScope.launch {
            try {
                repository.refreshTransactionsFromApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}