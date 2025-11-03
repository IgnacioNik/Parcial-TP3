package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark

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