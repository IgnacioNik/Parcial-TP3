package com.example.myapplication.data.models

import com.google.gson.annotations.SerializedName

data class BankAccount(
    @SerializedName("bank_name")
    val bankName: String,

    @SerializedName("account_type")
    val accountType: String,

    @SerializedName("cvu")
    val cvu: String,

    @SerializedName("alias")
    val alias: String,

    @SerializedName("currency")
    val currency: String
)