package com.application.ui.feature_home_wrapper.cart.ui.feature_home_wrapper.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.ui.components.MenuButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartInnerScreen(
    viewModel: CartInnerViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is CartInnerEffect.NavigateToOrderConfirmation -> {
                    // Navigate to external OrderConfirmation screen
                    // This would require passing rootNavController
                }
            }
        }
    }

    CartInnerScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onBackClick = { navController.popBackStack() },
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartInnerScreenContent(
    state: CartInnerState,
    onMenuClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onEvent: (CartInnerEvent) -> Unit = {}
) {
    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = { 
                    Text(
                        text = "Your Cart",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                },
                actions = {
                    TextButton(onClick = onBackClick) {
                        Text(
                            text = "Back",
                            color = colorRed
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
                text = "Cart Inner Screen",
                style = MaterialTheme.typography.headlineLarge,
                color = colorWhitePure
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (state.items.isEmpty()) {
                Text(
                    text = "Cart is empty",
                    color = colorWhitePure
                )
            } else {
                state.items.forEach { item ->
                    Text(
                        text = item,
                        color = colorWhitePure
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(CartInnerEvent.OnCheckoutClicked) }
            ) {
                Text("Checkout")
            }
        }
    }
}

@Preview
@Composable
fun CartInnerScreenContentPreview() {
    AppTheme {
        CartInnerScreenContent(
            state = CartInnerState(),
            onMenuClick = {},
            onBackClick = {},
            onEvent = {}
        )
    }
}
