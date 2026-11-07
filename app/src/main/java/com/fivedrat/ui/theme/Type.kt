@file:OptIn(ExperimentalTextApi::class)

package com.fivedrat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fivedrat.R

// Escala tipográfica dos protótipos (docs/PROTOTYPES.md).
// Fontes embarcadas OFL em res/font: Plus Jakarta Sans (UI, variável wght)
// e JetBrains Mono (código, variável wght) — pesos via FontVariation.
private fun jakarta(weight: Int) =
    Font(
        R.font.plusjakartasans_variable,
        variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
    )

private fun jetbrains(weight: Int) =
    Font(
        R.font.jetbrainsmono_variable,
        variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
    )

val UiFontFamily =
    FontFamily(
        jakarta(400),
        jakarta(500),
        jakarta(600),
        jakarta(700),
    )
val CodeFontFamily =
    FontFamily(
        jetbrains(400),
        jetbrains(600),
    )

val AppTypography =
    Typography(
        displaySmall =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
        headlineSmall =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        labelSmall =
            TextStyle(
                fontFamily = UiFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
            ),
    )

// SQL, código e etiquetas técnicas: 11–15sp conforme protótipo.
val CodeTextStyle =
    TextStyle(
        fontFamily = CodeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    )
