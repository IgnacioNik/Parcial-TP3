package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.viewmodels.HeaderUiState

/**
 * Este es el Header específico para la HomeScreen.
 * Muestra el saludo y la sección de balance.
 */
@Composable
fun HomeHeader(
    onNavigateToNotification: () -> Unit,
    headerState: HeaderUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        // --- FILA 1: "Welcome" y Campana ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.home_welcome),
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTextDark
                )
                Text(
                    text = stringResource(R.string.home_good_morning),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextDark
                )
            }
            IconButton(onClick = onNavigateToNotification) {
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = stringResource(R.string.home_icon_desc_notification),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- FILA 2, 3, 4: Llama al componente reutilizable ---
        BalanceHeaderSection(headerState = headerState)
    }
}