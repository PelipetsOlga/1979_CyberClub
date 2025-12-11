package com.application.ui.feature_home_wrapper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.navigation.HomeRoute
import com.application.ui.components.MenuButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorWhitePure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingTimeScreen(
    viewModel: GamingTimeViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is GamingTimeEffect.NavigateToCart -> {
                    navController.navigate(HomeRoute.CartInner.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    GamingTimeScreenContent(
        onMenuClick = onMenuClick,
        onCartIconClick = { viewModel.setEvent(GamingTimeEvent.OnCartIconClicked) },
        onEvent = {viewModel.handleEvent(it)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingTimeScreenContent(
    onMenuClick: () -> Unit = {},
    onCartIconClick: () -> Unit = {},
    onEvent: (GamingTimeEvent) -> Unit = {}
) {
    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = { },
                navigationIcon = {
                    MenuButton(onMenuClick)
                },
                actions = {
                    IconButton(onClick = onCartIconClick) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = "Cart",
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gaming Time Screen",
                style = MaterialTheme.typography.headlineLarge,
                color = colorWhitePure,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "PC/Console/Drinks/Snacks catalog",
                style = MaterialTheme.typography.bodyLarge,
                color = colorWhitePure,
            )
        }
    }
}

@Preview
@Composable
fun GamingTimeScreenContentPreview() {
    AppTheme {
        GamingTimeScreenContent(
            onMenuClick = {},
            onCartIconClick = {}
        )
    }
}

