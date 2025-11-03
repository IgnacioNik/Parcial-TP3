package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes // <-- NUEVO IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // <-- NUEVO IMPORT
import androidx.compose.ui.res.painterResource // <-- NUEVO IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite

@Composable
fun BalanceCard(
    title: String,
    amount: String,
    @DrawableRes icon: Int, // <-- CAMBIO 1: De ImageVector a Int
    modifier: Modifier = Modifier,
    amountColor: Color = AppBackground // <-- CAMBIO 2: Nuevo parámetro de color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppGreen.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = icon), // <-- CAMBIO 3: Usar painter
                    contentDescription = title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextDark.copy(alpha = 0.8f) // <-- CAMBIO 4 (Corrección)
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.labelLarge, // <-- 1. VOLVÉ A ESTE ESTILO
                color = amountColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1 // <-- 2. MANTENÉ ESTA LÍNEA
            )
        }
    }
}