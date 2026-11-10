package com.fivedrat.domain.narrative

import com.fivedrat.domain.mission.Case

/**
 * Resultado da geração de narrativa.
 * aiGenerated informa a origem do texto para a UI sinalizar com
 * honestidade (P6) quando o LLM não produziu o conteúdo.
 */
data class NarrativeResult(
    val text: String,
    val aiGenerated: Boolean,
)

/**
 * Fronteira de narrativa (M12): gera texto de SABOR para o briefing do caso.
 *
 * Regra do ADR-012: jamais usada para dados validados pelo objectiveSql —
 * a mecânica do jogo permanece determinística (ScenarioGenerator, M10).
 */
fun interface NarrativeGenerator {
    suspend fun briefing(case: Case): NarrativeResult
}
