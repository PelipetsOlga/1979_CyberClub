package com.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.application.navigation.RootRoute
import com.application.ui.feature_cart.CartScreen
import com.application.ui.feature_home.HomeScreen
import com.application.ui.feature_home_wrapper.HomeWrapperScreen
import com.application.ui.feature_onboarding.OnboardingScreen
import com.application.ui.feature_order_confirmation.OrderConfirmationScreen
import com.application.ui.feature_splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MaterialTheme {
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
                                viewModel = viewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(RootRoute.Onboarding.route) {
                            OnboardingScreen(
                                viewModel = viewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(RootRoute.Home.route) {
                            HomeScreen(
                                viewModel = viewModel(),
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
                                viewModel = viewModel()
                            )
                        }
                        
                        composable(RootRoute.Cart.route) {
                            CartScreen(
                                viewModel = viewModel(),
                                navController = rootNavController
                            )
                        }
                        
                        composable(RootRoute.OrderConfirmation.route) {
                            OrderConfirmationScreen(
                                viewModel = viewModel(),
                                navController = rootNavController
                            )
                        }
                    }
                }
            }
        }
    }
}