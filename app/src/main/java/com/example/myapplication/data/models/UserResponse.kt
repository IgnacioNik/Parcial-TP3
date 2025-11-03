package com.example.myapplication.data.models

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

// Esta clase recibirá los datos del header (Balance, Gasto, etc.)
data class UserResponse(
    @SerializedName("user_id")
    val userId: String,

    @JsonAdapter(NameAdapter::class)
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("balance")
    val balance: Double, // Este es el "Total Balance"

    @SerializedName("credit_card")
    val creditCard: CreditCard?,

    // --- ¡AQUÍ ESTÁ LA PARTE QUE FALTABA! ---
    @SerializedName("bank_account")
    val bankAccount: BankAccount?
)
