package com.application.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.application.ui.theme.AppTextStyleButton
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@Composable
fun PrimaryButton(onClick: ()-> Unit, text: String, modifier :Modifier) {
    Box(
        modifier = modifier
            .clickableNoRipple { onClick() }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF003D99),
                        colorBluePrimary
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = AppTextStyleButton,
            color = colorWhitePure,
            maxLines = 1
        )
    }
}