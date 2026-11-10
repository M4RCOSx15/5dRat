package com.fivedrat.data.narrative

import android.content.Context
import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.narrative.NarrativeResult

/**
 * Binding do variant release (M12): sem LLM — sempre narrativa de reserva.
 * A mesma FQCN existe em cada variant; o aiDebug troca a implementação.
 */
object NarrativeModule {
    val aiAvailable: Boolean = false

    @Suppress("UNUSED_PARAMETER")
    suspend fun briefing(
        context: Context,
        case: Case,
    ): NarrativeResult = TemplateNarrativeGenerator().briefing(case)
}
