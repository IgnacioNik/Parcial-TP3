package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.data.models.sampleTransactionEntities
import com.example.myapplication.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// (Puedes mover SummaryUiState a un archivo UiState.kt junto con HeaderUiState)
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

/**
 * Este ViewModel es "local" y "pesado".
 * Solo lo usan las pantallas que necesitan mostrar transacciones (Home, Transactions).
 */
class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository: TransactionRepository
    private val currencyFormatter = DecimalFormat("$#,##0.00")

    // Este ViewModel NO sabe si es invitado o no,
    // la pantalla se lo tiene que decir.
    private var isGuest = true

    // --- StateFlows de Transacciones y Resumen ---
    private val _transactionsState = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactionsState = _transactionsState.asStateFlow()

    private val _groupedTransactionsState = MutableStateFlow<Map<String, List<TransactionEntity>>>(emptyMap())
    val groupedTransactionsState = _groupedTransactionsState.asStateFlow()

    private val _summaryState = MutableStateFlow<SummaryUiState>(SummaryUiState.Loading)
    val summaryState = _summaryState.asStateFlow()

    // NOTA: Los 'notifications' y 'categories' ya no viven aquí

    init {
        repository = TransactionRepository(context)
        observeTransactions()
    }

    /**
     * La Pantalla (ej. HomeScreen) llama a esto UNA VEZ para
     * decirle si debe cargar datos de prueba o reales.
     */
    fun loadData(isGuestArg: Boolean) {
        isGuest = isGuestArg // Guarda el estado

        if (isGuestArg) {
            loadSampleData()
        } else {
            refreshTransactions()
        }
    }

    private fun loadSampleData() {
        // Carga transacciones de prueba
        _transactionsState.value = sampleTransactionEntities
        _groupedTransactionsState.value = groupAndSortTransactionsByMonth(sampleTransactionEntities)

        // Carga resumen de prueba
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
                    // ¡IMPORTANTE! Solo actualiza desde la BD si NO eres invitado
                    if (!isGuest) {
                        _transactionsState.value = transactionsList
                        _groupedTransactionsState.value = groupAndSortTransactionsByMonth(transactionsList)
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

    fun refreshTransactions() {
        viewModelScope.launch {
            try {
                repository.refreshTransactionsFromApi()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
}