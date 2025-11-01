package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.components.PagerIndicator
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppGreenCircleBg
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onNavigateToWelcome: () -> Unit) {
    // El PagerState controla en qué página estamos (tenemos 2 páginas)
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope() // Para animar el scroll

    Box(modifier = Modifier.fillMaxSize().background(AppGreen)) {

        // El Pager que contiene las páginas que se deslizan
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Muestra la página correspondiente
            OnboardingPage(page = page)
        }

        // --- Controles (Indicador de puntos y botón "Next") ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicador de puntos (Dot indicator)
            TextButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < 1) {
                            // Si estamos en la página 0, animamos a la página 1
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Si estamos en la última página (1), navegamos a la app
                            onNavigateToWelcome()
                        }
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.next_button),
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTextDark
                )
            }

            // 2. ESPACIO
            Spacer(modifier = Modifier.height(16.dp))

            // 3. PUNTOS
            PagerIndicator(
                pageCount = 2,
                currentPage = pagerState.currentPage,
                activeColor = AppGreen, // El punto activo sigue siendo verde sólido
                inactiveBorderColor = AppTextDark // El borde inactivo es el verde oscuro
            )
        }
    }
}

/**
 * El Composable reutilizable para el layout de UNA página de onboarding
 */
@Composable
fun OnboardingPage(page: Int) {
    // Decide qué imagen mostrar
    val imageRes = if (page == 0) {
        R.drawable.ic_onboarding_a // (la mano con monedas)
    } else {
        R.drawable.ic_onboarding_b // (la mano con el teléfono)
    }

    val imageDesc = if (page == 0) {
        stringResource(R.string.onboarding_image_1_desc)
    } else {
        stringResource(R.string.onboarding_image_2_desc)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // --- Parte Superior (Verde) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f), // Ocupa el 40% superior
            contentAlignment = Alignment.Center
        ) {
            // --- Lógica de Títulos ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (page == 0) {
                    // --- Pantalla 2-A ---
                    Text(
                        text = stringResource(R.string.onboarding_title_1_line_1),
                        style = MaterialTheme.typography.titleLarge, // Estilo más chico
                        color = AppTextDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_1_line_2),
                        style = MaterialTheme.typography.titleLarge, // Estilo más grande
                        color = AppTextDark,
                        textAlign = TextAlign.Center
                    )
                } else {
                    // --- Pantalla 2-B ---
                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_1),
                        // Usamos un estilo más chico que displaySmall para que entre bien
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_2),
                        // Usamos un estilo más chico que displaySmall para que entre bien
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_3),
                        // Usamos un estilo más chico que displaySmall para que entre bien
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }

        // --- Parte Inferior (Blanca con bordes redondeados) ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            color = AppBackground,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp), // Padding para dejar espacio a los controles
                contentAlignment = Alignment.Center
            ) {

                // --- CAMBIO PRINCIPAL: CÍRCULO DE FONDO ---
                Box(
                    contentAlignment = Alignment.Center
                ) {

                    // 1. EL CÍRCULO (VA AL FONDO)
                    Box(
                        modifier = Modifier
                            .size(220.dp) // <-- El círculo es más pequeño
                            .background(AppGreenCircleBg, shape = CircleShape)
                    )

                    // 2. LA IMAGEN (VA ENCIMA)
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = imageDesc,
                        modifier = Modifier.size(250.dp) // <-- La imagen es más grande
                    )
                }
                // ---------------------------------
            }
                // ------------------------------------------
        }
    }
}



@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    MyApplicationTheme {
        OnboardingScreen(onNavigateToWelcome = {})
    }
}