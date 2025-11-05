package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite
import com.example.myapplication.ui.viewmodels.SummaryUiState

@Composable
fun SummarySection(summaryState: SummaryUiState) { // <-- 1. ACEPTA EL ESTADO
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Ayuda a que la Row mida bien
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppGreen),
    ) {
        // 2. MANEJA LOS ESTADOS (LOADING, ERROR, SUCCESS)
        when (summaryState) {
            is SummaryUiState.Loading -> {
                // Muestra un spinner centrado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp), // Dale un poco de espacio
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppTextWhite)
                }
            }
            is SummaryUiState.Error -> {
                // Muestra un mensaje de error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = summaryState.message, color = AppTextWhite)
                }
            }
            is SummaryUiState.Success -> {
                // 3. DIBUJA EL CONTENIDO CON LOS DATOS DEL VIEWMODEL
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- COLUMNA 1: SAVINGS ---
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.size(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            GaplessCircularProgress(
                                modifier = Modifier.fillMaxSize(),
                                progress = summaryState.savingsProgress, // <-- DATO DEL VIEWMODEL
                                progressColor = AppIconBlueTint,
                                trackColor = AppTextWhite,
                                strokeWidth = 3.dp
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_car_saving),
                                contentDescription = stringResource(R.string.home_summary_savings),
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.home_summary_savings),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppTextDark, // <-- CORRECCIÓN DE COLOR
                            textAlign = TextAlign.Center
                        )
                    }

                    // --- DIVIDER VERTICAL ---
                    Spacer(modifier = Modifier.width(16.dp))
                    Divider(
                        modifier = Modifier.fillMaxHeight().width(1.dp),
                        color = AppTextWhite.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    // --- COLUMNA 2: REVENUE Y FOOD ---
                    Column(
                        modifier = Modifier.weight(1.5f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        SummaryItem(
                            iconRes = R.drawable.ic_revenue_stack,
                            title = stringResource(R.string.home_summary_revenue),
                            amount = "$${"%.2f".format(summaryState.revenueLastWeek)}", // <-- DATO DEL VIEWMODEL
                            amountColor = AppTextDark
                        )

                        Divider(color = AppTextWhite.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                        SummaryItem(
                            iconRes = R.drawable.ic_food_fork_knife,
                            title = stringResource(R.string.home_summary_food),
                            amount = "-$${"%.2f".format(summaryState.foodLastWeek)}", // <-- DATO DEL VIEWMODEL
                            amountColor = AppIconBlueTint
                        )
                    }
                }
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