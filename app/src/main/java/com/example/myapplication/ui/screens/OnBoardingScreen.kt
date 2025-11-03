package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.components.PagerIndicator
import com.example.myapplication.ui.theme.AppBackground
import com.example.myapplication.ui.theme.AppGreen
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(onNavigateToWelcome: () -> Unit) {

    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(AppGreen)) {


        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            OnboardingPage(page = page)
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < 1) {

                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {

                            onNavigateToWelcome()
                        }
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.next_button),
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTextDark
                )
            }


            Spacer(modifier = Modifier.height(16.dp))


            PagerIndicator(
                pageCount = 2,
                currentPage = pagerState.currentPage,
                activeColor = AppGreen,
                inactiveBorderColor = AppTextDark
            )
        }
    }
}

/**
 * El Composable reutilizable para el layout de UNA página de onboarding
 */
@Composable
fun OnboardingPage(page: Int) {

    val imageRes = if (page == 0) {
        R.drawable.ic_onboarding_a
    } else {
        R.drawable.ic_onboarding_b
    }

    val imageDesc = if (page == 0) {
        stringResource(R.string.onboarding_image_1_desc)
    } else {
        stringResource(R.string.onboarding_image_2_desc)
    }

    Column(modifier = Modifier.fillMaxSize()) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (page == 0) {

                    Text(
                        text = stringResource(R.string.onboarding_title_1_line_1),
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_1_line_2),
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center
                    )
                } else {

                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_1),

                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_2),

                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Text(
                        text = stringResource(R.string.onboarding_title_2_line_3),

                        style = MaterialTheme.typography.titleLarge,
                        color = AppTextDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            color = AppBackground,
            shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {


                Box(
                    contentAlignment = Alignment.Center
                ) {


                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(AppGreenLight, shape = CircleShape)
                    )


                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = imageDesc,
                        modifier = Modifier.size(250.dp)
                    )
                }

            }

        }
    }
}



@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    MyApplicationTheme {
        OnboardingScreen(onNavigateToWelcome = {})
    }
}