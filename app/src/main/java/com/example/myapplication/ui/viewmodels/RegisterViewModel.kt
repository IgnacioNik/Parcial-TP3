package com.example.myapplication.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel : ViewModel() {


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


        if (fullName.isBlank() || email.isBlank() || mobile.isBlank() || dob.isBlank() || password.isBlank()) {
            _registerState.value = RegisterUiState.Error("Please fill all fields")
            return
        }
        if (password != confirmPassword) {
            _registerState.value = RegisterUiState.Error("Passwords do not match")
            return
        }



        _registerState.value = RegisterUiState.Loading
        viewModelScope.launch {
            try {
                val request = RegisterRequest(fullName, email, mobile, dob, password)
                val apiKey = "123456789" // La API key de la consigna [cite: 142]

                val response = RetrofitClient.instance.register(apiKey, request)

                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = RegisterUiState.Success
                } else {
                    _registerState.value = RegisterUiState.Error("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterUiState.Error("Network error: ${e.message}")
            }
        }
    }


    fun resetErrorState() {
        _registerState.value = RegisterUiState.Idle
    }
}