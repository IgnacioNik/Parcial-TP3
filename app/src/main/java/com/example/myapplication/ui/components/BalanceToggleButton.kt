package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite

@Composable
fun BalanceToggleButton(
    text: String,
    amount: String,
    @DrawableRes iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    unselectedContentColor: Color,
    amountColor: Color
) {
    val containerColor = if (isSelected) AppIconBlueTint else AppTextWhite
    val contentColor = if (isSelected) AppTextWhite else unselectedContentColor
    val amountColor = if (isSelected) AppTextWhite else amountColor
    val textColor = if (isSelected) AppTextWhite else AppTextDark

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp), // Las esquinas están bien
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // --- 1. REDUCE EL PADDING VERTICAL ---
                // Antes: .padding(vertical = 16.dp)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Icono (Arriba)
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = contentColor,
                // --- 2. REDUCE EL TAMAÑO DEL ICONO ---
                // Antes: .size(32.dp)
                modifier = Modifier.size(24.dp)
            )

            // --- 3. REDUCE EL ESPACIO ---
            // Antes: Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(4.dp))

            // 2. Texto (Medio)
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )

            // 3. Monto (Abajo)
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}