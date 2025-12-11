package com.application.ui.feature_onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.navigation.RootRoute
import com.application.ui.components.PrimaryButton
import com.application.ui.theme.AppTextStyleNumbers
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBackgroundSurface
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    navController: NavController
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)
    val pagerState = rememberPagerState(pageCount = { state.totalPages })

    LaunchedEffect(pagerState.currentPage, state.currentPage) {
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
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPage(
                    pageNumber = page,
                    onEvent = { viewModel.handleEvent(it) }
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(
    pageNumber: Int,
    onEvent: (OnboardingEvent) -> Unit
) {
    when (pageNumber) {
        0 -> OnboardingPage1(onEvent)
        1 -> OnboardingPage2(onEvent)
        2 -> OnboardingPage3(onEvent)
        3 -> OnboardingPage4(onEvent)
    }
}

@Composable
fun OnboardingPage1(
    onEvent: (OnboardingEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)

    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_welcome),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colorBackgroundMain
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(0)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorBluePrimary)) {
                            append("Welcome to \n")
                        }
                        withStyle(style = SpanStyle(color = colorWhitePure)) {
                            append("1w Cyber Club")
                        }
                    },
                    style = AppTextStyleNumbers
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Description
                Text(
                    text = "Your gateway to high-end gaming, esports streaming and fast in-club services.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorWhitePure
                )

                Spacer(modifier = Modifier.height(32.dp))

                NextButtonWithArrow(onClick = { onEvent(OnboardingEvent.NextClicked) })
            }
        }
    }
}

@Composable
fun OnboardingPage3(
    onEvent: (OnboardingEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(2)

            // Payment terminal illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_cash_register),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            // Bottom content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Title with mixed colors
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorBluePrimary)) {
                            append("Order & Pay ")
                        }
                        withStyle(style = SpanStyle(color = colorWhitePure)) {
                            append("Faster")
                        }
                    },
                    style = AppTextStyleNumbers
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Buy gaming time, drinks and snacks. Your cart syncs automatically.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorWhitePure
                )

                Spacer(modifier = Modifier.height(32.dp))

                NextButtonWithArrow(onClick = {onEvent(OnboardingEvent.NextClicked) })
            }
        }
    }
}

@Composable
fun NextButtonWithArrow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colorBluePrimary,
                            colorBluePrimary.copy(alpha = 0.7f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                tint = colorWhitePure,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Next",
            style = MaterialTheme.typography.titleMedium,
            color = colorWhitePure
        )
    }
}

@Composable
private fun ProgressIndicator(pageNumber: Int) {
    // Progress indicator - page 2 (first 3 solid, 4th inactive)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .background(
                        color = if (index <= pageNumber) colorBluePrimary else Color.White.copy(
                            alpha = 0.25f
                        ),
                        shape = RoundedCornerShape(50)
                    )
            )
            if (index != 3) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun OnboardingPage2(
    onEvent: (OnboardingEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
            .navigationBarsPadding()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(1)

            // Phone illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_phone),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
                    .weight(1f),
                contentScale = ContentScale.Fit
            )
        }

        // Bottom content section
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Title with mixed colors
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorBackgroundSurface)
                    .padding(64.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorWhitePure)) {
                            append("Book Your ")
                        }
                        withStyle(style = SpanStyle(color = colorBluePrimary)) {
                            append("Gaming Spot")
                        }
                    },
                    style = AppTextStyleNumbers
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Reserve PC or console seats instantly and skip waiting in line.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorWhitePure
                )

                Spacer(modifier = Modifier.height(32.dp))

                NextButtonWithArrow(onClick = {onEvent(OnboardingEvent.NextClicked) })
            }
        }
    }
}

@Composable
fun OnboardingPage4(
    onEvent: (OnboardingEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
    ) {
        // VR headset illustration
        Image(
            painter = painterResource(id = R.mipmap.ic_mask),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(3)

            // Bottom content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Title with mixed colors
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF4D8AFF))) {
                            append("Stay ")
                        }
                        withStyle(style = SpanStyle(color = colorBluePrimary)) {
                            append("Updated")
                        }
                    },
                    style = AppTextStyleNumbers
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Follow esports match schedules and club announcements in real time.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorWhitePure
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Get Started button (no arrow, full width)
                PrimaryButton(
                    text = "Get Started", onClick = {onEvent(OnboardingEvent.OnGetStartedClicked) }, modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun OnboardingPage1Preview() {
    AppTheme {
        OnboardingPage1({})
    }
}

@Preview
@Composable
fun OnboardingPage2Preview() {
    AppTheme {
        OnboardingPage2({})
    }
}

@Preview
@Composable
fun OnboardingPage3Preview() {
    AppTheme {
        OnboardingPage3({})
    }
}

@Preview
@Composable
fun OnboardingPage4Preview() {
    AppTheme {
        OnboardingPage4({})
    }
}
