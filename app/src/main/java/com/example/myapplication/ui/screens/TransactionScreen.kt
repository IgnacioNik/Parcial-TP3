package com.example.myapplication.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.* // Importa todos tus componentes
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HeaderUiState
import com.example.myapplication.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel() // Reusa el ViewModel
) {
    // 1. ESTADOS
    val headerState by viewModel.headerState.collectAsState()
    // ¡NUEVO! Consume el estado agrupado
    val groupedTransactions by viewModel.groupedTransactionsState.collectAsState()
    val isGuest = viewModel.isGuest

    // --- Lógica de balance (¡AHORA ES MUCHO MÁS SIMPLE!) ---
    var totalBalance = "$0.00" // Valor por defecto
    if (headerState is HeaderUiState.Success) {
        // Simplemente lee el valor, no lo calcules.
        totalBalance = (headerState as HeaderUiState.Success).formattedBalance
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            // 1. Header de la App (Flecha, Título, Campana)
            AppHeader(
                title = stringResource(R.string.header_transactions),
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )

            // 2. NUEVA TARJETA DE BALANCE
            TotalBalanceCard(
                balance = totalBalance, // <-- Pasa el String ya formateado
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. SECCIÓN DE BALANCEHEADER
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                BalanceHeaderSection(headerState = headerState)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4. CONTENIDO BLANCO
            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                color = AppBackground,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                // --- ¡LAZYCOLUMN REFACTORIZADA! ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 24.dp,
                        bottom = 96.dp
                    )
                ) {
                    // Itera sobre el Mapa (Key="April", Value=List(...))
                    groupedTransactions.forEach { (month, transactionsInMonth) ->

                        // STICKY HEADER
                        stickyHeader {
                            ListHeader(text = month) // Usa la Key como título
                        }

                        // LISTA DE TRANSACCIONES
                        // 'transactionsInMonth' ya es la lista filtrada por el ViewModel
                        items(transactionsInMonth, key = { it.id }) { transaction ->
                            TransactionItem(transaction)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomBar(
                navController = navController,
                isGuest = isGuest
            )
        }
    }
}

/**
 * Función "ayudante" para los títulos de la lista (April, March)
 */
@Composable
private fun ListHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppTextDark,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground) // Fondo para que no se vea transparente
            .padding(vertical = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun TransactionsScreenPreview() {
    MyApplicationTheme {
        TransactionsScreen(navController = rememberNavController())
    }
}