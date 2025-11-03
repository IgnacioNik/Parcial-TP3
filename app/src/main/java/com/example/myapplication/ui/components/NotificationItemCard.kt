package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.myapplication.data.NotificationItem
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark

@Composable
fun NotificationItemCard(item: NotificationItem) {
    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp), // Un padding vertical estándar
            verticalAlignment = Alignment.Top
        ) {
            // 1. El Ícono (sin cambios)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(item.iconSize)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Columna de Texto (REESTRUCTURADA)
            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.spacedBy(4.dp) // Espacio entre líneas
            ) {

                // --- TÍTULO ---
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppTextDark
                )

                // --- DESCRIPCIÓN (Ahora tiene espacio) ---
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextDark
                )

                // --- DETALLES (si existen) ---
                item.details?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppIconBlueTint,
                        fontWeight = FontWeight.Medium
                    )
                }

                // --- TIMESTAMP (Al final y a la derecha) ---
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppIconBlueTint, // Color azul
                    modifier = Modifier
                        .fillMaxWidth() // Ocupa todo el ancho
                        .padding(top = 4.dp), // Espacio extra
                    textAlign = TextAlign.End // <-- ¡LO ALINEA A LA DERECHA!
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = AppGreen)
    }
}