package com.example.myapplication.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int
) {
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = stringResource(R.string.logo_content_description),
        modifier = modifier
    )
}