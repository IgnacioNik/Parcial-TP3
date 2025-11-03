package com.example.myapplication.data.models

import com.google.gson.annotations.SerializedName

data class CreditCard(
    @SerializedName("card_number")
    val cardNumber: String,

    @SerializedName("cardholder_name")
    val cardholderName: String,

    @SerializedName("expiration_date")
    val expirationDate: String,

    @SerializedName("cvv")
    val cvv: String,

    @SerializedName("credit_limit")
    val creditLimit: Double,

    @SerializedName("current_balance")
    val currentBalance: Double, // "Total Expense"

    @SerializedName("available_balance")
    val availableBalance: Int
)