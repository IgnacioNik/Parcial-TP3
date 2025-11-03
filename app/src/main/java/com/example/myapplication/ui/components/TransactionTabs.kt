package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppTextDark

@Composable
fun TransactionTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(
        stringResource(R.string.home_tab_daily),
        stringResource(R.string.home_tab_weekly),
        stringResource(R.string.home_tab_monthly)
    )

    // 1. El contenedor de fondo (la píldora verde pálido)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // Una altura fija para el componente
            .clip(CircleShape) // Redondea todo el contenedor
            .background(AppGreenLight) // El fondo verde pálido
            .padding(4.dp), // Un padding interno
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = (selectedTabIndex == index)

            // 2. Cada uno de los botones (Daily, Weekly, Monthly)
            TextButton(
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .weight(1f) // Ocupa 1/3 del espacio
                    .fillMaxHeight()
                    .clip(CircleShape) // Cada botón es una píldora
                    // 3. El fondo es verde brillante SI está seleccionado,
                    //    o transparente si NO lo está.
                    .background(if (isSelected) AppGreen else Color.Transparent),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTextDark // El texto siempre es oscuro (Fence Green)
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    // Hacemos el texto seleccionado un poco más grueso
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}