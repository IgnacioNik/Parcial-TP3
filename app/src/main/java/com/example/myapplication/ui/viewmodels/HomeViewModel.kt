package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.models.BankAccount
import com.example.myapplication.data.models.Category
import com.example.myapplication.data.models.CreditCard
import com.example.myapplication.data.models.NotificationGroup
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.data.models.UserResponse
import com.example.myapplication.data.models.sampleCategories
import com.example.myapplication.data.models.sampleNotifications
import com.example.myapplication.data.models.sampleTransactionEntities
import com.example.myapplication.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// --- DEFINICIONES DE ESTADO  ---
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

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository: TransactionRepository
    private val currencyFormatter = DecimalFormat("$#,##0.00")

    private val _isGuest = MutableStateFlow(true)
    val isGuest = _isGuest.asStateFlow()


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

        observeTransactions()
    }

    /**
     * HomeScreen llama a esta función UNA VEZ para decirle
     * al ViewModel qué datos debe cargar (reales o de prueba).
     */
    fun loadDataForUser(isGuestArg: Boolean) {
        if (_isGuest.value == isGuestArg && _headerState.value is HeaderUiState.Success) {
            return
        }

        _isGuest.value = isGuestArg

        if (isGuestArg) {
            loadSampleData()
        } else {
            refreshAllData()
        }
    }

    /**
     * Carga los datos de prueba (sample) en los StateFlows
     */
    private fun loadSampleData() {
        val sampleBalance = 7783.00
        _headerState.value = HeaderUiState.Success(
            userData = UserResponse(
                userId = "guest",
                name = context.getString(R.string.sample_guest_user),
                email = "guest@email.com",
                balance = sampleBalance,
                creditCard = CreditCard(
                    cardNumber = "0000",
                    cardholderName = context.getString(R.string.sample_guest),
                    expirationDate = "12/99", cvv = "123",
                    creditLimit = 20000.00, currentBalance = 6000.0, availableBalance = 7000
                ),
                bankAccount = BankAccount(
                    bankName = context.getString(R.string.sample_guest_bank),
                    accountType = "CVU", cvu = "000",
                    alias = context.getString(R.string.sample_guest_bank_alias),
                    currency = "ARS"
                )
            ),
            formattedBalance = currencyFormatter.format(sampleBalance)
        )

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

    private fun groupAndSortTransactionsByMonth(transactions: List<TransactionEntity>): Map<String, List<TransactionEntity>> {
        val monthOrder = mapOf(
            "January" to 1, "February" to 2, "March" to 3, "April" to 4, "May" to 5, "June" to 6,
            "July" to 7, "August" to 8, "September" to 9, "October" to 10, "November" to 11, "December" to 12
        )

        val grouped = transactions.groupBy { transaction ->
            monthOrder.keys.find { transaction.date.contains(it, true) }
                ?: context.getString(R.string.label_unknown_month)
        }

        return grouped.toSortedMap(compareByDescending { month ->
            monthOrder[month] ?: -1
        })
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.getTransactionsFromDb()
                .catch { e ->
                    _summaryState.value = SummaryUiState.Error(
                        context.getString(R.string.home_vm_error_db)
                    )
                    e.printStackTrace()
                }
                .collect { transactionsList ->

                    // Solo actualiza la lista desde la BD si NO eres invitado.
                    if (!_isGuest.value) {
                        _transactionsState.value = transactionsList
                        _groupedTransactionsState.value = groupAndSortTransactionsByMonth(transactionsList)
                        calculateSummary(transactionsList)
                    }
                    // Si ERES invitado, este bloque se ignora,
                    // y los datos de 'loadSampleData' se quedan.
                }
        }
    }

    private fun calculateSummary(transactions: List<TransactionEntity>) {
        try {
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
            _summaryState.value = SummaryUiState.Error(
                context.getString(R.string.home_vm_error_summary)
            )
        }
    }

    fun refreshAllData() {
        val userId = 1

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
                    _headerState.value = HeaderUiState.Error(
                        context.getString(R.string.home_vm_error_header)
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: context.getString(R.string.home_vm_error_unknown_network)
                _headerState.value = HeaderUiState.Error(errorMessage)
            }
        }

        viewModelScope.launch {
            try {
                repository.refreshTransactionsFromApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}