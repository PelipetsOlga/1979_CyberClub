package com.application.ui.feature_home_wrapper.live_club

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.domain.model.ZoneStatus
import com.application.navigation.HomeRoute
import com.application.ui.components.MenuButton
import com.application.ui.components.PrimaryButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorGreen
import com.application.ui.theme.colorRed
import com.application.ui.theme.colorWhitePure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveClubScreen(
    viewModel: LiveClubViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(Unit) {
        viewModel.setEvent(LiveClubEvent.OnScreenShown)
    }

    LaunchedEffect(effect) {
        effect?.let {
            when (it) {
                is LiveClubEffect.NavigateToReserveSeat -> {
                    navController.navigate(HomeRoute.ReserveSeat.route) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = {
                    Text(
                        text = "Live Club Status",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                }
            )
        }
    ) { paddingValues ->
        LiveClubScreenContent(
            state = state,
            onReserveNow = { viewModel.setEvent(LiveClubEvent.OnReserveNowClicked) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
fun LiveClubScreenContent(
    state: LiveClubState,
    onReserveNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clubStatus = state.clubStatus ?: getDefaultStatus()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // PC Availability Section
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "PC Availability",
                style = MaterialTheme.typography.titleLarge,
                color = colorWhitePure
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Free card
                AvailabilityCard(
                    label = "Free:",
                    value = clubStatus.pcFree.toString(),
                    labelColor = colorGreen,
                    modifier = Modifier.weight(1f)
                )
                // Busy card
                AvailabilityCard(
                    label = "Busy:",
                    value = clubStatus.pcBusy.toString(),
                    labelColor = colorRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Console Zones Section
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Console Zones",
                style = MaterialTheme.typography.titleLarge,
                color = colorWhitePure
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ZoneCard(
                    zoneName = "Zone A",
                    status = clubStatus.zoneAStatus,
                    modifier = Modifier.weight(1f)
                )
                ZoneCard(
                    zoneName = "Zone B",
                    status = clubStatus.zoneBStatus,
                    modifier = Modifier.weight(1f)
                )
                ZoneCard(
                    zoneName = "Zone C",
                    status = clubStatus.zoneCStatus,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Estimated Wait Time Section
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Estimated Wait Time",
                style = MaterialTheme.typography.titleLarge,
                color = colorWhitePure
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorBackgroundMain.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Next free PC in approx. ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${clubStatus.estimatedWaitTimeMinutes} min")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Reserve Now Button
        PrimaryButton(
            onClick = onReserveNow,
            text = "Reserve Now",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun AvailabilityCard(
    label: String,
    value: String,
    labelColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = colorBackgroundMain.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = labelColor
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = colorWhitePure,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ZoneCard(
    zoneName: String,
    status: ZoneStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                color = colorBackgroundMain.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = zoneName,
                style = MaterialTheme.typography.titleMedium,
                color = colorWhitePure
            )
            Text(
                text = if (status == ZoneStatus.FREE) "Free" else "Busy",
                style = MaterialTheme.typography.bodyLarge,
                color = if (status == ZoneStatus.FREE) colorGreen else colorRed,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun getDefaultStatus(): com.application.domain.model.ClubStatus {
    return com.application.domain.model.ClubStatus(
        id = "club_status",
        pcFree = 12,
        pcBusy = 8,
        zoneAStatus = ZoneStatus.FREE,
        zoneBStatus = ZoneStatus.BUSY,
        zoneCStatus = ZoneStatus.BUSY,
        estimatedWaitTimeMinutes = 12,
        lastUpdated = System.currentTimeMillis()
    )
}

