package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppTextWhite


@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color, // Color del punto activo (sólido)
    inactiveBorderColor: Color // Color del BORDE del punto inactivo
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre los puntos
    ) {
        repeat(pageCount) { index ->

            val isSelected = (index == currentPage)

            Box(
                modifier = Modifier
                    .size(10.dp) // Tamaño del punto
                    .clip(CircleShape)
                    .then( // .then nos deja aplicar modificadores condicionalmente
                        if (isSelected) {
                            // --- PUNTO ACTIVO ---
                            Modifier.background(activeColor)
                        } else {
                            // --- PUNTO INACTIVO ---
                            Modifier
                                .background(AppTextWhite) // Fondo blanco
                                .border(1.dp, inactiveBorderColor, CircleShape) // Borde
                        }
                    )
            )
        }
    }
}