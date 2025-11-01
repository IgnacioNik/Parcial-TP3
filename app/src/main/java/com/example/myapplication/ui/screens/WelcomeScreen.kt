package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.components.AppLogo
import com.example.myapplication.ui.components.PrimaryButton
import com.example.myapplication.ui.components.SecondaryButton
import com.example.myapplication.ui.components.TextLinkButton
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextGrey

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
    // ... otros lambdas como onForgotPasswordClick
) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            AppLogo(modifier = Modifier.size(120.dp) , drawableId = R.drawable.logo_b)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.finwise_app_name),
                color = AppGreen,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.welcome_tagline),
                color = AppTextGrey,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(48.dp))

            PrimaryButton(
                text = stringResource(R.string.log_in_button),
                modifier = Modifier.padding(horizontal = 40.dp),
                onClick = onLoginClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(
                text = stringResource(R.string.sign_up_button),
                modifier = Modifier.padding(horizontal = 40.dp),
                onClick = onRegisterClick
            )

            Spacer(modifier = Modifier.height(2.dp))

            TextLinkButton(
                text = stringResource(R.string.forgot_password_button),
                onClick = {
                    // Acción para "Olvidé mi contraseña"
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MaterialTheme {
        WelcomeScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}