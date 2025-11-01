package com.example.myapplication.api

import com.example.myapplication.data.LoginRequest
import com.example.myapplication.data.LoginResponse
import com.example.myapplication.data.RegisterRequest
import com.example.myapplication.data.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login") // El endpoint de la consigna
    suspend fun login(
        @Header("x-api-key") apiKey: String, //  El header de seguridad [cite: 142]
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/create") // El endpoint de la consigna
    suspend fun register(
        @Header("x-api-key") apiKey: String, // El header de seguridad [cite: 142]
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>
}