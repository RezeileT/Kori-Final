package com.example.kori.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val GoldPrimary = Color(0xFFC9A86A)
val GoldDark = Color(0xFFB39555)
val BlackPrimary = Color(0xFF0D0D0D)
val BlackSecondary = Color(0xFF1A1A1A)
val TextPrimary = Color(0xFFE5E5E5)
val TextSecondary = Color(0xFFB0B0B0)

val KoriDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    secondary = GoldDark,
    tertiary = GoldDark,
    background = BlackPrimary,
    surface = BlackSecondary,
    onPrimary = BlackPrimary,
    onSecondary = BlackPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)