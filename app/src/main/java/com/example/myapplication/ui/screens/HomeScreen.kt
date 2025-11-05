package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.HomeHeader
import com.example.myapplication.ui.components.SummarySection
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.components.TransactionTabs
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.SharedViewModel
import com.example.myapplication.ui.viewmodels.TransactionsViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToNotification: () -> Unit,
    isGuest: Boolean
) {
    // --- 2. OBTÉN EL VIEWMODEL "LIVIANO" COMPARTIDO ---
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val sharedViewModel: SharedViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)

    // --- 3. OBTÉN EL VIEWMODEL "PESADO" LOCAL ---
    val transactionsViewModel: TransactionsViewModel = viewModel()

    // --- 4. ORDÉNALES QUÉ CARGAR ---
    LaunchedEffect(isGuest) {

        sharedViewModel.loadUser(isGuest)

        transactionsViewModel.loadData(isGuest)
    }

    var selectedTab by remember { mutableStateOf(2) }

    // --- 5. OBSERVA LOS ESTADOS DESDE EL VIEWMODEL CORRECTO ---
    val headerState by sharedViewModel.headerState.collectAsState()
    val isGuestFromVM by sharedViewModel.isGuest.collectAsState()

    val transactions by transactionsViewModel.transactionsState.collectAsState()
    val summaryState by transactionsViewModel.summaryState.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            HomeHeader(
                onNavigateToNotification = onNavigateToNotification,
                headerState = headerState
            )

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
                    item {
                        SummarySection(summaryState = summaryState)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        TransactionTabs(
                            selectedTabIndex = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

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
                isGuest = isGuestFromVM
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            navController = rememberNavController(),
            onNavigateToNotification = {},
            isGuest = true
        )
    }
}