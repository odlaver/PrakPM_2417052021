package com.example.prakpm_2417052021.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DefaultColorScheme = lightColorScheme(
    primary = CustomPrimary,
    secondary = CustomSecondary,
    tertiary = CustomTertiary,
    background = CustomBackground,
    surface = CustomBackground,
    surfaceVariant = CustomTertiary
)

@Composable
fun PrakPM_2417052021Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content
    )
}