package com.example.myapplication.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.ui.components.PrimaryButton
import com.example.myapplication.ui.components.RegisterFormField
import com.example.myapplication.ui.components.SecondaryButton
import com.example.myapplication.ui.components.TextLinkButton
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppIconBlueTint
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextGrey
import com.example.myapplication.ui.theme.AppVividBlue
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodels.LoginUiState
import com.example.myapplication.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGuestLogin: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onFacebookprintClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onBottomSignUpClick: () -> Unit
) {

    // ---  AÑADIMOS LA LÓGICA DE LA FÁBRICA ---
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val factory = ViewModelProvider.AndroidViewModelFactory(application)
    val viewModel: LoginViewModel = viewModel(factory = factory)


    val email = viewModel.email
    val password = viewModel.password
    val passwordVisible = viewModel.passwordVisible
    val loginState = viewModel.loginState.collectAsState().value



    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                onLoginSuccess() // Navega a Home (logueado)
            }
            is LoginUiState.GuestLogin -> {
                onGuestLogin() // Navega a Home (invitado)
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetErrorState() // Resetea el error
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
                text = stringResource(R.string.login_welcome_title),
                style = MaterialTheme.typography.displaySmall,
                color = AppTextDark
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            color = AppBackground,
            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                RegisterFormField(
                    label = stringResource(R.string.login_label_email),
                    value = email,
                    placeholder = stringResource(R.string.login_placeholder_email),
                    onValueChange = { viewModel.email = it }
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

                Spacer(modifier = Modifier.height(36.dp))

                PrimaryButton(
                    text = stringResource(R.string.log_in_button),
                    modifier = Modifier.padding(horizontal = 40.dp),
                    onClick = { viewModel.login() }
                )


                if (loginState == LoginUiState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(2.dp))

                TextLinkButton(
                    text = stringResource(R.string.forgot_password_button),
                    onClick = onForgotPasswordClick
                )

                Spacer(modifier = Modifier.height(2.dp))

                SecondaryButton(
                    text = stringResource(R.string.sign_up_button),
                    modifier = Modifier.padding(horizontal = 40.dp),
                    onClick = onSignUpClick
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = buildAnnotatedString {

                        append(stringResource(R.string.login_use_fingerprint_part1))


                        withStyle(style = SpanStyle(color = AppIconBlueTint)) {
                            append(stringResource(R.string.login_use_fingerprint_part2))
                        }


                        append(stringResource(R.string.login_use_fingerprint_part3))
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTextGrey
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.login_or_sign_up_with),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTextGrey
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    IconButton(onClick = onFacebookprintClick) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_facebook),
                            contentDescription = stringResource(R.string.login_icon_desc_facebook),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    IconButton(onClick = onGoogleClick) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_google),
                            contentDescription = stringResource(R.string.login_icon_desc_google),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = onBottomSignUpClick) {
                    Text(
                        text = buildAnnotatedString {

                            append(stringResource(R.string.login_link_no_account_part1))


                            withStyle(style = SpanStyle(color = AppVividBlue)) {
                                append(stringResource(R.string.login_link_no_account_part2))
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTextGrey
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginSuccess = {},
            onGuestLogin = {},
            onForgotPasswordClick = {},
            onSignUpClick = {},
            onFacebookprintClick = {},
            onGoogleClick = {},
            onBottomSignUpClick = {}
        )
    }
}