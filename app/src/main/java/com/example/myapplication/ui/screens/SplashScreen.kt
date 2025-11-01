package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.components.AppLogo
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppTextWhite
import kotlinx.coroutines.delay


@Composable
fun SplashScreenContent(onSplashFinished: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(3000) // Espera 3 segundos
        onSplashFinished() // Llama al lambda que le pasamos
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppGreen
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppLogo(modifier = Modifier.size(120.dp) , drawableId = R.drawable.logo_a)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.finwise_app_name),
                color = AppTextWhite,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    MaterialTheme {
        SplashScreenContent({})
    }
}