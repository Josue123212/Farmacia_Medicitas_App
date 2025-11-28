package com.example.farmacia_medicitas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Aplicamos tu paleta (azules/grises) en Material 3
private val LightColors = lightColorScheme(
    primary = Primary500,
    onPrimary = TextOnPrimary,
    primaryContainer = Primary100,
    onPrimaryContainer = Primary900,

    secondary = Secondary500,
    onSecondary = TextOnPrimary,
    secondaryContainer = Secondary100,
    onSecondaryContainer = Secondary900,

    background = BackgroundColor,
    onBackground = Secondary900,

    surface = SurfaceColor,
    onSurface = Secondary900,

    error = Error,
    onError = TextOnPrimary
)

@Composable
fun FarmaciaMedicitasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}