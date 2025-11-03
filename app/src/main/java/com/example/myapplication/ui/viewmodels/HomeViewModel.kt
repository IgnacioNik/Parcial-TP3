package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.BankAccount // Importa el modelo
import com.example.myapplication.data.models.CreditCard // Importa el modelo
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.data.models.UserResponse
import com.example.myapplication.data.repository.TransactionRepository
import com.example.myapplication.data.models.sampleTransactionEntities // <-- 1. ¡IMPORTA LOS DATOS DE PRUEBA!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// --- 2. ¡DEFINE LOS ESTADOS AQUÍ (FUERA DE LA CLASE)! ---
sealed class HeaderUiState {
    object Loading : HeaderUiState()
    data class Success(val userData: UserResponse) : HeaderUiState()
    data class Error(val message: String) : HeaderUiState()
}

sealed class SummaryUiState {
    object Loading : SummaryUiState()
    data class Success(
        val revenueLastWeek: Double,
        val foodLastWeek: Double,
        val savingsProgress: Float
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
    // 2. Convierte manualmente el String a un Boolean
    val isGuest: Boolean = isGuestString.toBoolean()

    // StateFlow para el Header (Balance, Gasto)
    private val _headerState = MutableStateFlow<HeaderUiState>(HeaderUiState.Loading)
    val headerState = _headerState.asStateFlow()

    // StateFlow para la Lista de Transacciones (desde Room)
    private val _transactionsState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactionsState = _transactionsState.asStateFlow()

    // StateFlow para el Recuadro Verde (Summary)
    private val _summaryState = MutableStateFlow<SummaryUiState>(SummaryUiState.Loading)
    val summaryState = _summaryState.asStateFlow()

    init {
        repository = TransactionRepository(application.applicationContext)

        if (isGuest) {
            loadSampleData()
        } else {
            observeTransactions()
            refreshAllData()
        }
    }

    /**
     * Carga los datos de prueba (sample) en los StateFlows
     * para el "modo invitado".
     */
    private fun loadSampleData() {
        // Muestra un header de prueba
        _headerState.value = HeaderUiState.Success(
            UserResponse(
                userId = "guest",
                name = "Guest User",
                email = "guest@email.com",
                balance = 7783.00,
                creditCard = CreditCard( // Crea una CreditCard falsa
                    cardNumber = "0000", cardholderName = "Guest", expirationDate = "12/99", cvv = "123",
                    creditLimit = 20000.00,
                    currentBalance = 6000.0,
                    availableBalance = 7000
                ),
                bankAccount = BankAccount( // Crea una BankAccount falsa
                    bankName = "Guest Bank", accountType = "CVU", cvu = "000", alias = "guest.bank", currency = "ARS"
                )
            )
        )

        // Muestra la lista de transacciones de prueba
        _transactionsState.value = sampleTransactionEntities

        _summaryState.value = SummaryUiState.Success(
            revenueLastWeek = 4000.00,
            foodLastWeek = 100.00,
            savingsProgress = 0.5f
        )
    }


    private fun observeTransactions() {
        viewModelScope.launch {
            repository.getTransactionsFromDb()
                .catch { e ->
                    _summaryState.value = SummaryUiState.Error("Error reading database")
                    e.printStackTrace()
                }
                .collect { transactionsList ->
                    _transactionsState.value = transactionsList
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

            _summaryState.value = SummaryUiState.Success(
                revenueLastWeek = revenue,
                foodLastWeek = food,
                savingsProgress = 0.75f
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
                    _headerState.value = HeaderUiState.Success(response.body()!!)
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