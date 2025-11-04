package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.BankAccount
import com.example.myapplication.data.models.CreditCard
import com.example.myapplication.data.models.NotificationGroup
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.data.models.UserResponse
import com.example.myapplication.data.models.sampleNotifications
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.data.models.sampleTransactionEntities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.DecimalFormat // <-- 1. IMPORTA EL FORMATEADOR

// --- 2. ¡DEFINE LOS ESTADOS AQUÍ (FUERA DE LA CLASE)! ---
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
// --- FIN DE LAS DEFINICIONES DE ESTADO ---


class HomeViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    private val isGuestString: String = savedStateHandle.get<String>("isGuest") ?: "true"
    val isGuest: Boolean = isGuestString.toBoolean()

    // Formateador de dinero (reutilizable)
    private val currencyFormatter = DecimalFormat("$#,##0.00")

    // StateFlow para el Header (Balance, Gasto)
    private val _headerState = MutableStateFlow<HeaderUiState>(HeaderUiState.Loading)
    val headerState = _headerState.asStateFlow()

    // StateFlow para la Lista PLANA de Transacciones (la mantenemos por si otra pantalla la necesita)
    private val _transactionsState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactionsState = _transactionsState.asStateFlow()

    private val _groupedTransactionsState = MutableStateFlow<Map<String, List<TransactionEntity>>>(emptyMap())
    val groupedTransactionsState = _groupedTransactionsState.asStateFlow()


    // StateFlow para el Recuadro Verde (Summary)
    private val _summaryState = MutableStateFlow<SummaryUiState>(SummaryUiState.Loading)
    val summaryState = _summaryState.asStateFlow()

    // --- AÑADE EL NUEVO STATEFLOW ---
    private val _notificationsState = MutableStateFlow<List<NotificationGroup>>(emptyList())
    val notificationsState = _notificationsState.asStateFlow()

    init {
        repository = TransactionRepository(application.applicationContext)

        // --- CAMBIO CLAVE ---
        // Cargamos las notificaciones de muestra SIEMPRE,
        // ya que no tenemos una API real para ellas.
        // Esto sirve tanto para 'guest' como para 'logged-in'.
        _notificationsState.value = sampleNotifications
        // --- FIN DEL CAMBIO ---

        if (isGuest) {
            loadSampleData()
        } else {
            observeTransactions()
            refreshAllData()
            // Ya no llamamos a loadRealNotifications()
        }
    }

    /**
     * Carga los datos de prueba (sample) en los StateFlows
     * para el "modo invitado".
     */
    private fun loadSampleData() {
        // --- Poblar el 'formattedBalance' ---
        val sampleBalance = 7783.00
        _headerState.value = HeaderUiState.Success(
            userData = UserResponse(
                userId = "guest",
                name = "Guest User",
                email = "guest@email.com",
                balance = sampleBalance, // Mantenemos el Double original
                creditCard = CreditCard(
                    cardNumber = "0000", cardholderName = "Guest", expirationDate = "12/99", cvv = "123",
                    creditLimit = 20000.00,
                    currentBalance = 6000.0,
                    availableBalance = 7000
                ),
                bankAccount = BankAccount(
                    bankName = "Guest Bank", accountType = "CVU", cvu = "000", alias = "guest.bank", currency = "ARS"
                )
            ),
            formattedBalance = currencyFormatter.format(sampleBalance) // <-- Le pasamos el String
        )

        // --- Poblar AMBOS estados de transacciones ---
        _transactionsState.value = sampleTransactionEntities
        _groupedTransactionsState.value = groupAndSortTransactionsByMonth(sampleTransactionEntities)

        // --- CORRECCIÓN AQUÍ ---
        // Debes proveer los valores para formattedIncome y formattedExpense
        val sampleRevenue = 4000.00
        val sampleFood = 100.00
        _summaryState.value = SummaryUiState.Success(
            revenueLastWeek = sampleRevenue,
            foodLastWeek = sampleFood,
            savingsProgress = 0.5f,
            // ¡Pasa los strings formateados!
            formattedIncome = currencyFormatter.format(sampleRevenue),
            formattedExpense = "-${currencyFormatter.format(sampleFood)}"
        )

        // --- CAMBIO CLAVE ---
        // La línea de _notificationsState.value se movió al bloque init { }
        // para que se cargue SIEMPRE.
    }

    /**
     * --- Nueva función helper para agrupar ---
     */
    private fun groupAndSortTransactionsByMonth(transactions: List<TransactionEntity>): Map<String, List<TransactionEntity>> {
        val monthOrder = mapOf(
            "January" to 1, "February" to 2, "March" to 3, "April" to 4, "May" to 5, "June" to 6,
            "July" to 7, "August" to 8, "September" to 9, "October" to 10, "November" to 11, "December" to 12
        )
        val grouped = transactions.groupBy { transaction ->
            monthOrder.keys.find { transaction.date.contains(it, true) } ?: "Unknown"
        }
        return grouped.toSortedMap(compareByDescending { month ->
            monthOrder[month] ?: -1
        })
    }

    // --- CAMBIO CLAVE ---
    // BORRAMOS toda la función loadRealNotifications()
    // porque ya no se usa.

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.getTransactionsFromDb()
                .catch { e ->
                    _summaryState.value = SummaryUiState.Error("Error reading database")
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

    private fun calculateSummary(transactions: List<TransactionEntity>) {
        try {
            val revenue = transactions
                .filter { it.type == "credit" }
                .sumOf { it.amount }

            val food = transactions
                .filter { it.category.contains("Pantry", true) || it.category.contains("Groceries", true) }
                .sumOf { it.amount }

            // --- CORRECCIÓN AQUÍ ---
            _summaryState.value = SummaryUiState.Success(
                revenueLastWeek = revenue,
                foodLastWeek = food,
                savingsProgress = 0.75f,
                formattedIncome = currencyFormatter.format(revenue),
                formattedExpense = "-${currencyFormatter.format(food)}"
            )
        } catch (e: Exception) {
            _summaryState.value = SummaryUiState.Error("Summary calculation failed")
        }
    }

    fun refreshAllData() {
        val userId = 1 // (ID de usuario de ejemplo)

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
                    _headerState.value = HeaderUiState.Error("Failed to load header data")
                }
            } catch (e: Exception) {
                _headerState.value = HeaderUiState.Error(e.message ?: "Unknown error")
            }
        }

        viewModelScope.launch {
            try {
                repository.refreshTransactionsFromApi()
            } catch (e: Exception) {
                _summaryState.value = SummaryUiState.Error("Failed to refresh transactions")
            }
        }
    }
}