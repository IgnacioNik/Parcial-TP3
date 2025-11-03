package com.example.myapplication.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.myapplication.data.models.sampleNotifications
import com.example.myapplication.navigation.Screen // <-- 1. IMPORTA "Screen"
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader // <-- 2. ¡IMPORTA TU NUEVO HEADER!
import com.example.myapplication.ui.components.NotificationItemCard
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val isGuest = viewModel.isGuest

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().background(AppGreen)) {

            // --- 3. ¡AQUÍ LO USAMOS! ---
            AppHeader(
                title = stringResource(R.string.notification_title),
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { /* TODO: Settings */ }
            )
            // --- FIN DEL CAMBIO ---

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = AppBackground,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 0.dp,
                        bottom = 96.dp
                    )
                ) {
                    sampleNotifications.forEach { group ->
                        stickyHeader {
                            Text(
                                text = group.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTextDark, // (Tu AppTextDark)
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(AppBackground)
                                    .padding(top = 24.dp, bottom = 4.dp)
                            )
                        }

                        items(group.notifications, key = { it.id }) { item ->
                            NotificationItemCard(item = item)
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

// --- 4. BORRAMOS EL "private fun NotificationHeader" DE AQUÍ ---


@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    MyApplicationTheme {
        NotificationScreen(navController = rememberNavController())
    }
}