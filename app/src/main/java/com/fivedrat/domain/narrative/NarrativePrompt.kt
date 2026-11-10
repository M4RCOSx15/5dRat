package com.fivedrat.domain.narrative

import com.fivedrat.domain.mission.Case

/**
 * Monta o prompt de narrativa a partir dos campos públicos do caso.
 * Kotlin puro: testável em JVM sem engine de IA.
 */
object NarrativePrompt {
    const val SYSTEM_INSTRUCTION: String =
        "Você é o narrador de um jogo educativo de investigação em SQL. " +
            "Escreva textos curtos de sabor noir em português brasileiro, " +
            "sem revelar a solução do caso e sem citar comandos SQL."

    fun build(case: Case): String =
        "Escreva um briefing de detetive de no máximo 3 frases para o caso " +
            "\"${case.title}\" (${case.code}). " +
            "Resumo oficial: ${case.briefing} " +
            "Tabelas envolvidas: ${case.tables.joinToString()}. " +
            "Tom: noir leve e divertido. Não explique como resolver."
}
