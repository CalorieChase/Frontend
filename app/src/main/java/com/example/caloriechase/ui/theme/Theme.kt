package com.example.caloriechase.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CalorieChaseColorScheme = darkColorScheme(
    primary = NeonOrange,
    onPrimary = TextDark,
    secondary = NeonGreen,
    onSecondary = TextDark,
    tertiary = NeonPurple,
    onTertiary = TextDark,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceOutline
)

@Composable
fun CalorieChaseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CalorieChaseColorScheme,
        typography = CalorieChaseTypography,
        content = content
    )
}
