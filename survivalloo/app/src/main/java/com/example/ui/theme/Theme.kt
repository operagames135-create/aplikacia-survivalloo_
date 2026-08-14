package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SleekColorScheme = darkColorScheme(
    primary = SleekPurple,
    onPrimary = Color(0xFF120024),
    primaryContainer = SleekPurpleDark,
    onPrimaryContainer = SleekPurple,
    secondary = SleekLime,
    onSecondary = Color(0xFF072100),
    secondaryContainer = SleekLimeDark,
    onSecondaryContainer = SleekLime,
    tertiary = SleekCyan,
    background = SleekBg,
    onBackground = TextPrimary,
    surface = SleekCard,
    onSurface = TextPrimary,
    surfaceVariant = SleekCardSecondary,
    onSurfaceVariant = TextSecondary,
    outline = SleekBorder,
    outlineVariant = SleekBorderSubtle,
    error = SleekRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SleekColorScheme,
        typography = Typography,
        content = content
    )
}
