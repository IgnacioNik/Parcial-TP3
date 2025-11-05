package com.example.myapplication.ui.viewmodels

import android.app.Application // <-- 1. IMPORTA APPLICATION
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel // <-- 2. CAMBIA ESTO
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R // <-- 3. IMPORTA R
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.data.models.RegisterRequest // (Asumo que esta es tu ruta)
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

// --- 4. CAMBIO EN LA FIRMA DE LA CLASE ---
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    // Guarda el contexto para usarlo al buscar strings
    private val context = application.applicationContext

    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var mobile by mutableStateOf("")
    var dob by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)


    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val registerState = _registerState.asStateFlow()

    fun onPasswordVisibilityToggle() {
        passwordVisible = !passwordVisible
    }

    fun onConfirmPasswordVisibilityToggle() {
        confirmPasswordVisible = !confirmPasswordVisible
    }


    fun register() {
        if (_registerState.value == RegisterUiState.Loading) return

        // --- 5. CAMBIOS DE STRINGS ---
        if (fullName.isBlank() || email.isBlank() || mobile.isBlank() || dob.isBlank() || password.isBlank()) {
            _registerState.value = RegisterUiState.Error(
                context.getString(R.string.register_error_all_fields)
            )
            return
        }
        if (password != confirmPassword) {
            _registerState.value = RegisterUiState.Error(
                context.getString(R.string.register_error_passwords_no_match)
            )
            return
        }

        _registerState.value = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val request = RegisterRequest(fullName, email, mobile, dob, password)
                val apiKey = "123456789"

                val response = RetrofitClient.instance.register(apiKey, request)

                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = RegisterUiState.Success
                } else {
                    val errorMessage = response.message() ?: context.getString(R.string.register_error_unknown)
                    _registerState.value = RegisterUiState.Error(
                        context.getString(R.string.register_error_api_failed, errorMessage)
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: context.getString(R.string.register_error_unknown)
                _registerState.value = RegisterUiState.Error(
                    context.getString(R.string.register_error_network, errorMessage)
                )
            }
        }
    }

    fun resetErrorState() {
        _registerState.value = RegisterUiState.Idle
    }
}