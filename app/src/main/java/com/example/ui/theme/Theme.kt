package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreen80,
    onPrimary = Color(0xFF00390B),
    primaryContainer = ForestGreen40,
    onPrimaryContainer = Color(0xFFA6F5A7),
    secondary = HarvestGold80,
    onSecondary = Color(0xFF432C00),
    secondaryContainer = Color(0xFF5F4100),
    onSecondaryContainer = Color(0xFFFFDEA1),
    tertiary = SproutGreen80,
    background = AgroDarkBackground,
    surface = AgroDarkSurface,
    surfaceVariant = AgroDarkSurfaceVariant,
    onBackground = Color(0xFFE2E4DE),
    onSurface = Color(0xFFE2E4DE)
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF002105),
    secondary = HarvestGold40,
    onSecondary = Color.White,
    secondaryContainer = HarvestGoldContainer,
    onSecondaryContainer = Color(0xFF4E2600),
    tertiary = SproutGreen40,
    background = AgroBackgroundLight,
    surface = AgroSurfaceLight,
    surfaceVariant = Color(0xFFE5ECE3),
    onBackground = Color(0xFF191D18),
    onSurface = Color(0xFF191D18)
)

@Composable
fun AgroPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve branded agricultural palette
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

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AgroPulseTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
