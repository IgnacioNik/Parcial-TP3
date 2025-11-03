package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas // <-- NUEVO IMPORT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap // <-- NUEVO IMPORT
import androidx.compose.ui.graphics.drawscope.Stroke // <-- NUEVO IMPORT
import androidx.compose.ui.platform.LocalDensity // <-- NUEVO IMPORT
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp // <-- NUEVO IMPORT
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.*

@Composable
fun SummarySection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppGreen),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- COLUMNA 1: SAVINGS ---
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Box para el círculo de progreso
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // --- CAMBIO 1: USAMOS UN CANVAS ---
                    GaplessCircularProgress(
                        modifier = Modifier.fillMaxSize(),
                        progress = 0.5f, // 50%
                        progressColor = AppIconBlueTint, // El azul
                        trackColor = AppTextWhite,
                        strokeWidth = 3.dp
                    )
                    // --- FIN CAMBIO 1 ---

                    Icon(
                        painter = painterResource(id = R.drawable.ic_car_saving),
                        contentDescription = stringResource(R.string.home_summary_savings),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // --- CAMBIO 2: TEXTO ---
                Text(
                    text = stringResource(R.string.home_summary_savings), // Usa el string con \n
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextDark,
                    textAlign = TextAlign.Center
                    // <-- QUITAMOS el modifier = Modifier.width(70.dp)
                )
                // --- FIN CAMBIO 2 ---
            }

            // --- DIVIDER VERTICAL ---
            Spacer(modifier = Modifier.width(16.dp))
            Divider(
                modifier = Modifier.fillMaxHeight().width(1.dp),
                color = AppTextWhite.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1.5f),
                verticalArrangement = Arrangement.Center
            ) {
                SummaryItem(
                    iconRes = R.drawable.ic_revenue_stack, // (Tu ícono)
                    title = stringResource(R.string.home_summary_revenue),
                    amount = stringResource(R.string.home_amount_revenue),
                    amountColor = AppTextWhite
                )
                Divider(color = AppTextWhite.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                SummaryItem(
                    iconRes = R.drawable.ic_food_fork_knife, // (Tu ícono)
                    title = stringResource(R.string.home_summary_food),
                    amount = stringResource(R.string.home_amount_food),
                    amountColor = AppIconBlueTint // Color azul
                )
            }
        }
    }
}

// ... (El @Composable private fun SummaryItem queda igual) ...


// -----------------------------------------------------------------
// --- NUEVA FUNCIÓN "AYUDANTE" QUE DIBUJA EL CÍRCULO SIN ESPACIOS ---
// -----------------------------------------------------------------
@Composable
private fun GaplessCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    trackColor: Color,
    strokeWidth: Dp
) {
    val stroke = with(LocalDensity.current) { strokeWidth.toPx() }

    Canvas(modifier = modifier.padding(strokeWidth / 2)) {
        val diameter = size.minDimension - stroke
        val radius = diameter / 2f
        val topLeft = Offset(stroke / 2f, stroke / 2f)
        val size = Size(diameter, diameter)
        val startAngle = -90f

        // Dibuja el track (fondo)
        drawArc(
            color = trackColor,
            startAngle = startAngle,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // Dibuja el progreso (frente)
        drawArc(
            color = progressColor,
            startAngle = startAngle,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

/**
 * Función "ayudante" para los items de la derecha (Revenue y Food)
 */
@Composable
private fun SummaryItem(
    iconRes: Int,
    title: String,
    amount: String,
    amountColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = Color.Unspecified, // Usa el color original
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = AppTextDark
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor // Color dinámico
            )
        }
    }
}