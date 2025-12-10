package com.application.ui.feature_onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.navigation.RootRoute
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain

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
                    navController.navigate(RootRoute.Home.route) {
                        popUpTo(RootRoute.Splash.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain),
        color = colorBackgroundMain
    ) {
        Box (
            modifier = Modifier.fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPage(
                    pageNumber = page + 1,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
) {
    when (pageNumber) {
        1 -> OnboardingPage1()
        2 -> OnboardingPage2()
        3 -> OnboardingPage3()
        4 -> OnboardingPage4()
    }
}

@Composable
fun OnboardingPage1(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Onboarding Page 1",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Content for page 1 of 4",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OnboardingPage2(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Onboarding Page 2",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Content for page 2 of 4",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OnboardingPage3(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Onboarding Page3",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Content for page 3 of 4",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OnboardingPage4(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Onboarding Page 4",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Content for page 4 of 4",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun OnboardingPage1Preview() {
    AppTheme {
        OnboardingPage1()
    }
}

@Preview
@Composable
fun OnboardingPage2Preview() {
    AppTheme {
        OnboardingPage2()
    }
}

@Preview
@Composable
fun OnboardingPage3Preview() {
    AppTheme {
        OnboardingPage3()
    }
}

@Preview
@Composable
fun OnboardingPage4Preview() {
    AppTheme {
        OnboardingPage4()
    }
}
