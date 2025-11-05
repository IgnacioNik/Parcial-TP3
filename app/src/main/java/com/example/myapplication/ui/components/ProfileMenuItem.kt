package com.example.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.ProfileOption
import com.example.myapplication.ui.theme.AppTextDark

/**
 * Muestra una única fila del menú de perfil.
 */
@Composable
fun ProfileMenuItem(
    item: ProfileOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono (ya usa tus imágenes pre-hechas)
        Image(
            painter = painterResource(id = item.iconRes),
            contentDescription = stringResource(id = item.titleRes), // <-- 2. USA EL STRING
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Texto
        Text(
            text = stringResource(id = item.titleRes), // <-- 3. USA EL STRING
            style = MaterialTheme.typography.bodyLarge,
            color = AppTextDark,
            fontWeight = FontWeight.SemiBold
        )
    }
}