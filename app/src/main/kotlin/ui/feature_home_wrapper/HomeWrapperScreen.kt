package com.application.ui.feature_home_wrapper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.R
import com.application.navigation.HomeRoute
import com.application.navigation.RootRoute
import com.application.ui.feature_home_wrapper.cart.ui.feature_home_wrapper.cart.CartInnerScreen
import com.application.ui.feature_home_wrapper.club_info.ui.feature_home_wrapper.club_info.ClubInfoScreen
import com.application.ui.feature_home_wrapper.gaming_time.ui.feature_home_wrapper.gaming_time.GamingTimeScreen
import com.application.ui.feature_home_wrapper.reserve_seat.ui.feature_home_wrapper.reserve_seat.ReserveSeatScreen
import com.application.ui.feature_home_wrapper.schedule.ui.feature_home_wrapper.schedule.MatchScheduleScreen
import com.application.ui.feature_home_wrapper.support.ui.feature_home_wrapper.support.SupportScreen
import com.application.ui.theme.AppTheme
import com.application.ui.theme.colorBluePrimary
import com.application.ui.theme.colorSteelBlue
import com.application.ui.theme.colorWhitePure
import kotlinx.coroutines.launch

@Composable
fun HomeWrapperScreen(
    initialScreen: String = HomeRoute.GamingTime.route,
    rootNavController: NavController,
    viewModel: HomeWrapperViewModel = viewModel()
) {
    val homeNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Navigate to initial screen
    LaunchedEffect(initialScreen) {
        homeNavController.navigate(initialScreen) {
            // Ensure a single inner destination on start
            popUpTo(homeNavController.graph.startDestinationRoute ?: initialScreen) { inclusive = true }
            launchSingleTop = true
        }
    }

    // Track current route for drawer selection - observe NavController's current destination
    var currentRoute by remember {
        mutableStateOf(homeNavController.currentDestination?.route ?: initialScreen)
    }

    // Update current route when navigation changes
    LaunchedEffect(homeNavController.currentDestination) {
        homeNavController.currentDestination?.route?.let { route ->
            currentRoute = route
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheetContent(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    if (route == "home") {
                        scope.launch {
                            drawerState.close()
                            // Close HomeWrapperScreen and return to HomeScreen
                            val poppedToHome = rootNavController.popBackStack(RootRoute.Home.route, false)
                            if (!poppedToHome) {
                                rootNavController.navigate(RootRoute.Home.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    } else {
                        // Update route immediately when clicked
                        currentRoute = route
                        scope.launch {
                            homeNavController.navigate(route) {
                                // Keep only ONE inner destination in the stack
                                popUpTo(homeNavController.graph.startDestinationRoute ?: initialScreen) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            // Ensure route is updated after navigation
                            homeNavController.currentDestination?.route?.let {
                                currentRoute = it
                            }
                            drawerState.close()
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = homeNavController,
            startDestination = initialScreen
        ) {
            composable(HomeRoute.GamingTime.route) {
                GamingTimeScreen(
                    viewModel = hiltViewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.CartInner.route) {
                CartInnerScreen(
                    viewModel = hiltViewModel(),
                    navController = homeNavController,
                    rootNavController = rootNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.MatchSchedule.route) {
                MatchScheduleScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.ReserveSeat.route) {
                ReserveSeatScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.ClubInfo.route) {
                ClubInfoScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackToHome = {
                        // Close HomeWrapperScreen and return to HomeScreen
                        val poppedToHome = rootNavController.popBackStack(RootRoute.Home.route, false)
                        if (!poppedToHome) {
                            rootNavController.navigate(RootRoute.Home.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
            composable(HomeRoute.Support.route) {
                SupportScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onBackToHome = {
                        // Close HomeWrapperScreen and return to HomeScreen
                        val poppedToHome = rootNavController.popBackStack(RootRoute.Home.route, false)
                        if (!poppedToHome) {
                            rootNavController.navigate(RootRoute.Home.route) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .width(32.dp)
                .height(1.dp)
                .background(colorWhitePure)
        )
    }
}

@Composable
fun DrawerBody(
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Home (close wrapper -> HomeScreen)
        DrawerIconButton(
            iconRes = R.drawable.ic_home,
            route = "home",
            currentRoute = currentRoute,
            onClick = { onItemClick("home") }
        )

        // Gaming Time
        DrawerIconButton(
            iconRes = R.drawable.ic_home_gaming_time,
            route = HomeRoute.GamingTime.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.GamingTime.route) }
        )

        // Cart
        DrawerIconButton(
            iconRes = R.drawable.ic_home_cart,
            route = HomeRoute.CartInner.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.CartInner.route) }
        )

        // Schedule (Match Schedule)
        DrawerIconButton(
            iconRes = R.drawable.ic_home_schedule,
            route = HomeRoute.MatchSchedule.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.MatchSchedule.route) }
        )

        // Seat (Reserve Seat)
        DrawerIconButton(
            iconRes = R.drawable.ic_home_seat,
            route = HomeRoute.ReserveSeat.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.ReserveSeat.route) }
        )

        // Info (Club Info)
        DrawerIconButton(
            iconRes = R.drawable.ic_home_info,
            route = HomeRoute.ClubInfo.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.ClubInfo.route) }
        )

        // Support
        DrawerIconButton(
            iconRes = R.drawable.ic_home_support,
            route = HomeRoute.Support.route,
            currentRoute = currentRoute,
            onClick = { onItemClick(HomeRoute.Support.route) }
        )

        // Live
        DrawerIconButton(
            iconRes = R.drawable.ic_live,
            route = "", // No route for Live, can be customized
            currentRoute = currentRoute,
            onClick = { /* Handle Live click */ }
        )
    }
}

@Composable
private fun DrawerIconButton(
    iconRes: Int,
    route: String,
    currentRoute: String,
    onClick: () -> Unit
) {
    val isSelected = route == currentRoute && route.isNotEmpty()

    Box(
        modifier = Modifier
            .size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = colorWhitePure,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(colorBluePrimary)
                    )
                }
            }
        } else {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(56.dp)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(colorWhitePure.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
fun ModalDrawerSheetContent(
    currentRoute: String = HomeRoute.GamingTime.route,
    onItemClick: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(96.dp),
        drawerContainerColor = colorSteelBlue.copy(alpha = 0.4f)
    ) {
        DrawerHeader()
        DrawerBody(
            currentRoute = currentRoute,
            onItemClick = onItemClick
        )
    }
}

@Preview
@Composable
fun ModalDrawerSheetContentPreview() {
    AppTheme {
        ModalDrawerSheetContent(
            currentRoute = HomeRoute.GamingTime.route,
            onItemClick = {}
        )
    }
}

