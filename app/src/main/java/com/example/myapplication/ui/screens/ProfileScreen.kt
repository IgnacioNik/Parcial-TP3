package com.example.myapplication.ui.screens

import android.app.Application // <-- 1. IMPORTA
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // <-- 2. IMPORTA
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel // <-- 3. IMPORTA
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.ProfileOption
import com.example.myapplication.data.models.profileScreenOptions
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader
import com.example.myapplication.ui.components.ProfileMenuItem
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HeaderUiState
import com.example.myapplication.ui.viewmodels.HomeViewModel

@Composable
fun ProfileScreen(
    navController: NavController
) {
    // --- 5. AÑADE LA INYECCIÓN DEL VIEWMODEL AQUÍ ---
    // La función viewModel() es lo suficientemente inteligente
    // para proveer TANTO Application como SavedStateHandle a HomeViewModel.
    // NO se necesita una Factory.
    val viewModel: HomeViewModel = viewModel()
    val isGuest = viewModel.isGuest
    val headerState by viewModel.headerState.collectAsState()

    // --- 1. VALORES POR DEFECTO USANDO STRINGS ---
    var userName = stringResource(R.string.profile_guest_user)
    var userId = stringResource(R.string.profile_guest_id_na)

    if (headerState is HeaderUiState.Success) {
        val userData = (headerState as HeaderUiState.Success).userData
        userName = userData.name
        // --- 2. PREFIJO USANDO STRING CON FORMATO ---
        userId = stringResource(R.string.profile_user_id_prefix, userData.userId)
    }

    val profileImageSize = 120.dp
    val overlap = 60.dp // Mitad de la imagen

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().background(AppGreen)) {

            AppHeader(
                title = stringResource(R.string.profile_title),
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )

            // --- LAYOUT BOX PARA SUPERPONER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // --- 1. EL CONTENIDO BLANCO (FONDO) ---
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = overlap),
                    color = AppBackground,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(
                            top = overlap + 80.dp,
                            bottom = 96.dp
                        )
                    ) {
                        // --- 2. LISTA DE OPCIONES ---
                        // (Esto ahora funciona porque ProfileMenuItem espera
                        // un ProfileOption que tiene un 'titleRes' Int)
                        items(profileScreenOptions, key = { it.id }) { option ->
                            ProfileMenuItem(
                                item = option,
                                onClick = {
                                    if (option.id == "logout") {
                                        navController.navigate(Screen.Splash.route) {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        // TODO
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }

                // --- 3. HEADER FLOTANTE (IMAGEN + TEXTO) ---
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .zIndex(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // La imagen
                    Image(
                        painter = painterResource(id = R.drawable.img_profile_placeholder),
                        // --- 3. CONTENT DESCRIPTION USANDO STRING ---
                        contentDescription = stringResource(R.string.profile_cd_profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(profileImageSize)
                            .clip(CircleShape)
                    )

                    // El texto
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AppTextDark
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        text = userId,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTextDark.copy(alpha = 0.6f)
                    )
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

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyApplicationTheme {
        ProfileScreen(navController = rememberNavController())
    }
}