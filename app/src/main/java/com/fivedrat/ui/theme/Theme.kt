package com.fivedrat.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val AppLightColors =
    lightColorScheme(
        primary = AppPrimary,
        onPrimary = Color.White,
        primaryContainer = AppPrimaryContainer,
        onPrimaryContainer = Color.White,
        secondary = AppSecondary,
        onSecondary = Color.White,
        secondaryContainer = AppSecondaryContainer,
        onSecondaryContainer = AppOnSurface,
        tertiary = AppSecondary,
        onTertiary = Color.White,
        tertiaryContainer = AppTertiaryFixed,
        onTertiaryContainer = AppOnSurface,
        background = AppBackground,
        onBackground = AppOnSurface,
        surface = AppBackground,
        onSurface = AppOnSurface,
        surfaceVariant = AppSurfaceContainer,
        onSurfaceVariant = AppOnSurfaceVariant,
        surfaceBright = AppSurfaceBright,
        surfaceContainerLowest = AppSurfaceContainerLowest,
        surfaceContainerLow = AppSurfaceContainerLow,
        surfaceContainer = AppSurfaceContainer,
        surfaceContainerHigh = AppSurfaceContainerHigh,
        surfaceContainerHighest = AppSurfaceContainerHighest,
        primaryFixed = AppPrimaryFixed,
        primaryFixedDim = AppPrimaryFixedDim,
        outline = AppOutline,
        outlineVariant = AppOutlineVariant,
        error = AppError,
        onError = Color.White,
        errorContainer = AppErrorContainer,
        onErrorContainer = AppError,
    )

/**
 * Tema único do app. Somente light: os protótipos não definem paleta dark —
 * decisão registrada quando o tema dark for necessário.
 */
@Composable
fun FiveDRatTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSemanticChipColors provides DefaultSemanticChipColors) {
        MaterialTheme(
            colorScheme = AppLightColors,
            typography = AppTypography,
            content = content,
        )
    }
}
