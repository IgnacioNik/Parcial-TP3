package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.*
import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.Transaction

// 2. EL COMPONENTE
@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Para los Dividers
        verticalAlignment = Alignment.CenterVertically
    ) {

        // --- 1. ICONO (CORREGIDO) ---
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape), // Mantenemos el recorte por si acaso
            // <-- QUITAMOS EL .background()
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = transaction.icon),
                contentDescription = transaction.title,
                tint = Color.Unspecified // <-- USA EL COLOR ORIGINAL DEL ICONO
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // --- 2. TÍTULO Y FECHA ---
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = AppTextDark.copy(alpha = 0.9f),
                maxLines = 1
            )
            Text(
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall,
                color = AppIconBlueTint, // Color azul
                maxLines = 1
            )
        }

        // --- 3. DIVIDER ---
        TransactionDivider()

        // --- 4. CATEGORÍA (CORREGIDO) ---
        Box(
            modifier = Modifier.weight(0.3f),
            contentAlignment = Alignment.Center // <-- CENTRADO
        ) {
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextGrey,
                maxLines = 1
            )
        }

        // --- 5. DIVIDER ---
        TransactionDivider()

        // --- 6. MONTO (CORREGIDO) ---
        Box(
            modifier = Modifier.weight(0.35f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = transaction.amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                // Lógica de color actualizada (ya no busca "+")
                color = if (!transaction.amount.startsWith("-")) AppTextDark else AppIconBlueTint,
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