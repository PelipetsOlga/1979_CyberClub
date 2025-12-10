package com.application.ui.feature_splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.ui.theme.colorSplashBackground
import com.application.ui.theme.colorSplashLoadingActive
import com.application.ui.theme.colorSplashLoadingTrack
import com.application.ui.theme.colorWhitePure

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    navController: NavController
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        viewModel.setEvent(SplashEvent.OnScreenShown)
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is SplashEffect.NavigateToOnboarding -> {
                    if (it.onboardingCompleted) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorSplashBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Loading...",
                color = colorWhitePure,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = colorSplashLoadingActive,
                    trackColor = colorSplashLoadingTrack,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

