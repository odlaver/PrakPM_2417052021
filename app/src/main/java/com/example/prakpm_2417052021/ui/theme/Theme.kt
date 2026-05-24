package com.example.prakpm_2417052021.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DefaultColorScheme = lightColorScheme(
    primary = CustomPrimary,
    onPrimary = Color.White,
    primaryContainer = CustomPrimaryContainer,
    onPrimaryContainer = CustomOnSurface,
    secondary = CustomSecondary,
    onSecondary = CustomOnSurface,
    secondaryContainer = CustomSecondaryContainer,
    onSecondaryContainer = CustomOnSurface,
    tertiary = CustomTertiary,
    onTertiary = Color.White,
    tertiaryContainer = CustomTertiaryContainer,
    onTertiaryContainer = CustomOnSurface,
    background = CustomBackground,
    onBackground = CustomOnSurface,
    surface = CustomSurface,
    onSurface = CustomOnSurface,
    surfaceVariant = CustomSurfaceVariant,
    onSurfaceVariant = CustomOnSurfaceVariant,
    outline = CustomOutline,
    outlineVariant = CustomOutlineVariant,
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
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
