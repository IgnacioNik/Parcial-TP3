package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextWhite

/**
 * La tarjeta blanca que muestra el balance total
 * en la pantalla de Transacciones.
 */
@Composable
fun TotalBalanceCard(
    balance: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), // <-- Es buena idea que el Card también ocupe el ancho
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppTextWhite), // Fondo Blanco
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // <-- 3. HAZ QUE LA COLUMNA OCUPE EL ANCHO
            horizontalAlignment = Alignment.CenterHorizontally // <-- 4. ALINEA LOS HIJOS AL CENTRO
        ) {
            Text(
                text = stringResource(R.string.transactions_total_balance),
                style = MaterialTheme.typography.bodyMedium,
                color = AppTextDark
                // Ya no necesitas 'textAlign', la columna se encarga.
            )
            Text(
                text = balance,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppTextDark
                // Ya no necesitas 'textAlign'.
            )
        }
    }
}