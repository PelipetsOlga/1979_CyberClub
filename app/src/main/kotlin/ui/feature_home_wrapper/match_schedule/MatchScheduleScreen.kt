package com.application.ui.feature_home_wrapper.match_schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.R
import com.application.domain.model.Match
import com.application.ui.components.MenuButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorWhitePure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScheduleScreen(
    viewModel: MatchScheduleViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setEvent(MatchScheduleEvent.OnScreenShown)
    }

    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = {
                    Text(
                        text = "Esports Schedule",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                }
            )
        }
    ) { paddingValues ->
        MatchScheduleContent(
            state = state,
            onRetry = { viewModel.setEvent(MatchScheduleEvent.OnRetry) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
fun MatchScheduleContent(
    state: MatchScheduleState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.matches.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No matches scheduled",
                color = colorWhitePure.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(state.matches) { match ->
                MatchCard(match = match)
            }
        }
    }
}

@Composable
fun MatchCard(match: Match) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colorBluePrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(colorBackgroundMain)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Game and Stage (top-left) and Date & Time (top-right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${match.matchTitle} - ${match.stage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorBluePrimary.copy(alpha = 0.9f)
                )
                Text(
                    text = match.dateTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorWhitePure
                )
            }

            // Teams: Team A vs Team B
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team A
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Team A Logo
                    if (match.teamA.logoResId != null) {
                        Image(
                            painter = painterResource(id = match.teamA.logoResId),
                            contentDescription = match.teamA.name,
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        // Placeholder if no logo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = colorBluePrimary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = match.teamA.name.take(2).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorWhitePure
                            )
                        }
                    }
                    Text(
                        text = match.teamA.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorWhitePure
                    )
                }

                // VS
                Text(
                    text = "vs",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Team B
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = match.teamB.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorWhitePure
                    )
                    // Team B Logo
                    if (match.teamB.logoResId != null) {
                        Image(
                            painter = painterResource(id = match.teamB.logoResId),
                            contentDescription = match.teamB.name,
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        // Placeholder if no logo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = colorBluePrimary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = match.teamB.name.take(2).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorWhitePure
                            )
                        }
                    }
                }
            }

            // Tournament Name
            Text(
                text = match.tournamentName,
                style = MaterialTheme.typography.bodyMedium,
                color = colorWhitePure
            )
        }
    }
}

