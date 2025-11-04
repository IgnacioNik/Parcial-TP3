package com.example.myapplication.data.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppGreen
import com.google.gson.annotations.SerializedName // <-- ¡Añadí este import!

data class Transaction(
    @SerializedName("transaction_id")
    val id: String, // "004"

    @SerializedName("date")
    val date: String, // "2024-10-20"

    @SerializedName("description")
    val description: String, // "Devolución de compra..."

    @SerializedName("amount")
    val amount: Double, // 1500

    @SerializedName("currency")
    val currency: String, // "ARS"

    @SerializedName("type")
    val type: String // "credit"
) {
    // --- LÓGICA DE MAPEO ---
    // (Aquí es donde convertimos los datos de la API a lo que la UI necesita)

    // 1. ¿Cómo obtenemos el TÍTULO?
    // Por ahora, usaremos la descripción.
    fun getTitle(): String {
        // Podríamos acortar la descripción si es muy larga
        return this.description
    }

    // 2. ¿Cómo obtenemos la CATEGORÍA?
    // ¡La API no nos da esta info!
    // Usaremos "Unknown" como placeholder.
    fun getCategory(): String {
        // TODO: ¿Hay otra llamada a la API para buscar la categoría de "004"?
        // Por ahora:
        if (this.description.contains("Salary", true)) return "Monthly"
        if (this.description.contains("Groceries", true)) return "Pantry"
        return "General" // Placeholder
    }

    // 3. ¿Cómo obtenemos el ICONO?
    // ¡La API no nos da esta info!
    // Tendremos que adivinarlo basado en el texto.
    @DrawableRes
    fun getIconResource(): Int {
        // TODO: ¿De dónde sacamos el ícono?
        if (this.type.contains("credit", true)) return R.drawable.ic_salary
        if (this.description.contains("Groceries", true)) return R.drawable.ic_groceries
        if (this.description.contains("Rent", true)) return R.drawable.ic_rent
        if (this.description.contains("Transport", true)) return R.drawable.ic_transport
        return R.drawable.ic_entertainment // Icono por defecto
    }

    // 4. ¿Cómo formateamos el MONTO?
    // Usamos el 'type' para decidir el signo.
    fun getFormattedAmount(): String {
        val amountInPesos = "%.2f".format(this.amount / 100.0) // Asumiendo 2 decimales
        return if (this.type == "credit") {
            "$$amountInPesos"
        } else {
            "-$${amountInPesos}"
        }
    }

    // 5. ¿Cómo formateamos la FECHA?
    // (Por ahora la dejamos como está, pero se podría formatear)
    fun getFormattedDate(): String {
        return this.date // TODO: Formatear de "2024-10-20" a "17:00 - April 24"
    }
}

val sampleTransactionEntities = listOf(
    TransactionEntity(id = "001", title = "Salary", category = "Monthly", amount = 4000.00, date = "18:27 - April 30", icon = R.drawable.ic_salary, type = "credit"),
    TransactionEntity(id = "002", title = "Groceries", category = "Pantry", amount = 100.00, date = "17:00 - April 24", icon = R.drawable.ic_groceries, type = "debit"),
    TransactionEntity(id = "003", title = "Rent", category = "Rent", amount = 674.40, date = "8:30 - April 15", icon = R.drawable.ic_rent, type = "debit"),
    TransactionEntity(id = "004", title = "Spotify", category = "Entertainment", amount = 10.00, date = "12:00 - April 14", icon = R.drawable.ic_entertainment, type = "debit"),
    TransactionEntity(id = "005", title = "Transport", category = "Transport", amount = 5.00, date = "09:00 - April 12", icon = R.drawable.ic_transport, type = "debit")
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

data class Category(
    val id: String,
    val title: String,
    @DrawableRes val iconRes: Int // El ID del recurso drawable (ej: R.drawable.ic_food)
)

// 2. Crea la lista de muestra (hardcodeada)
// NOTA: Debes asegurarte de tener estos iconos en tu carpeta res/drawable
// (ic_food, ic_transport, ic_medicine, etc.)
val sampleCategories = listOf(
    Category(id = "1", title = "Food", iconRes = R.drawable.ic_food),
    Category(id = "2", title = "Transport", iconRes = R.drawable.ic_transport),
    Category(id = "3", title = "Medicine", iconRes = R.drawable.ic_medicine),
    Category(id = "4", title = "Groceries", iconRes = R.drawable.ic_groceries),
    Category(id = "5", title = "Rent", iconRes = R.drawable.ic_rent),
    Category(id = "6", title = "Gifts", iconRes = R.drawable.ic_gift),
    Category(id = "7", title = "Savings", iconRes = R.drawable.ic_saving),
    Category(id = "8", title = "Entertainment", iconRes = R.drawable.ic_entertainment),
    Category(id = "9", title = "More", iconRes = R.drawable.ic_more),
)