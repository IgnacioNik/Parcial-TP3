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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.SummarySection
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.components.TransactionTabs
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HomeViewModel
// VVV ¡IMPORTA TU NUEVO HEADER! VVV
import com.example.myapplication.ui.components.HomeHeader

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToNotification: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(2) } // 2 = "Monthly"

    // OBSERVAMOS LOS ESTADOS DEL VIEWMODEL
    val headerState by viewModel.headerState.collectAsState()
    val transactions by viewModel.transactionsState.collectAsState()
    val summaryState by viewModel.summaryState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            // ¡LLAMA AL NUEVO COMPONENTE!
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
            AppBottomBar(navController = navController)
        }
    }
}


// --- EL HEADER Y PROGRESSBAR FUERON MOVIDOS A "ui/components/HomeHeader.kt" ---


// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            navController = rememberNavController(),
            onNavigateToNotification = {}
        )
    }
}