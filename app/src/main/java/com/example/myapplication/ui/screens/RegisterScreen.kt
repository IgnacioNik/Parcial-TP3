package com.example.myapplication.ui.screens

import android.app.Application // <-- 1. IMPORTA APPLICATION
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <-- 2. IMPORTA LOCALCONTEXT
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider // <-- 3. IMPORTA VIEWMODELPROVIDER
import androidx.lifecycle.viewmodel.compose.viewModel // <-- 4. ASEGÚRATE DE USAR ESTE IMPORT
import com.example.myapplication.R
import com.example.myapplication.ui.components.PrimaryButton
import com.example.myapplication.ui.components.RegisterFormField
import com.example.myapplication.ui.components.TextLinkButton
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.RegisterUiState
import com.example.myapplication.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit,
    // viewModel: RegisterViewModel = viewModel() // <-- 5. BORRAMOS EL VIEWMODEL DE LA FIRMA
) {

    // --- 6. AÑADIMOS LA LÓGICA DE LA FÁBRICA ---
    val context = LocalContext.current
    val application = context.applicationContext as Application
    // Creamos una fábrica que sabe cómo construir AndroidViewModels
    val factory = ViewModelProvider.AndroidViewModelFactory(application)
    // Le pasamos la fábrica al creador del ViewModel
    val viewModel: RegisterViewModel = viewModel(factory = factory)
    // --- FIN DEL CAMBIO ---

    val fullName = viewModel.fullName
    val email = viewModel.email
    val mobile = viewModel.mobile
    val dob = viewModel.dob
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword
    val passwordVisible = viewModel.passwordVisible
    val confirmPasswordVisible = viewModel.confirmPasswordVisible

    val registerState = viewModel.registerState.collectAsState().value

    // (El resto de tu pantalla se queda exactamente igual)
    // ...
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
                // NO: stringResource(R.string.register_toast_success)
                // SÍ: context.getString(R.string.register_toast_success)
                Toast.makeText(context, context.getString(R.string.register_toast_success), Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is RegisterUiState.Error -> {
                // 'registerState.message' ya es un String, así que está bien
                Toast.makeText(context, registerState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetErrorState()
            }
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(AppGreen)) {

        Box(
            modifier = Modifier.fillMaxWidth().weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineSmall,
                color = AppTextDark
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            color = AppBackground,
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                RegisterFormField(
                    label = stringResource(R.string.register_label_full_name),
                    value = fullName,
                    placeholder = stringResource(R.string.login_placeholder_email),
                    onValueChange = { viewModel.fullName = it }
                )

                RegisterFormField(
                    label = stringResource(R.string.register_label_email),
                    value = email,
                    placeholder = stringResource(R.string.login_placeholder_email),
                    onValueChange = { viewModel.email = it }
                )

                RegisterFormField(
                    label = stringResource(R.string.register_label_mobile),
                    value = mobile,
                    placeholder = stringResource(R.string.register_placeholder_mobile),
                    onValueChange = { viewModel.mobile = it }
                )

                RegisterFormField(
                    label = stringResource(R.string.register_label_dob),
                    value = dob,
                    placeholder = stringResource(R.string.register_placeholder_dob),
                    onValueChange = { viewModel.dob = it }
                )

                RegisterFormField(
                    label = stringResource(R.string.login_label_password),
                    value = password,
                    placeholder = stringResource(R.string.login_placeholder_password),
                    onValueChange = { viewModel.password = it },
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordToggle = { viewModel.onPasswordVisibilityToggle() }
                )

                RegisterFormField(
                    label = stringResource(R.string.register_label_confirm_password),
                    value = confirmPassword,
                    placeholder = stringResource(R.string.login_placeholder_password),
                    onValueChange = { viewModel.confirmPassword = it },
                    isPassword = true,
                    passwordVisible = confirmPasswordVisible,
                    onPasswordToggle = { viewModel.onConfirmPasswordVisibilityToggle() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.register_legal_text),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextGrey,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = stringResource(R.string.sign_up_button),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { viewModel.register() }
                )

                if (registerState == RegisterUiState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextLinkButton(
                    text = stringResource(R.string.register_link_log_in),
                    onClick = onBackToLoginClick
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}