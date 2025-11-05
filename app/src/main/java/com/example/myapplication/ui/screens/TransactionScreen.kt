package com.example.myapplication.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader
import com.example.myapplication.ui.components.BalanceHeaderSection
import com.example.myapplication.ui.components.TotalBalanceCard
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.HeaderUiState
import com.example.myapplication.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
) {
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val viewModel: HomeViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)
    // 1. ESTADOS
    val headerState by viewModel.headerState.collectAsState()
    val groupedTransactions by viewModel.groupedTransactionsState.collectAsState()
    val isGuest by viewModel.isGuest.collectAsState()

    // --- Lógica de balance
    var totalBalance = "$0.00"
    if (headerState is HeaderUiState.Success) {
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
                balance = totalBalance,
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
                            ListHeader(text = month)
                        }

                        // LISTA DE TRANSACCIONES
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
            .background(AppBackground)
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