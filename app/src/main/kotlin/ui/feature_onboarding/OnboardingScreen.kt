package com.application.ui.feature_onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)
    val pagerState = rememberPagerState(pageCount = { state.totalPages })

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setEvent(OnboardingEvent.OnPageChanged(pagerState.currentPage))
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is OnboardingEffect.NavigateToHome -> {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(
                    pageNumber = page + 1,
                    totalPages = state.totalPages
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.currentPage < state.totalPages - 1) {
                    TextButton(
                        onClick = { /* Skip onboarding */ }
                    ) {
                        Text("Skip")
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                if (state.currentPage == state.totalPages - 1) {
                    Button(
                        onClick = { viewModel.setEvent(OnboardingEvent.OnGetStartedClicked) }
                    ) {
                        Text("Get Started")
                    }
                } else {
                    TextButton(
                        onClick = { /* Next page handled by swipe */ }
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPage(
    pageNumber: Int,
    totalPages: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Onboarding Page $pageNumber",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Content for page $pageNumber of $totalPages",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

