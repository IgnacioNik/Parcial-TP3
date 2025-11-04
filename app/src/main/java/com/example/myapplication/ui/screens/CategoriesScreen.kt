package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.Category
import com.example.myapplication.data.models.sampleCategories
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.*
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.HomeViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    // 1. CONSUMIMOS LOS ESTADOS
    val headerState by viewModel.headerState.collectAsState()
    val categories by viewModel.categoriesState.collectAsState()
    val isGuest = viewModel.isGuest

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            // 1. Header de la App (Reutilizado)
            AppHeader(
                title = stringResource(R.string.categories_title), // (Añade "Categories" a tu strings.xml)
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notification.route) }
            )

            // 2. Sección de Balance (Reutilizado)
            Column(modifier = Modifier.padding(horizontal = 32.dp).offset(y = (-12).dp)) {
                BalanceHeaderSection(headerState = headerState)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. CONTENIDO BLANCO (con la cuadrícula)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = AppBackground,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                // Usamos LazyVerticalGrid para la cuadrícula
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 columnas
                    modifier = Modifier.fillMaxWidth(),

                    // --- ¡AQUÍ ESTÁ LA SOLUCIÓN! ---
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 32.dp,
                        bottom = 96.dp // <-- Padding extra para la barra de navegación
                    ),
                    // Espaciado horizontal y vertical entre ítems
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        CategoryItem(
                            category = category,
                            onClick = {
                                // TODO: Navegar a la pantalla de detalle de categoría
                            }
                        )
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

/**
 * Composable privado para CADA ítem de la cuadrícula
 */



@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    // Necesitarás un HomeViewModel de Preview o datos de muestra falsos
    // Por ahora, lo mostramos así:
    MyApplicationTheme {
        // Creamos una lista falsa solo para la preview
        val previewCategories = sampleCategories

        // (El preview no puede mostrar el ViewModel,
        // así que el headerState y el isGuest no funcionarán aquí,
        // pero la cuadrícula sí se verá)
        CategoriesScreen(navController = rememberNavController())
    }
}