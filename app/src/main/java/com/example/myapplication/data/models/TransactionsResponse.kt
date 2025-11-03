package com.example.myapplication.data.models

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(
    @SerializedName("credit_card_transactions")
    val creditCardTransactions: List<Transaction>,

    @SerializedName("bank_account_transactions")
    val bankAccountTransactions: List<Transaction>
)