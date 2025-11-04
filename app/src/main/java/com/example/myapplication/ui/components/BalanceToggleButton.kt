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
    unselectedContentColor: Color
) {
    val containerColor = if (isSelected) AppIconBlueTint else AppTextWhite
    val contentColor = if (isSelected) AppTextDark else unselectedContentColor

    Card(
        modifier = modifier, // El .weight(1f) de la pantalla le da el ancho
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        // VVV ¡CAMBIO DE "Row" A "Column"! VVV
        Column(
            modifier = Modifier
                .fillMaxWidth() // Ocupa el ancho que le da el .weight(1f)
                .padding(vertical = 16.dp), // Padding arriba y abajo
            horizontalAlignment = Alignment.CenterHorizontally, // Centra todo
            verticalArrangement = Arrangement.Center
        ) {
            // 1. Icono (Arriba)
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio

            // 2. Texto (Medio)
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextDark
            )

            // 3. Monto (Abajo)
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
        // ^^^ FIN DEL CAMBIO ^^^
    }
}