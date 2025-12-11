package com.application.ui.feature_onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.navigation.RootRoute
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure

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
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                                color = if (index == 0) colorBluePrimary else Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(50)
                            )
                    )
                    if (index != 3) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Stay Updated",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorBluePrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Follow esports match schedules and club announcements in real time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { /* Action handled by parent */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBluePrimary,
                    contentColor = colorWhitePure
                )
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun OnboardingPage2(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                                color = if (index < 3) colorBluePrimary else Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(50)
                            )
                    )
                    if (index != 3) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            // Payment terminal illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_cash_register),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            // Bottom content section
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = "Buy gaming time, drinks and snacks. Your cart syncs automatically.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Next button with arrow
                Row(
                    modifier = Modifier
                        .clickable { /* Action handled by parent */ },
                    verticalAlignment = Alignment.CenterVertically
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
                            imageVector = Icons.Default.ArrowForward,
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
        }
    }
}

@Composable
fun OnboardingPage3(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress indicator - page 3 (first 2 solid, last 2 outlined)
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
                                color = if (index < 2) colorBluePrimary else Color.Transparent,
                                shape = RoundedCornerShape(50)
                            )
                            .then(
                                if (index >= 2) {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = colorBluePrimary.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(50)
                                    )
                                } else {
                                    Modifier
                                }
                            )
                    )
                    if (index != 3) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            // Phone illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_phone),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            // Bottom content section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Title with mixed colors
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorWhitePure)) {
                            append("Book Your ")
                        }
                        withStyle(style = SpanStyle(color = colorBluePrimary)) {
                            append("Gaming Spot")
                        }
                    },
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = "Reserve PC or console seats instantly and skip waiting in line.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Next button with arrow
                Row(
                    modifier = Modifier
                        .clickable { /* Action handled by parent */ },
                    verticalAlignment = Alignment.CenterVertically
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
                            imageVector = Icons.Default.ArrowForward,
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
        }
    }
}

@Composable
fun OnboardingPage4(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress indicator - page 4 (first 3 solid, 4th outlined)
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
                                color = if (index < 3) colorBluePrimary else Color.Transparent,
                                shape = RoundedCornerShape(50)
                            )
                            .then(
                                if (index == 3) {
                                    Modifier.border(
                                        width = 1.dp,
                                        color = colorBluePrimary,
                                        shape = RoundedCornerShape(50)
                                    )
                                } else {
                                    Modifier
                                }
                            )
                    )
                    if (index != 3) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            // VR headset illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_mask),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Fit
            )

            // Bottom content section
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = "Follow esports match schedules and club announcements in real time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Get Started button (no arrow, full width)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable { /* Action handled by parent */ }
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF003D99),
                                    colorBluePrimary
                                )
                            ),
                            shape = RoundedCornerShape(32.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorWhitePure
                    )
                }
            }
        }
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
