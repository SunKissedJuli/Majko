package com.coolgirl.majko.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Blue,
    secondary = Purple,
    background = White,
    surface = Purple,
    onSurface = Gray,
    onSecondary = Color.Black,
    onError = Red,
    error = Red
)

private val LightColorPalette = lightColorScheme(
    primary = Blue,
    secondary = Purple,
    background = White,
    surface = Purple,
    onSurface = Gray,
    onSecondary = Color.Black,
    onError = Red,
    error = Red
)

@Composable
fun MajkoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Material2Typography,
        shapes = Shapes,
        content = content
    )
}