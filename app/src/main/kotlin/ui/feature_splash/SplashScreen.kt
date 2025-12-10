package com.application.ui.feature_splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.navigation.RootRoute
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        viewModel.checkFirstLaunch()
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is SplashEffect.NavigateToOnboarding -> {
                    navController.navigate(RootRoute.Onboarding.route) {
                        popUpTo(RootRoute.Splash.route) { inclusive = true }
                    }
                }
                is SplashEffect.NavigateToHome -> {
                    navController.navigate(RootRoute.Home.route) {
                        popUpTo(RootRoute.Splash.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.headlineSmall, // H3: 18/24
            color = colorWhitePure
        )
        CircularLoadingAnimation()
    }
}



@Composable
fun CircularLoadingAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier.size(120.dp) // Increased size from 80dp to 120dp
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 * 3 / 4// Adjusted radius for larger size
        val segmentAngle = 360f / 12

        rotate(rotation, center) {
            for (i in 0 until 12) {
                val angle = i * segmentAngle
                val startAngle = angle - segmentAngle / 2

                val intensity = (i + 1) / 12.toFloat()

                val endColor = colorBluePrimary.copy(alpha = intensity)

                // Draw segment
                drawArc(
                    color = endColor,
                    startAngle = startAngle,
                    sweepAngle = segmentAngle * 0.75f, // Slightly smaller segments for better spacing
                    useCenter = true,
                    topLeft = Offset(
                        center.x - radius,
                        center.y - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }

        drawCircle(
            color = colorBackgroundMain,
            radius = radius / 6 * 5
        )
    }
}
