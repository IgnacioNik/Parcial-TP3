package com.example.myapplication.data

import com.example.myapplication.R

// 1. EL MODELO DE DATOS
data class Transaction(
    val id: Int,
    val title: String,
    val category: String,
    val amount: String,
    val date: String,
    val icon: Int // R.drawable.ic_salary
)

// 2. LOS DATOS DE PRUEBA
val sampleTransactions = listOf(
    Transaction(1, "Salary", "Monthly", "$4,000.00", "18:27 - April 30", R.drawable.ic_salary),
    Transaction(2, "Groceries", "Pantry", "-$100.00", "17:00 - April 24", R.drawable.ic_groceries),
    Transaction(3, "Rent", "Rant", "-$674.40", "8:30 - April 15", R.drawable.ic_rent),
    Transaction(4, "Spotify", "Entertainment", "-$10.00", "12:00 - April 14", R.drawable.ic_entertainment),
    Transaction(5, "Transport", "Transport", "-$50.00", "09:00 - April 12", R.drawable.ic_transport),
    Transaction(6, "Freelance", "Income", "$500.00", "15:30 - April 10", R.drawable.ic_salary)
)