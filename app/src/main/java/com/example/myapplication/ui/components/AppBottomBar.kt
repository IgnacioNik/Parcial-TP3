package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.R
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppTextDark

/**
 * La barra de navegación inferior personalizada ("píldora" flotante)
 */
@Composable
fun AppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        // <-- CAMBIO: Quitamos todo el padding
        color = AppGreenLight, // Fondo verde pálido
        // VVV CAMBIO: La forma ahora coincide con el Surface de arriba VVV
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // Mantenemos la altura
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 3. CADA ÍCONO
            BottomBarIcon(
                iconRes = R.drawable.ic_home,
                description = stringResource(R.string.nav_desc_home),
                isSelected = currentRoute == Screen.Home.route,
                onClick = { navController.navigate(Screen.Home.route) }
            )
            BottomBarIcon(
                iconRes = R.drawable.ic_account_balance,
                description = stringResource(R.string.nav_desc_balance),
                isSelected = currentRoute == Screen.AccountBalance.route,
                onClick = { navController.navigate(Screen.AccountBalance.route) }
            )
            BottomBarIcon(
                iconRes = R.drawable.ic_transaction,
                description = stringResource(R.string.nav_desc_transactions),
                isSelected = currentRoute == Screen.Transactions.route,
                onClick = { navController.navigate(Screen.Transactions.route) }
            )
            BottomBarIcon(
                iconRes = R.drawable.ic_category,
                description = stringResource(R.string.nav_desc_categories),
                isSelected = currentRoute == Screen.Categories.route,
                onClick = { navController.navigate(Screen.Categories.route) }
            )
            BottomBarIcon(
                iconRes = R.drawable.ic_profile,
                description = stringResource(R.string.nav_desc_profile),
                isSelected = currentRoute == Screen.Profile.route,
                onClick = { navController.navigate(Screen.Profile.route) }
            )
        }
    }
}

/**
 * Función "ayudante" para cada ícono de la barra
 */
@Composable
private fun BottomBarIcon(
    iconRes: Int,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // El fondo (círculo verde) solo se muestra si está seleccionado
    val backgroundColor = if (isSelected) AppGreen else Color.Transparent

    // El tinte del ícono es blanco si está seleccionado, u oscuro si no
    val iconColor = AppTextDark

    IconButton(onClick = onClick) {
        // Un Box para el fondo circular
        Box(
            modifier = Modifier
                .size(48.dp) // Tamaño del círculo verde
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = description,
                tint = iconColor, // Aplicamos el tinte
                modifier = Modifier.size(24.dp)
            )
        }
    }
}