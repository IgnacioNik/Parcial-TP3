package com.example.myapplication.ui.screens

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
import com.example.myapplication.ui.components.*
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HomeViewModel
import com.example.myapplication.ui.viewmodels.SummaryUiState

@Composable
fun AccountBalanceScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel() // Reusa el ViewModel
) {
    // 1. ESTADOS
    val headerState by viewModel.headerState.collectAsState()
    val transactions by viewModel.transactionsState.collectAsState()
    val summaryState by viewModel.summaryState.collectAsState()

    var selectedButton by remember { mutableStateOf("") }

    // --- Lógica para los montos de los botones ---
    var incomeAmount = "$0.00"
    var expenseAmount = "$0.00"
    if (summaryState is SummaryUiState.Success) {
        val summaryData = (summaryState as SummaryUiState.Success)
        // (La API no nos da "Expense" solo, así que usamos el total de "Food" como ejemplo)
        incomeAmount = "$${"%.2f".format(summaryData.revenueLastWeek)}"
        expenseAmount = "-$${"%.2f".format(summaryData.foodLastWeek)}"
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            // 1. Header de la App (Flecha, Título, Campana)
            AppHeader(
                title = stringResource(R.string.header_account_balance), // <-- STRING
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )

            // 2. Sección de Balance (Tarjetas y Barra de Progreso)
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                BalanceHeaderSection(headerState = headerState)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. BOTONES (ahora con strings y datos del ViewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BalanceToggleButton(
                    text = stringResource(R.string.account_balance_button_income), // <-- STRING
                    amount = incomeAmount, // <-- DATO
                    iconRes = R.drawable.ic_income,
                    isSelected = selectedButton == stringResource(R.string.account_balance_button_income),
                    onClick = { selectedButton = "Income" },
                    modifier = Modifier.weight(1f),
                    unselectedContentColor = AppGreen
                )
                BalanceToggleButton(
                    text = stringResource(R.string.account_balance_button_expense), // <-- STRING
                    amount = expenseAmount, // <-- DATO
                    iconRes = R.drawable.ic_expense,
                    isSelected = selectedButton == stringResource(R.string.account_balance_button_expense),
                    onClick = { selectedButton = "Expense" },
                    modifier = Modifier.weight(1f),
                    unselectedContentColor = AppIconBlueTint
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. CONTENIDO BLANCO
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
                    // 5. TÍTULO DE LA LISTA
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.account_balance_list_header), // <-- STRING
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppTextDark
                            )
                            TextButton(onClick = { /* TODO */ }) {
                                Text(
                                    text = stringResource(R.string.account_balance_list_see_all),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTextDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // 6. LISTA DE TRANSACCIONES
                    items(transactions, key = { it.id }) { transaction ->
                        TransactionItem(transaction)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomBar(
                navController = navController,
                isGuest = viewModel.isGuest
            )
        }
    }
}


// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun AccountBalanceScreenPreview() {
    MyApplicationTheme {
        AccountBalanceScreen(navController = rememberNavController())
    }
}