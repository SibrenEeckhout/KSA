package com.example.ksa.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    background = Grey900,
    surface = Grey900,
    onSurface = PureWhite,
    primary = Grey50,
    onPrimary = Grey900,
    secondary = AlmostPureWhite
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    background = PureWhite,
    surface = PureWhite,
    onSurface = Grey900,
    primary = Grey900,
    onPrimary = PureWhite,
    secondary = LightGray
)


@Composable
fun KsaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}