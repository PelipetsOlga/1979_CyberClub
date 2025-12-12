package com.application.ui.feature_home_wrapper.support.ui.feature_home_wrapper.support

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.application.ui.components.MenuButton
import com.application.ui.components.PrimaryButton
import com.application.ui.components.topAppBarColors
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBackgroundMain
import com.application.ui.theme.colorBackgroundSurface
import com.application.ui.theme.colorGreyCool
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    viewModel: SupportViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    SupportScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onBackToHome = onBackToHome,
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreenContent(
    state: SupportState,
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    onEvent: (SupportEvent) -> Unit = {}
) {
    // This screen currently renders static support contacts (no state needed).
    // Keep `state` / `onEvent` in signature for MVI consistency/future updates.
    SupportContent(
        onMenuClick = onMenuClick,
        onBackToHome = onBackToHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    initialExpandedId: String? = "phone"
) {
    val items = remember {
        listOf(
            SupportContactItem(
                id = "phone",
                title = "Phone Support",
                primary = "+380 67 000 00 00",
                secondary = "Available: 10:00 — 23:00"
            ),
            SupportContactItem(
                id = "telegram",
                title = "Telegram",
                primary = "@CyberClubSupport",
                secondary = null
            ),
            SupportContactItem(
                id = "email",
                title = "Email",
                primary = "support@1wcyberclub.com",
                secondary = null
            )
        )
    }

    var expandedId by rememberSaveable(initialExpandedId) { mutableStateOf(initialExpandedId) }

    Scaffold(
        containerColor = colorBackgroundMain,
        topBar = {
            TopAppBar(
                colors = topAppBarColors,
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Support",
                            color = colorWhitePure,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 48.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    MenuButton(onMenuClick)
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                PrimaryButton(
                    onClick = onBackToHome,
                    text = "Back to Home",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 18.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "If you need help, feel free to contact our\nstaff using any method below.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorWhitePure.copy(alpha = 0.85f)
                )
            }

            items(items, key = { it.id }) { item ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = item.title,
                        color = colorWhitePure,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SupportContactCard(
                        primary = item.primary,
                        secondary = item.secondary,
                        onToggle = {
                            expandedId = if (expandedId == item.id) null else item.id
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "Talk to any administrator at the club counter.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorGreyCool,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(84.dp))
            }
        }
    }
}

private data class SupportContactItem(
    val id: String,
    val title: String,
    val primary: String,
    val secondary: String?
)

@Composable
private fun SupportContactCard(
    primary: String,
    secondary: String?,
    onToggle: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.30f),
                spotColor = Color.Black.copy(alpha = 0.30f)
            )
            .clickableNoRipple { onToggle() }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorBackgroundSurface.copy(alpha = 0.96f),
                        colorBackgroundSurface.copy(alpha = 0.90f)
                    )
                ),
                shape = shape
            )
            .animateContentSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = primary,
                    color = colorWhitePure,
                    style = MaterialTheme.typography.headlineLarge
                )
                if (secondary != null) {
                    Text(
                        text = secondary,
                        color = colorGreyCool,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SupportScreenContentPreview() {
    AppTheme {
        SupportScreenContent(
            state = SupportState(),
            onMenuClick = {},
            onBackToHome = {},
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun SupportContentPreview() {
    AppTheme {
        SupportContent(
            onMenuClick = {},
            onBackToHome = {},
            initialExpandedId = "phone"
        )
    }
}
