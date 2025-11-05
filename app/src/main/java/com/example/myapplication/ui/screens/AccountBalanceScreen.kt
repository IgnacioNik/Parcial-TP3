package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.myapplication.ui.components.BalanceToggleButton
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.SharedViewModel
import com.example.myapplication.ui.viewmodels.SummaryUiState
import com.example.myapplication.ui.viewmodels.TransactionsViewModel

@Composable
fun AccountBalanceScreen(
    navController: NavController
) {
    // --- 2. OBTÉN EL VIEWMODEL "LIVIANO" COMPARTIDO ---
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val sharedViewModel: SharedViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)

    // --- 3. OBTÉN EL VIEWMODEL "PESADO" LOCAL ---
    val transactionsViewModel: TransactionsViewModel = viewModel()

    // --- 4. OBSERVA LOS ESTADOS DESDE EL VIEWMODEL CORRECTO ---
    val headerState by sharedViewModel.headerState.collectAsState()
    val isGuest by sharedViewModel.isGuest.collectAsState()

    val transactions by transactionsViewModel.transactionsState.collectAsState()
    val summaryState by transactionsViewModel.summaryState.collectAsState()

    // --- 5. ORDEN QUÉ CARGAR  ---
    LaunchedEffect(isGuest) {
        transactionsViewModel.loadData(isGuest)
    }

    var selectedButton by remember { mutableStateOf("") }

    // --- Lógica de botones  ---
    var incomeAmount = "$0.00"
    var expenseAmount = "$0.00"
    if (summaryState is SummaryUiState.Success) {
        incomeAmount = (summaryState as SummaryUiState.Success).formattedIncome
        expenseAmount = (summaryState as SummaryUiState.Success).formattedExpense
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            AppHeader(
                title = stringResource(R.string.header_account_balance),
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )

            Column(modifier = Modifier.padding(horizontal = 32.dp).offset(y = (-12).dp)) {
                BalanceHeaderSection(
                    headerState = headerState
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. BOTONES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                val incomeText = stringResource(R.string.account_balance_button_income)
                val expenseText = stringResource(R.string.account_balance_button_expense)

                BalanceToggleButton(
                    text = incomeText,
                    amount = incomeAmount,
                    iconRes = R.drawable.ic_income,
                    isSelected = selectedButton == incomeText,
                    onClick = { selectedButton = incomeText },
                    modifier = Modifier.weight(1f),
                    unselectedContentColor = AppGreen,
                    amountColor = AppTextDark
                )
                BalanceToggleButton(
                    text = expenseText,
                    amount = expenseAmount,
                    iconRes = R.drawable.ic_expense,
                    isSelected = selectedButton == expenseText,
                    onClick = { selectedButton = expenseText },
                    modifier = Modifier.weight(1f),
                    unselectedContentColor = AppIconBlueTint,
                    amountColor = AppIconBlueTint
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        top = 16.dp,
                        bottom = 96.dp
                    )
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.account_balance_list_header),
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
                isGuest = isGuest
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccountBalanceScreenPreview() {
    MyApplicationTheme {
        AccountBalanceScreen(navController = rememberNavController())
    }
}