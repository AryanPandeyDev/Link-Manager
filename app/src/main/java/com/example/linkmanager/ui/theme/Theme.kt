package com.example.linkmanager.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightPrimary.copy(alpha = 0.1f),
    onPrimaryContainer = LightPrimary,

    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = LightSecondary.copy(alpha = 0.1f),
    onSecondaryContainer = LightSecondary,

    tertiary = LightTertiary,
    onTertiary = Color.White,
    tertiaryContainer = LightTertiary.copy(alpha = 0.1f),
    onTertiaryContainer = LightTertiary,

    background = LightBackground,
    onBackground = Color(0xFF212529),

    surface = LightSurface,
    onSurface = Color(0xFF212529),

    surfaceVariant = Color(0xFFE9ECEF),
    onSurfaceVariant = Color(0xFF495057),

    error = ErrorRed,
    onError = Color.White,

    outline = Color(0xFFDEE2E6)
)


val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = DarkPrimary.copy(alpha = 0.1f),
    onPrimaryContainer = DarkPrimary,

    secondary = DarkSecondary,
    onSecondary = Color.Black,
    secondaryContainer = DarkSecondary.copy(alpha = 0.1f),
    onSecondaryContainer = DarkSecondary,

    tertiary = DarkTertiary,
    onTertiary = Color.Black,
    tertiaryContainer = DarkTertiary.copy(alpha = 0.1f),
    onTertiaryContainer = DarkTertiary,

    background = DarkBackground,
    onBackground = Color(0xFFE9ECEF),

    surface = DarkSurface,
    onSurface = Color(0xFFE9ECEF),

    surfaceVariant = Color(0xFF2D3338),
    onSurfaceVariant = Color(0xFFADB5BD),

    error = ErrorRed,
    onError = Color.Black,

    outline = Color(0xFF495057)
)

@Composable
fun LinkManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

