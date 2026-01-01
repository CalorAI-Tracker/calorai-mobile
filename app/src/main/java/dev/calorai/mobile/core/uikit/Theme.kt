package dev.calorai.mobile.core.uikit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Pink,
    onPrimary = Black,
    secondary = Red,
    onSecondary = White,
    surface = White,
    onSurface = Black,
    background = WhitePink,
    onBackground = Grey,
    onSurfaceVariant = Red,
    surfaceContainerHigh = White,
)

private val LightColorScheme = lightColorScheme(
    primary = Pink,
    onPrimary = Black,
    secondary = Red,
    onSecondary = White,
    surface = White,
    onSurface = Black,
    background = WhitePink,
    onBackground = Grey,
    onSurfaceVariant = Red,
    surfaceContainerHigh = White,
)


@Composable
fun CalorAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}