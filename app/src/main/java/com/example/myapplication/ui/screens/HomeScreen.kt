package com.example.myapplication.ui.screens

// --- ¡AQUÍ ESTÁN TODOS TUS COMPONENTES IMPORTADOS! ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.sampleTransactions
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.BalanceCard
import com.example.myapplication.ui.components.SummarySection
import com.example.myapplication.ui.components.TransactionItem
import com.example.myapplication.ui.components.TransactionTabs
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite
import com.example.myapplication.ui.theme.MyApplicationTheme


// --- DATOS DE PRUEBA (Mover a ViewModel luego) ---


// --- 1. LA PANTALLA PRINCIPAL (EL SCAFFOLD) ---
@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToNotification: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(2) } // 2 = "Monthly"

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. EL CONTENIDO PRINCIPAL (FONDO VERDE)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen) // Este verde se verá detrás de la barra de estado
        ) {
            HomeHeader(onNavigateToNotification = onNavigateToNotification) // Llama al header

            // 2. EL SURFACE BLANCO
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
                        // 3. ESPACIO PARA QUE LA BARRA FLOTE
                        bottom = 96.dp
                    )
                ) {
                    item {
                        SummarySection()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        TransactionTabs(
                            selectedTabIndex = selectedTab,
                            onTabSelected = { selectedTab = it }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    items(sampleTransactions) { transaction ->
                        TransactionItem(transaction)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // 4. LA BARRA DE NAVEGACIÓN (FLOTA ENCIMA)
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomBar(navController = navController)
        }

    }
}


// --- 2. HEADER (Se queda en HomeScreen.kt) ---
@Composable
private fun HomeHeader(onNavigateToNotification: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.home_welcome), // "Hi, Welcome Back"
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTextDark // <-- CORRECCIÓN (AppTextDark no se ve en fondo verde)
                )
                Text(
                    text = stringResource(R.string.home_good_morning), // "Good Morning"
                    style = MaterialTheme.typography.bodySmall, // Un estilo más chico
                    color = AppTextDark // <-- CORRECCIÓN
                )
            }
            IconButton(onClick =  onNavigateToNotification ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = stringResource(R.string.home_icon_desc_notification),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BalanceCard( // <-- LLAMA AL COMPONENTE
                title = stringResource(R.string.home_total_balance),
                amount = stringResource(R.string.home_amount_balance),
                icon = R.drawable.ic_income,
                modifier = Modifier.weight(1f)
            )
            BalanceCard( // <-- LLAMA AL COMPONENTE
                title = stringResource(R.string.home_total_expense),
                amount = stringResource(R.string.home_amount_expense),
                icon = R.drawable.ic_expense,
                modifier = Modifier.weight(1f),
                amountColor = AppIconBlueTint
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp), // Altura de la barra
        ) {

            // 2. La barra de FONDO (track, clara)
            Box(
                modifier = Modifier
                    .fillMaxSize() // Ocupa el 100%
                    .clip(CircleShape) // Píldora completa
                    .background(AppGreenLight),
                contentAlignment = Alignment.CenterEnd // Alinea el texto a la derecha
            ) {
                Text(
                    text = stringResource(R.string.home_progress_text_b),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextDark,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            // 3. La barra de PROGRESO (oscura)
            Box(
                modifier = Modifier
                    .fillMaxHeight() // Ocupa toda la altura
                    .fillMaxWidth(0.3f) // Ocupa el 30% del ancho
                    .clip(CircleShape) // Píldora completa
                    .background(AppTextDark), // (Tu AppTextDark/Fence Green)
                contentAlignment = Alignment.CenterStart // Alinea el texto a la izquierda
            ) {
                Text(
                    text = stringResource(R.string.home_progress_text_a),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextWhite,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto "Looks Good"
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = stringResource(R.string.home_icon_desc_check),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.home_looks_good),
                style = MaterialTheme.typography.bodySmall,
                color = AppTextDark // <-- CORRECCIÓN
            )
        }

    }
}

// --- 3. PREVIEW ---
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