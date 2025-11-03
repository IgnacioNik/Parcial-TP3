package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.sampleNotifications
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.NotificationItemCard
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite
import com.example.myapplication.ui.theme.MyApplicationTheme

//@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavController
) {
    // --- 2. USAMOS UN BOX COMO RAÍZ ---
    Box(modifier = Modifier.fillMaxSize()) {

        // --- 3. EL CONTENIDO DE LA PANTALLA ---
        Column(modifier = Modifier.fillMaxSize().background(AppGreen)) {

            NotificationHeader(
                onBackClick = { navController.popBackStack() }
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
                        top = 0.dp, // El padding del sticky header lo maneja
                        // VVV CAMBIO: Padding para la barra VVV
                        bottom = 96.dp
                    )
                ) {
                    sampleNotifications.forEach { group ->
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

        // --- 4. LA BARRA DE NAVEGACIÓN (FLOTA ENCIMA) ---
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomBar(navController = navController)
        }
    }
}

// --- HEADER PERSONALIZADO ---
@Composable
private fun NotificationHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ... (Icono Izquierdo - Flecha)
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AppTextWhite,
                modifier = Modifier.size(28.dp)
            )
        }
        // ... (Texto Centro - "Notification")
        Text(
            text = stringResource(R.string.notification_title),
            style = MaterialTheme.typography.bodyLarge,
            color = AppTextDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        // ... (Icono Derecho - Campana)
        IconButton(onClick = { /* TODO: Settings */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = stringResource(R.string.home_icon_desc_notification),
                tint = Color.Unspecified
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