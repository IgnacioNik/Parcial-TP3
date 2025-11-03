package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite

/**
 * El Header (barra superior) reutilizable para las
 * pantallas internas (ej. Account Balance, Profile, etc.)
 */
@Composable
fun AppHeader(
    title: String,
    onBackClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding() // Padding para la barra de estado del celular
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Icono de Volver (Izquierda) ---
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.notification_desc_arrow),
                tint = AppTextWhite,
                modifier = Modifier.size(28.dp)
            )
        }

        // --- Título (Centro) ---
        Text(
            text = title, // Usa el título que le pasamos
            style = MaterialTheme.typography.bodyLarge,
            color = AppTextDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // --- Icono de Campana (Derecha) ---
        IconButton(onClick = onNotificationClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = stringResource(R.string.home_icon_desc_notification),
                tint = Color.Unspecified
            )
        }
    }
}