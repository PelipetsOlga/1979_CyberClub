package com.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.application.navigation.RootRoute
import com.application.ui.feature_home.HomeScreen
import com.application.ui.feature_home_wrapper.HomeWrapperScreen
import com.application.ui.feature_onboarding.OnboardingScreen
import com.application.ui.feature_order_confirmation.OrderConfirmationScreen
import com.application.ui.feature_splash.SplashScreen
import com.application.ui.theme.AppTheme
import com.application.ui.utils.setUpEdgeToEdgeMode

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpEdgeToEdgeMode()
        
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val rootNavController = rememberNavController()
                    
                    NavHost(
                        navController = rootNavController,
                        startDestination = RootRoute.Splash.route
                    ) {
                        composable(RootRoute.Splash.route) {
                            SplashScreen(
                                viewModel = hiltViewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(RootRoute.Onboarding.route) {
                            OnboardingScreen(
                                viewModel = hiltViewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(RootRoute.Home.route) {
                            HomeScreen(
                                viewModel = hiltViewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(
                            route = RootRoute.HomeWrapper.route,
                            arguments = listOf(
                                navArgument("initialScreen") {
                                    type = NavType.StringType
                                    defaultValue = "gaming_time"
                                }
                            )
                        ) { backStackEntry ->
                            val initialScreen = backStackEntry.arguments?.getString("initialScreen") ?: "gaming_time"
                            HomeWrapperScreen(
                                initialScreen = initialScreen,
                                rootNavController = rootNavController,
                                viewModel = hiltViewModel()
                            )
                        }
                        
                        composable(RootRoute.OrderConfirmation.route) {
                            OrderConfirmationScreen(
                                viewModel = hiltViewModel(),
                                navController = rootNavController
                            )
                        }
                    }
                }
            }
        }
    }
}