package com.fivedrat.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Pares fundo/texto para chips e tags do domínio do jogo (ADR-001):
 * tabela/entidade, coluna/variável, suspeito e sucesso.
 */
@Immutable
data class SemanticChipColors(
    val tableBackground: Color,
    val tableForeground: Color,
    val columnBackground: Color,
    val columnForeground: Color,
    val suspectBackground: Color,
    val suspectForeground: Color,
    val successBackground: Color,
    val successForeground: Color,
)

val DefaultSemanticChipColors =
    SemanticChipColors(
        tableBackground = SemanticTableBg,
        tableForeground = SemanticTableFg,
        columnBackground = SemanticColumnBg,
        columnForeground = SemanticColumnFg,
        suspectBackground = SemanticSuspectBg,
        suspectForeground = SemanticSuspectFg,
        successBackground = SemanticSuccessBg,
        successForeground = SemanticSuccessFg,
    )

val LocalSemanticChipColors = staticCompositionLocalOf { DefaultSemanticChipColors }
