package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.ui.components.FormTextField
import com.example.myapplication.ui.components.PrimaryButton
import com.example.myapplication.ui.components.TextLinkButton
import com.example.myapplication.ui.theme.*
import com.example.myapplication.ui.viewmodels.RegisterUiState
import com.example.myapplication.ui.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {

    val fullName = viewModel.fullName
    val email = viewModel.email
    val mobile = viewModel.mobile
    val dob = viewModel.dob
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword
    val passwordVisible = viewModel.passwordVisible
    val confirmPasswordVisible = viewModel.confirmPasswordVisible

    val registerState = viewModel.registerState.collectAsState().value

    val context = LocalContext.current


    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is RegisterUiState.Error -> {
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
@Composable
private fun RegisterFormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppTextDark,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        FormTextField(
            value = value,
            onValueChange = onValueChange,
            placeholderText = placeholder,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = onPasswordToggle) {
                        Icon(imageVector = image, "toggle password")
                    }
                }
            } else { null }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MyApplicationTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onBackToLoginClick = {}
        )
    }
}