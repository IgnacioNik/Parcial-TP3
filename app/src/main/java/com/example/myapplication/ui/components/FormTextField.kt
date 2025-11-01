package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AppGreenLight
import com.example.myapplication.ui.theme.AppTextDark
import com.example.myapplication.ui.theme.AppTextGrey

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(AppGreenLight, shape = RoundedCornerShape(50.dp)),
        singleLine = true,
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            fontSize = 12.sp,
            color = AppTextDark
        ),
        decorationBox = { innerTextField ->

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            color = AppTextGrey.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                    innerTextField()
                }


                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
    )
}