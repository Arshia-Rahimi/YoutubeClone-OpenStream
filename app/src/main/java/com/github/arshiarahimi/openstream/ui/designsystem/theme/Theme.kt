package com.github.arshiarahimi.openstream.ui.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF111111),
    surface = Color(0xFF111111),
    surfaceContainer = Color(0xFF111111),
    primary = Color(0xFFCC2849),
    secondary = Color(0xFF2A2A2A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    primaryContainer = Color(0xFFCC2849),
    secondaryContainer = Color(0xFF2A2A2A),
    tertiary = Color(0xFFAAAAAA),
    onTertiary = Color(0xFFAAAAAA),
    tertiaryContainer = Color(0xFF272727),
    onBackground = Color(0xFFA9A9A9),
)

@Composable
fun OpenStreamTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
