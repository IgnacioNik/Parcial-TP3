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
    val transactions by viewModel.transactionsState.collectAsState()
    val isGuest = viewModel.isGuest

    // --- Lógica para el balance (igual que en AccountBalance) ---
    var totalBalance = "$0.00"
    if (headerState is HeaderUiState.Success) {
        totalBalance = "$${"%.2f".format((headerState as HeaderUiState.Success).userData.balance)}"
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
                balance = totalBalance,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                BalanceHeaderSection(headerState = headerState)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. CONTENIDO BLANCO
            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                color = AppBackground,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 24.dp,
                        bottom = 96.dp
                    )
                ) {
                    // 4. STICKY HEADER "April"
                    stickyHeader {
                        ListHeader(text = stringResource(R.string.transactions_group_april))
                    }

                    // 5. LISTA DE TRANSACCIONES (de April)
                    // (Esta lógica se puede mejorar en el ViewModel,
                    // por ahora filtramos la lista completa)
                    items(transactions.filter { it.date.contains("April", true) }, key = { it.id }) { transaction ->
                        TransactionItem(transaction)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 6. STICKY HEADER "March"
                    stickyHeader {
                        ListHeader(text = stringResource(R.string.transactions_group_march))
                    }

                    // 7. LISTA DE TRANSACCIONES (de March)
                    items(transactions.filter { it.date.contains("March", true) }, key = { it.id }) { transaction ->
                        TransactionItem(transaction)
                        Spacer(modifier = Modifier.height(16.dp))
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