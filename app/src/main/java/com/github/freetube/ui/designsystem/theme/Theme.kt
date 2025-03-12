package com.github.freetube.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.freetube.core.model.AppTheme

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF111111)
)

private val LightColorScheme = lightColorScheme()

@Composable
fun FreeTubeTheme(
    theme: AppTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = when(theme) {
        AppTheme.Light -> LightColorScheme
        AppTheme.Dark -> DarkColorScheme
        AppTheme.System -> if(isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
