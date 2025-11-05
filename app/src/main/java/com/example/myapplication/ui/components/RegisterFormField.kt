package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppTextDark

/**
 * Un campo de texto estandarizado para formularios de registro/login.
 * Incluye una etiqueta, un placeholder y lógica para campos de contraseña.
 */
@Composable
fun RegisterFormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppTextDark,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Asumiendo que tienes un componente FormTextField.
        // Si no, reemplaza esto con tu OutlinedTextField
        FormTextField(
            value = value,
            onValueChange = onValueChange,
            placeholderText = placeholder,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = onPasswordToggle) {
                        // --- CAMBIO DE STRING AQUÍ ---
                        Icon(
                            imageVector = image,
                            contentDescription = stringResource(R.string.cd_toggle_password)
                        )
                    }
                }
            } else { null }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}