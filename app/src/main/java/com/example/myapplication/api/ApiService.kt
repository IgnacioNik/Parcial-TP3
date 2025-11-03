package com.example.myapplication.api

import com.example.myapplication.data.models.LoginRequest
import com.example.myapplication.data.models.LoginResponse
import com.example.myapplication.data.models.RegisterRequest
import com.example.myapplication.data.models.RegisterResponse
import com.example.myapplication.data.models.TransactionsResponse
import com.example.myapplication.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("transactions")
    suspend fun getTransactions(
        @Header("x-api-key") apiKey: String
        // (Podríamos necesitar un @Query("user_id") si la API lo pide)
    ): Response<TransactionsResponse> // Devuelve una lista de transacciones

    // --- 2. MÉTODO NUEVO (Ver información del cliente) ---
    @GET("users/{id}")
    suspend fun getUserData(
        @Header("x-api-key") apiKey: String,
        @Path("id") userId: Int // El ID del usuario que se logueó
    ): Response<UserResponse> // Devuelve la info del balance

}