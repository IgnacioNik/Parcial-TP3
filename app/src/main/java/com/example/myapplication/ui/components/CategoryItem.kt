package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.Category
import com.example.myapplication.ui.theme.AppTextDark

/**
 * Muestra un único ítem de categoría para la cuadrícula.
 * Este es un componente reutilizable.
 *
 * @param category El objeto de datos Category a mostrar.
 * @param onClick La acción a ejecutar cuando se presiona.
 * @param modifier Modificadores de Composable.
 */
@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Muestra la imagen del drawable directamente
        // (Asumiendo que el drawable ya incluye el fondo azul)
        Image(
            painter = painterResource(id = category.iconRes),
            contentDescription = category.title,
            modifier = Modifier
                .size(80.dp)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // El texto de abajo
        Text(
            text = category.title,
            style = MaterialTheme.typography.bodySmall,
            color = AppTextDark,
           fontWeight = FontWeight.Bold
        )
    }
}