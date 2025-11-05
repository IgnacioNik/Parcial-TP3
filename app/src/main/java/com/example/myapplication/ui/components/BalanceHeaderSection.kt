package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite
import com.example.myapplication.ui.viewmodels.HeaderUiState

/**
 * Este componente muestra el Balance, Gasto y la Barra de Progreso.
 * Es reutilizable en HomeScreen y AccountBalanceScreen.
 */
@Composable
fun BalanceHeaderSection(headerState: HeaderUiState) {
    // --- MANEJO DE ESTADO (Loading, Error, Success) ---
    when (headerState) {
        is HeaderUiState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = AppTextWhite // Asegúrate que este color sea visible en el fondo
            )
        }
        is HeaderUiState.Error -> {
            Text(
                text = headerState.message,
                color = AppTextWhite, // Asegúrate que este color sea visible en el fondo
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        is HeaderUiState.Success -> {
            val userData = headerState.userData

            // Formateamos el balance (este siempre viene)
            val totalBalance = "$${"%.2f".format(userData.balance)}"

            // Comprobamos si la tarjeta es nula
            if (userData.creditCard != null) {
                // --- SI HAY TARJETA (Usuario existente) ---
                val totalExpense = "-$${"%.2f".format(userData.creditCard.currentBalance)}"
                val progress = (userData.creditCard.currentBalance / userData.creditCard.creditLimit).toFloat()
                val limit = "$${userData.creditCard.creditLimit}"
                val progressText = "${(progress * 100).toInt()}%"

                // --- CAMBIO AQUÍ ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // 1. Espacio reducido
                    verticalAlignment = Alignment.CenterVertically      // 2. Alineación vertical
                ) {
                    BalanceCard(
                        title = stringResource(R.string.home_total_balance),
                        amount = totalBalance,
                        icon = R.drawable.ic_income,
                        modifier = Modifier.weight(1f)
                    )

                    // 3. ¡LA LÍNEA DIVISORIA!
                    Divider(
                        color = AppTextWhite.copy(alpha = 0.5f), // Color blanco semi-transparente
                        modifier = Modifier
                            .height(40.dp) // Altura de la línea
                            .width(1.dp)
                    )

                    BalanceCard(
                        title = stringResource(R.string.home_total_expense),
                        amount = totalExpense,
                        icon = R.drawable.ic_expense,
                        modifier = Modifier.weight(1f),
                        amountColor = AppIconBlueTint
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                // Fila 3: Barra de Progreso
                ProgressBarWithText(
                    progress = progress,
                    progressText = progressText,
                    limitText = limit
                )

            } else {
                // --- SI NO HAY TARJETA (Usuario nuevo) ---

                // --- CAMBIO AQUÍ (MISMO CAMBIO DE ARRIBA) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // 1. Espacio reducido
                    verticalAlignment = Alignment.CenterVertically      // 2. Alineación vertical
                ) {
                    BalanceCard(
                        title = stringResource(R.string.home_total_balance),
                        amount = totalBalance, // $0.00
                        icon = R.drawable.ic_income,
                        modifier = Modifier.weight(1f)
                    )

                    // 3. ¡LA LÍNEA DIVISORIA!
                    Divider(
                        color = AppTextWhite.copy(alpha = 0.5f), // Color blanco semi-transparente
                        modifier = Modifier
                            .height(40.dp) // Altura de la línea
                            .width(1.dp)
                    )

                    BalanceCard(
                        title = stringResource(R.string.home_total_expense),
                        amount = "-$0.00", // Valor por defecto
                        icon = R.drawable.ic_expense,
                        modifier = Modifier.weight(1f),
                        amountColor = AppIconBlueTint
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                // Fila 3: Barra de Progreso
                ProgressBarWithText(
                    progress = 0f, // 0%
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
                    // Este color depende del fondo. Si el fondo es verde,
                    // este color debería ser AppTextWhite. Si el fondo es blanco,
                    // debería ser AppTextDark o AppGreen.
                    color = AppTextDark
                )
            }
        }
    }
}


/**
 * La barra de progreso personalizada.
 * Es 'private' porque SOLO la usa BalanceHeaderSection.
 */
@Composable
private fun ProgressBarWithText(
    progress: Float,
    progressText: String,
    limitText: String
) {
    // Este diseño de barra de progreso asume un fondo claro (blanco).
    // Si tu fondo es verde, necesitarás cambiar estos colores.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Fondo de la barra
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(AppTextWhite), // Color de fondo (ej: verde claro)
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = limitText,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextDark, // Color de texto para el límite
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        // Barra de progreso (relleno)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(CircleShape)
                .background(AppTextDark), // Color de relleno (ej: oscuro)
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextWhite, // Color de texto para el progreso
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}