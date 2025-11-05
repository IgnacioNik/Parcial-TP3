package com.example.myapplication.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader
import com.example.myapplication.ui.components.NotificationItemCard
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.SharedViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavController,
) {
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val viewModel: SharedViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)

    val isGuest by viewModel.isGuest.collectAsState()
    // ---  ¡CONSUME EL ESTADO DEL VIEWMODEL! ---
    val notificationGroups by viewModel.notificationsState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().background(AppGreen)) {

            AppHeader(
                title = stringResource(R.string.notification_title),
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { /* TODO: Settings */ }
            )

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
                    // --- 4. USA LA LISTA DEL VIEWMODEL ---
                    notificationGroups.forEach { group ->
                        stickyHeader {
                            Text(
                                text = group.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = AppTextDark,
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



@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    MyApplicationTheme {
        NotificationScreen(navController = rememberNavController())
    }
}