package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HeaderUiState

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
        Spacer(modifier = Modifier.height(24.dp))

        // --- MANEJO DE ESTADO (Loading, Error, Success) ---
        when (headerState) {
            is HeaderUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = AppTextWhite
                )
            }
            is HeaderUiState.Error -> {
                Text(
                    text = headerState.message,
                    color = AppTextWhite,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is HeaderUiState.Success -> {
                val userData = headerState.userData

                // Formateamos el balance (este siempre viene)
                val totalBalance = "$${"%.2f".format(userData.balance)}"

                // Comprobamos si la tarjeta es nula (para modo invitado)
                if (userData.creditCard != null) {
                    // --- SI HAY TARJETA (Usuario existente) ---
                    val totalExpense = "-$${"%.2f".format(userData.creditCard.currentBalance)}"
                    // Cálculo de progreso seguro (evita división por cero)
                    val progress = if (userData.creditCard.creditLimit > 0) {
                        (userData.creditCard.currentBalance / userData.creditCard.creditLimit).toFloat()
                    } else {
                        0f
                    }
                    val limit = "$${userData.creditCard.creditLimit}"
                    val progressText = "${(progress * 100).toInt()}%"

                    // Fila 2: Balance Cards
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        BalanceCard(
                            title = stringResource(R.string.home_total_balance),
                            amount = totalBalance,
                            icon = R.drawable.ic_income,
                            modifier = Modifier.weight(1f)
                        )
                        BalanceCard(
                            title = stringResource(R.string.home_total_expense),
                            amount = totalExpense,
                            icon = R.drawable.ic_expense,
                            modifier = Modifier.weight(1f),
                            amountColor = AppIconBlueTint
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fila 3: Barra de Progreso (CON DATOS)
                    ProgressBarWithText(
                        progress = progress,
                        progressText = progressText,
                        limitText = limit
                    )

                } else {
                    // --- SI NO HAY TARJETA (Usuario nuevo o invitado) ---
                    // Fila 2: Balance Cards
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        BalanceCard(
                            title = stringResource(R.string.home_total_balance),
                            amount = totalBalance,
                            icon = R.drawable.ic_income,
                            modifier = Modifier.weight(1f)
                        )
                        BalanceCard(
                            title = stringResource(R.string.home_total_expense),
                            amount = "-$0.00",
                            icon = R.drawable.ic_expense,
                            modifier = Modifier.weight(1f),
                            amountColor = AppIconBlueTint
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fila 3: Barra de Progreso (VACÍA)
                    ProgressBarWithText(
                        progress = 0f,
                        progressText = "0%",
                        limitText = "$0"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fila 4: "Looks Good"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = stringResource(R.string.home_icon_desc_check),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.home_looks_good),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTextDark
                    )
                }
            }
        }
    }
}

/**
 * La barra de progreso personalizada.
 * Es 'private' porque SOLO la usa HomeHeader.
 */
@Composable
private fun ProgressBarWithText(
    progress: Float,
    progressText: String,
    limitText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp),
        contentAlignment = Alignment.CenterStart // Alinea la barra oscura a la izquierda
    ) {
        // Barra de fondo (clara)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(AppGreenLight),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = limitText,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextDark,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        // Barra de progreso (oscura)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f)) // Asegura que el progreso esté entre 0 y 1
                .clip(CircleShape)
                .background(AppTextDark),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextWhite,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}