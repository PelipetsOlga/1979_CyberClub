package com.application.ui.feature_home_wrapper

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.navigation.HomeRoute
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
            popUpTo(HomeRoute.GamingTime.route) { inclusive = true }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                DrawerHeader()
                DrawerBody(
                    onItemClick = { route ->
                        homeNavController.navigate(route) {
                            launchSingleTop = true
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = homeNavController,
            startDestination = initialScreen
        ) {
            composable(HomeRoute.GamingTime.route) {
                GamingTimeScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.CartInner.route) {
                CartInnerScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
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
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            composable(HomeRoute.Support.route) {
                SupportScreen(
                    viewModel = viewModel(),
                    navController = homeNavController,
                    onMenuClick = { scope.launch { drawerState.open() } }
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
            .padding(16.dp)
    ) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun DrawerBody(
    onItemClick: (String) -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
        label = { Text("Gaming Time") },
        selected = false,
        onClick = { onItemClick(HomeRoute.GamingTime.route) }
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
        label = { Text("Cart") },
        selected = false,
        onClick = { onItemClick(HomeRoute.CartInner.route) }
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
        label = { Text("Match Schedule") },
        selected = false,
        onClick = { onItemClick(HomeRoute.MatchSchedule.route) }
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Place, contentDescription = null) },
        label = { Text("Reserve Seat") },
        selected = false,
        onClick = { onItemClick(HomeRoute.ReserveSeat.route) }
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Info, contentDescription = null) },
        label = { Text("Club Info") },
        selected = false,
        onClick = { onItemClick(HomeRoute.ClubInfo.route) }
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
        label = { Text("Support") },
        selected = false,
        onClick = { onItemClick(HomeRoute.Support.route) }
    )
}

