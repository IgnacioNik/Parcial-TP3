package com.example.myapplication.ui.screens

// VVV ¡IMPORTA TU NUEVO HEADER! VVV
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
import com.example.myapplication.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToNotification: () -> Unit,
) {
    val viewModel: HomeViewModel = viewModel()

    var selectedTab by remember { mutableStateOf(2) } // 2 = "Monthly"

    // OBSERVAMOS LOS ESTADOS DEL VIEWMODEL
    val headerState by viewModel.headerState.collectAsState()
    val transactions by viewModel.transactionsState.collectAsState()
    val summaryState by viewModel.summaryState.collectAsState()
    val isGuest = viewModel.isGuest

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
            AppBottomBar(
                navController = navController,
                isGuest = isGuest // <-- ¡AQUÍ ESTÁ EL CAMBIO!
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
            onNavigateToNotification = {}
        )
    }
}