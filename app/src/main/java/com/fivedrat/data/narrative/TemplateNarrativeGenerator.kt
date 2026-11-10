package com.fivedrat.data.narrative

import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.narrative.NarrativeGenerator
import com.fivedrat.domain.narrative.NarrativeResult
import kotlin.math.abs

/**
 * Narrativa de reserva determinística (M12): compõe um briefing de sabor a
 * partir dos campos do caso, sem depender de modelo de IA. Marca
 * aiGenerated = false para a UI sinalizar a origem do texto.
 */
class TemplateNarrativeGenerator : NarrativeGenerator {
    override suspend fun briefing(case: Case): NarrativeResult {
        val opening = OPENINGS[abs(case.id.hashCode()) % OPENINGS.size]
        val tables = case.tables.joinToString(" e ")
        return NarrativeResult(
            text = "$opening ${case.briefing} Os rastros apontam para a tabela $tables.",
            aiGenerated = false,
        )
    }

    private companion object {
        val OPENINGS =
            listOf(
                "Mais uma madrugada no setor de anomalias, investigador.",
                "O scanner disparou de novo — padrão conhecido de intrusão.",
                "Expediente duvidoso registrado no banco de dados da loja.",
            )
    }
}
