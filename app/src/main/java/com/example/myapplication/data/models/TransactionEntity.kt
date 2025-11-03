package com.example.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Esta es la "tabla" de nuestra base de datos.
 * Representa los datos que la UI (TransactionItem) necesita.
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String, // "001"
    val title: String, // "Salary"
    val category: String, // "Monthly"
    val amount: Double, // "$4,000.00"
    val date: String, // "18:27 - April 30"
    val icon: Int, // R.drawable.ic_salary
    val type: String // "credit" o "debit"
)