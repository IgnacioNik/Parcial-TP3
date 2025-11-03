package com.example.myapplication.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppGreen

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

data class NotificationItem(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: String,
    @DrawableRes val icon: Int,
    val iconBgColor: Color,
    val details: String? = null,
    val iconSize: Dp = 24.dp // <-- 3. AÑADIMOS EL TAMAÑO (default 24.dp)
)

data class NotificationGroup(
    val title: String,
    val notifications: List<NotificationItem>
)

val sampleNotifications = listOf(
    NotificationGroup(
        title = "Today",
        notifications = listOf(
            NotificationItem(1, "Reminder!", "Set up your automatic savings to \nmeet your savings goal...", "17:00 - April 24", R.drawable.ic_notif_bell, AppGreen, iconSize = 24.dp), // <-- Tamaño Campana
            NotificationItem(2, "New Update", "Set up your automatic savings to \nmeet your savings goal...", "17:00 - April 24", R.drawable.ic_notif_star, AppGreen, iconSize = 20.dp) // <-- Tamaño Estrella
        )
    ),
    NotificationGroup(
        title = "Yesterday",
        notifications = listOf(
            NotificationItem(3, "Transactions", "A new transaction has been registered", "17:00 - April 24", R.drawable.ic_notif_dollar, AppGreen,"Groceries | Pantry | -$100.00", iconSize = 20.dp), // <-- Tamaño Dólar
            NotificationItem(4, "Reminder!", "Set up your automatic savings to \nmeet your savings goal...", "17:00 - April 24", R.drawable.ic_notif_bell, AppGreen, iconSize = 24.dp) // <-- Tamaño Campana
        )
    ),
    NotificationGroup(
        title = "This Weekend",
        notifications = listOf(
            NotificationItem(5, "Expense Record", "We recommend that you be more \nattentive to your finances", "17:00 - April 24", R.drawable.ic_notif_arrow, AppGreen, iconSize = 20.dp), // <-- Tamaño Flecha
            NotificationItem(6, "Transactions", "A new transaction has been registered", "17:00 - April 24", R.drawable.ic_notif_dollar, AppGreen,"Food | Dinner | -$70.40", iconSize = 20.dp) // <-- Tamaño Dólar
        )
    )
)