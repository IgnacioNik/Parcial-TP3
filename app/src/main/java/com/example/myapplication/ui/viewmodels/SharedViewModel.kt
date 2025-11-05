package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.models.BankAccount
import com.example.myapplication.data.models.Category
import com.example.myapplication.data.models.CreditCard
import com.example.myapplication.data.models.NotificationGroup
import com.example.myapplication.data.models.UserResponse
import com.example.myapplication.data.models.sampleCategories
import com.example.myapplication.data.models.sampleNotifications
import com.example.myapplication.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// Los Sealed class (como HeaderUiState) se quedan igual,
// pero puedes moverlos a un archivo "UiState.kt" si quieres
sealed class HeaderUiState {
    object Loading : HeaderUiState()
    data class Success(
        val userData: UserResponse,
        val formattedBalance: String
    ) : HeaderUiState()
    data class Error(val message: String) : HeaderUiState()
}

/**
 * Este ViewModel es "compartido" por todas las pantallas de la app.
 * Es liviano y solo maneja el estado del usuario (quién es y si es invitado).
 */
class SharedViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository: TransactionRepository
    private val currencyFormatter = DecimalFormat("$#,##0.00")

    private val _isGuest = MutableStateFlow(true)
    val isGuest = _isGuest.asStateFlow()

    private val _headerState = MutableStateFlow<HeaderUiState>(HeaderUiState.Loading)
    val headerState = _headerState.asStateFlow()

    // --- 2. AÑADE LOS NUEVOS STATEFLOWS AQUÍ ---
    private val _notificationsState = MutableStateFlow<List<NotificationGroup>>(emptyList())
    val notificationsState = _notificationsState.asStateFlow()

    private val _categoriesState = MutableStateFlow<List<Category>>(emptyList())
    val categoriesState = _categoriesState.asStateFlow()
    // --- FIN DEL CAMBIO ---

    init {
        repository = TransactionRepository(application.applicationContext)

        // --- 3. CARGA LOS DATOS ESTÁTICOS/MUESTRA AQUÍ ---
        _notificationsState.value = sampleNotifications
        _categoriesState.value = sampleCategories
        // (Borramos la lógica de 'if (isGuest)' de aquí)
    }

    /**
     * HomeScreen llama a esta función UNA VEZ para decirle
     * al ViewModel qué datos de usuario debe cargar (reales o de prueba).
     */
    fun loadUser(isGuestArg: Boolean) {
        if (_isGuest.value == isGuestArg && _headerState.value is HeaderUiState.Success) {
            return
        }

        _isGuest.value = isGuestArg

        if (isGuestArg) {
            loadSampleHeaderData()
        } else {
            refreshHeaderData()
        }
    }

    /**
     * Carga el header de prueba (sample)
     */
    private fun loadSampleHeaderData() {
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
    }

    /**
     * Llama a la API para refrescar los datos del header del usuario.
     */
    fun refreshHeaderData() {
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
                    _headerState.value = HeaderUiState.Error(
                        context.getString(R.string.home_vm_error_header)
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: context.getString(R.string.home_vm_error_unknown_network)
                _headerState.value = HeaderUiState.Error(errorMessage)
            }
        }
    }
}