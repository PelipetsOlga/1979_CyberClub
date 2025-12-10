package com.application.ui.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.apply

fun Activity.setUpEdgeToEdgeMode(navBarColor: Int? = Color.rgb(19, 23, 31)) {
    window.apply {
        // Enable edge-to-edge mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Set status bar to transparent for edge-to-edge
        statusBarColor = Color.TRANSPARENT
        
        // Set navigation bar color (will be overridden by theme if needed)
        navigationBarColor = (navBarColor ?: Color.rgb(19, 23, 31))
        
        // Use modern WindowInsetsController for appearance
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Set light icons on dark background (false = light icons for dark bg)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false
    }
}