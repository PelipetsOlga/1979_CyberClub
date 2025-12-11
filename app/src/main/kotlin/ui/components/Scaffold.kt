package com.application.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.application.R
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorGreyCool

@OptIn(ExperimentalMaterial3Api::class)
val topAppBarColors = TopAppBarColors(
    containerColor = colorBackgroundMain,
    scrolledContainerColor = colorBackgroundMain,
    navigationIconContentColor = colorGreyCool,
    titleContentColor = colorGreyCool,
    actionIconContentColor = colorGreyCool,
)

@Composable
fun MenuButton(onMenuClick: ()-> Unit){
    IconButton(onClick = onMenuClick) {
        Image(
            painter = painterResource(id = R.drawable.ic_home_menu),
            contentDescription = "Menu",
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit,
        )
    }
}