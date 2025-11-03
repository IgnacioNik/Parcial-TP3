package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.Transaction
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextGrey

// 2. EL COMPONENTE
@Composable
fun TransactionItem(transaction: TransactionEntity) {
    // --- Lógica de formateo ---
    val amountString = "%.2f".format(transaction.amount)
    val formattedAmount = if (transaction.type == "credit") {
        "$$amountString"
    } else {
        "-$${amountString}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Para los Dividers
        verticalAlignment = Alignment.CenterVertically
    ) {

        // --- 1. ICONO ---
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = transaction.icon), // <-- USA HELPER
                contentDescription = transaction.title, // <-- USA HELPER
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // --- 2. TÍTULO Y FECHA ---
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = transaction.title, // <-- USA HELPER
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = AppTextDark.copy(alpha = 0.9f), // (AppTextDark que usaste)
                maxLines = 1
            )
            Text(
                text = transaction.date, // <-- USA HELPER
                style = MaterialTheme.typography.bodySmall,
                color = AppIconBlueTint, // (Tu color azul)
                maxLines = 1
            )
        }

        // --- 3. DIVIDER ---
        TransactionDivider()

        // --- 4. CATEGORÍA ---
        Box(
            modifier = Modifier.weight(0.3f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transaction.category, // <-- USA HELPER
                style = MaterialTheme.typography.bodySmall,
                color = AppTextGrey,
                maxLines = 1
            )
        }

        // --- 5. DIVIDER ---
        TransactionDivider()

        // --- 6. MONTO ---
        Box(
            modifier = Modifier.weight(0.35f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = formattedAmount, // <-- USA HELPER
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                // Lógica de color basada en el "type" de la API
                color = if (transaction.type == "credit") AppTextDark else AppIconBlueTint,
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }
    }
}

/**
 * Función "ayudante" para los divisores verticales
 */
@Composable
private fun TransactionDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp),
        color = AppGreen.copy(alpha = 0.3f)
    )
}