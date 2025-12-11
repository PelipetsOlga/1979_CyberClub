package com.application.ui.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
import com.application.ui.utils.clickableNoRipple

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

    HomeScreenContent(
        onGamingTimeClicked = { viewModel.setEvent(HomeEvent.OnGamingTimeClicked) },
        onCartClicked = { viewModel.setEvent(HomeEvent.OnCartClicked) },
        onMatchScheduleClicked = { viewModel.setEvent(HomeEvent.OnMatchScheduleClicked) },
        onReserveSeatClicked = { viewModel.setEvent(HomeEvent.OnReserveSeatClicked) },
        onClubInfoClicked = { viewModel.setEvent(HomeEvent.OnClubInfoClicked) },
        onSupportClicked = { viewModel.setEvent(HomeEvent.OnSupportClicked) }
    )
}

@Composable
fun HomeScreenContent(
    onGamingTimeClicked: () -> Unit,
    onCartClicked: () -> Unit,
    onMatchScheduleClicked: () -> Unit,
    onReserveSeatClicked: () -> Unit,
    onClubInfoClicked: () -> Unit,
    onSupportClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(0))
            .background(colorBackgroundMain),
        color = colorBackgroundMain
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorBackgroundMain,
                            colorBackgroundMain.copy(alpha = 0.95f)
                        )
                    )
                )
                .navigationBarsPadding()
                .statusBarsPadding(),
            contentAlignment = Alignment.TopEnd
        ) {
            // Laptop illustration
            Image(
                painter = painterResource(id = R.mipmap.ic_laptop),
                contentDescription = null,
                modifier = Modifier
                    .rotate(-10f)
                    .size(200.dp)
                    .padding(start = 16.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp)

            ) {
                // Top section with Hello text and laptop illustration

                Column(
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = colorBluePrimary)) {
                                append("Hello!")
                            }
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "What would you like to do today?",
                        color = colorWhitePure,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))

                // Service options grid (3 rows x 2 columns)
                val maxButtonHeight = remember { mutableFloatStateOf(0f) }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Row 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        ServiceButton(
                            iconRes = R.drawable.ic_home_gaming_time,
                            title = "Gaming Time",
                            description = "purchase PC/Console gaming time",
                            onClick = onGamingTimeClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                        ServiceButton(
                            iconRes = R.drawable.ic_home_cart,
                            title = "Cart",
                            description = "go to cart",
                            onClick = onCartClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                    }

                    // Row 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        ServiceButton(
                            iconRes = R.drawable.ic_home_schedule,
                            title = "Match Schedule",
                            description = "live match broadcast schedule",
                            onClick = onMatchScheduleClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                        ServiceButton(
                            iconRes = R.drawable.ic_home_seat,
                            title = "Reserve Seat",
                            description = "book a PC/table",
                            onClick = onReserveSeatClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                    }

                    // Row 3
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        ServiceButton(
                            iconRes = R.drawable.ic_home_info,
                            title = "Club Info",
                            description = "rules and information",
                            onClick = onClubInfoClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                        ServiceButton(
                            iconRes = R.drawable.ic_home_support,
                            title = "Support",
                            description = "club contacts",
                            onClick = onSupportClicked,
                            modifier = Modifier.weight(1f),
                            maxHeight = maxButtonHeight.value,
                            onHeightMeasured = { height ->
                                if (height > maxButtonHeight.value) {
                                    maxButtonHeight.value = height
                                }
                            }
                        )
                    }
                }
                // Bottom right abstract icon (QR code-like placeholder)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home_qr),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceButton(
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxHeight: Float = 0f,
    onHeightMeasured: (Float) -> Unit = {}
) {
    val density = LocalDensity.current
    
    Box(
        modifier = modifier
            .clickableNoRipple { onClick() }
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .then(
                if (maxHeight > 0f) {
                    Modifier.height(with(density) { maxHeight.toDp() })
                } else {
                    Modifier.onGloballyPositioned { coordinates ->
                        // Measure natural height (including padding which is applied after)
                        val naturalHeight = coordinates.size.height.toFloat()
                        onHeightMeasured(naturalHeight)
                    }
                }
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colorWhitePure,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = colorWhitePure.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreenContent(
            onGamingTimeClicked = {},
            onCartClicked = {},
            onMatchScheduleClicked = {},
            onReserveSeatClicked = {},
            onClubInfoClicked = {},
            onSupportClicked = {}
        )
    }
}
