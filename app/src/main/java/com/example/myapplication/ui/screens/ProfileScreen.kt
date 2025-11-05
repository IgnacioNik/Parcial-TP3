package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.profileScreenOptions
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader
import com.example.myapplication.ui.components.ProfileMenuItem
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.HeaderUiState
import com.example.myapplication.ui.viewmodels.HomeViewModel

@Composable
fun ProfileScreen(
    navController: NavController
) {
    // --- 2. OBTÉN EL VIEWMODEL COMPARTIDO ---
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val viewModel: HomeViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)

    // --- 3. "DESENVUELVE" LOS ESTADOS ---
    val isGuest by viewModel.isGuest.collectAsState()
    val headerState by viewModel.headerState.collectAsState()

    // --- 4. LÓGICA DE TEXTO ---
    val defaultUserName = stringResource(R.string.profile_guest_user)
    val defaultUserId = stringResource(R.string.profile_guest_id_na)

    var userName = defaultUserName
    var userId = defaultUserId


    if (headerState is HeaderUiState.Success) {
        val userData = (headerState as HeaderUiState.Success).userData

        // Comprueba si el nombre de la API NO es nulo o blanco
        userName = if (userData.name.isNullOrBlank()) {
            defaultUserName // Si lo es, usa el por defecto
        } else {
            userData.name // Si no, usa el de la API
        }

        // Comprueba si el ID de la API NO es nulo, blanco, o el string "null"
        userId = if (userData.userId.isNullOrEmpty() || userData.userId == "null") {
            defaultUserId // Si lo es, usa el por defecto
        } else {
            // Si no, usa el de la API con el formato
            stringResource(R.string.profile_user_id_prefix, userData.userId)
        }
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
                        items(profileScreenOptions, key = { it.id }) { option ->
                            ProfileMenuItem(
                                item = option,
                                onClick = {
                                    if (option.id == "logout") {
                                        navController.navigate(Screen.Welcome.route) {
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


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyApplicationTheme {
        ProfileScreen(navController = rememberNavController())
    }
}