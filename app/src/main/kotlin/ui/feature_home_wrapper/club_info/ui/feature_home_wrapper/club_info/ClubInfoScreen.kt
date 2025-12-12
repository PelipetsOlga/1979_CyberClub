package com.application.ui.feature_home_wrapper.club_info.ui.feature_home_wrapper.club_info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorGreyCool
import com.application.ui.theme.colorWhitePure
import com.application.ui.utils.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubInfoScreen(
    viewModel: ClubInfoViewModel,
    navController: NavController,
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setEvent(ClubInfoEvent.OnScreenShown)
    }

    ClubInfoScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onBackToHome = onBackToHome,
        onEvent = { viewModel.setEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubInfoScreenContent(
    state: ClubInfoState,
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    onEvent: (ClubInfoEvent) -> Unit = {}
) {
    // This screen currently renders static rules content (no state needed).
    // Keep `state` / `onEvent` in signature for MVI consistency/future updates.
    ClubInfoContent(
        onMenuClick = onMenuClick,
        onBackToHome = onBackToHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubInfoContent(
    onMenuClick: () -> Unit = {},
    onBackToHome: () -> Unit = {},
    initialExpandedId: String? = null
) {
    val items = remember {
        listOf(
            ClubInfoAccordionItem(
                id = "about",
                title = "About the Club",
                bullets = listOf(
                    "Welcome to 1w Cyber Club!",
                    "Please read the rules below to ensure a comfortable and safe experience for everyone."
                )
            ),
            ClubInfoAccordionItem(
                id = "age",
                title = "Age Requirements",
                bullets = listOf(
                    "Visitors must be 14+",
                    "Guests under 16 must be accompanied by an adult"
                )
            ),
            ClubInfoAccordionItem(
                id = "behavior",
                title = "Behavior Rules",
                bullets = listOf(
                    "Be respectful to other players",
                    "No shouting, harassment, or toxic behavior",
                    "Keep your area clean",
                    "Follow staff instructions at all times"
                )
            ),
            ClubInfoAccordionItem(
                id = "equipment",
                title = "Equipment Rules",
                bullets = listOf(
                    "Do not move or unplug cables",
                    "Report any issues to the staff immediately",
                    "Do not change system settings",
                    "Damaged equipment must be compensated"
                )
            ),
            ClubInfoAccordionItem(
                id = "time",
                title = "Time & Reservations",
                bullets = listOf(
                    "Reserved seats must be used on time",
                    "Late arrival may shorten your session",
                    "Unclaimed reservations are cancelled after 10 minutes"
                )
            ),
            ClubInfoAccordionItem(
                id = "safety",
                title = "Safety",
                bullets = listOf(
                    "No food or drinks near equipment",
                    "Keep personal belongings with you",
                    "In case of emergency follow staff instructions"
                )
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
                            text = "Club Info & Rules",
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
        }
        ,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Welcome to ")
                            withStyle(SpanStyle(color = colorBluePrimary)) { append("1w") }
                            append(" Cyber Club!")
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        color = colorWhitePure,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please read the rules below to ensure a comfortable\nand safe experience for everyone.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorWhitePure.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            items(items, key = { it.id }) { item ->
                ClubInfoAccordionCard(
                    title = item.title,
                    bullets = item.bullets,
                    expanded = expandedId == item.id,
                    onToggle = {
                        expandedId = if (expandedId == item.id) null else item.id
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(84.dp))
            }
        }
    }
}

private data class ClubInfoAccordionItem(
    val id: String,
    val title: String,
    val bullets: List<String>
)

@Composable
private fun ClubInfoAccordionCard(
    title: String,
    bullets: List<String>,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "chevronRotation")
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 22.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.35f),
                spotColor = Color.Black.copy(alpha = 0.35f)
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
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = colorWhitePure,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = colorBluePrimary,
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                bullets.forEach { bullet ->
                    Text(
                        text = "\u2022  $bullet",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorGreyCool
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ClubInfoScreenContentPreview() {
    AppTheme {
        ClubInfoScreenContent(
            state = ClubInfoState(),
            onMenuClick = {},
            onBackToHome = {},
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun ClubInfoContentPreview() {
    AppTheme {
        ClubInfoContent(
            onMenuClick = {},
            onBackToHome = {},
            initialExpandedId = "time"
        )
    }
}
