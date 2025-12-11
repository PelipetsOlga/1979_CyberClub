package com.application.ui.feature_home_wrapper

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.ui.components.MenuButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
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

    MatchScheduleScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScheduleScreenContent(
    state: MatchScheduleState,
    onMenuClick: () -> Unit = {},
    onEvent: (MatchScheduleEvent) -> Unit = {}
) {
    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = { 
                    Text(
                        text = "Match Schedule",
                        color = colorWhitePure
                    )
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Match Schedule Screen",
                style = MaterialTheme.typography.headlineLarge,
                color = colorWhitePure
            )
        }
    }
}

@Preview
@Composable
fun MatchScheduleScreenContentPreview() {
    AppTheme {
        MatchScheduleScreenContent(
            state = MatchScheduleState(),
            onMenuClick = {},
            onEvent = {}
        )
    }
}
