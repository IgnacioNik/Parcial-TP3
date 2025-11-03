package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment // <-- Importa esto
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*

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