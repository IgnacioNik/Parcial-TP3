package com.example.myapplication.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel // <-- 1. CAMBIO AQUÍ
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R // <-- 2. IMPORTA R
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.data.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object GuestLogin : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// --- 3. CAMBIO EN LA FIRMA DE LA CLASE ---
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Guarda el contexto para usarlo al buscar strings
    private val context = application.applicationContext

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun onPasswordVisibilityToggle() {
        passwordVisible = !passwordVisible
    }

    fun login() {
        if (_loginState.value == LoginUiState.Loading) return

        if (email.isBlank() && password.isBlank()) {
            _loginState.value = LoginUiState.GuestLogin
            return
        }

        _loginState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val apiKey = "123456789"

                val response = RetrofitClient.instance.login(apiKey, request)

                if (response.isSuccessful && response.body() != null) {
                    _loginState.value = LoginUiState.Success
                } else {
                    // --- 4. CAMBIO DE STRING ---
                    _loginState.value = LoginUiState.Error(
                        context.getString(R.string.login_error_invalid_credentials)
                    )
                }
            } catch (e: Exception) {
                // --- 5. CAMBIO DE STRING (MÁS SEGURO) ---
                val errorMessage = e.message ?: context.getString(R.string.login_error_unknown)
                _loginState.value = LoginUiState.Error(
                    context.getString(R.string.login_error_network, errorMessage)
                )
            }
        }
    }

    fun resetErrorState() {
        _loginState.value = LoginUiState.Idle
    }
}