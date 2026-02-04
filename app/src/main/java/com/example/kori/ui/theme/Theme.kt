package com.example.kori.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun KoriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = KoriDarkColorScheme  // Tema oscuro KÖRI

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KoriTypography,
        content = content
    )
}
