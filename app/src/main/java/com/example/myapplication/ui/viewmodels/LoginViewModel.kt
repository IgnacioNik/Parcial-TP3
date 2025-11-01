package com.example.myapplication.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.data.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {


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


        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                // La API Key que te pide la consigna [cite: 142]
                val apiKey = "123456789"

                val response = RetrofitClient.instance.login(apiKey, request)

                if (response.isSuccessful && response.body() != null) {

                    _loginState.value = LoginUiState.Success

                } else {

                    _loginState.value = LoginUiState.Error("Invalid credentials")
                }
            } catch (e: Exception) {

                _loginState.value = LoginUiState.Error("Network error: ${e.message}")
            }
        }
    }
}