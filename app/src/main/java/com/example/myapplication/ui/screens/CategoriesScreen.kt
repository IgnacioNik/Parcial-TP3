package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.data.models.sampleCategories
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.AppBottomBar
import com.example.myapplication.ui.components.AppHeader
import com.example.myapplication.ui.components.BalanceHeaderSection
import com.example.myapplication.ui.components.CategoryItem
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.HomeViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
) {
    val navGraphBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(navController.graph.id)
    }
    val viewModel: HomeViewModel = viewModel(viewModelStoreOwner = navGraphBackStackEntry)
    // 1. CONSUMIMOS LOS ESTADOS
    val headerState by viewModel.headerState.collectAsState()
    val categories by viewModel.categoriesState.collectAsState()
    val isGuest by viewModel.isGuest.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGreen)
        ) {
            // 1. Header de la App (Reutilizado)
            AppHeader(
                title = stringResource(R.string.categories_title),
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
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),


                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 32.dp,
                        bottom = 96.dp
                    ),

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



@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    MyApplicationTheme {
        val previewCategories = sampleCategories
        CategoriesScreen(navController = rememberNavController())
    }
}