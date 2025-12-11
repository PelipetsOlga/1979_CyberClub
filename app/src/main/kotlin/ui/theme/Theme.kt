package com.application.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.application.R

val Manrope = FontFamily(
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
)

val AppTextStyleNumbers = TextStyle(
    fontFamily = Manrope,
    fontSize = 24.sp,
    lineHeight = 36.sp,
    fontWeight = FontWeight.Bold
)

val AppTextStyleButton= TextStyle(
    fontFamily = Manrope,
    fontSize = 20.sp,
    lineHeight = 24.sp,
    fontWeight = FontWeight.Bold
)

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Manrope,
        fontSize = 22.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Normal
    ),
    // H2: 20/30
    headlineMedium = TextStyle(
        fontFamily = Manrope,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Normal
    ),
    // H3: 18/24
    headlineSmall = TextStyle(
        fontFamily = Manrope,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    // Body: 16/24
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    // Small: 14/18
    bodySmall = TextStyle(
        fontFamily = Manrope,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal
    ),
    // Default Material3 styles with Manrope font
    titleLarge = Typography().titleLarge.copy(fontFamily = Manrope),
    titleMedium = Typography().titleMedium.copy(fontFamily = Manrope),
    titleSmall = Typography().titleSmall.copy(fontFamily = Manrope),
    labelLarge = Typography().labelLarge.copy(fontFamily = Manrope),
    labelMedium = Typography().labelMedium.copy(fontFamily = Manrope),
    labelSmall = Typography().labelSmall.copy(fontFamily = Manrope),
    displayLarge = Typography().displayLarge.copy(fontFamily = Manrope),
    displayMedium = Typography().displayMedium.copy(fontFamily = Manrope),
    displaySmall = Typography().displaySmall.copy(fontFamily = Manrope)
)

private val DarkColors = darkColorScheme(
    primary = colorGreen,
    onPrimary = colorBlack,

    secondary = colorYellow,
    onSecondary = colorBlack,

    error = colorRed,
    onError = colorWhitePure,

    background = colorBlack,
    onBackground = colorWhitePure,

    surface = colorGreyCool,
    onSurface = colorWhitePure,

    surfaceVariant = colorGreyCool,
    onSurfaceVariant = colorWhitePure,

    outline = colorBluePrimary,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColors // Force dark color scheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Start,
) {

    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorGreen,
                style = AppTypography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = textAlign
            )
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = colorGreyCool
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorBlack
        ),
        modifier = modifier.background(colorBlack)
    )
}

