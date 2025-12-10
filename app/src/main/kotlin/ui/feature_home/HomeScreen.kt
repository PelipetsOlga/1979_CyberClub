package com.application.ui.feature_home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.navigation.RootRoute

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is HomeEffect.NavigateToHomeWrapper -> {
                    navController.navigate(RootRoute.HomeWrapper.createRoute(it.screen))
                }
                is HomeEffect.NavigateToCart -> {
                    navController.navigate(RootRoute.Cart.route)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "HomeScreen",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Hello! What would you like to do today?",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnGamingTimeClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gaming Time")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnCartClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cart")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnMatchScheduleClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Match Schedule")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnReserveSeatClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reserve Seat")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnClubInfoClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Club Info")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.setEvent(HomeEvent.OnSupportClicked) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Support")
            }
        }
    }
}

