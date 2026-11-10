package com.fivedrat.data.narrative

import android.content.Context
import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.narrative.NarrativeResult

/**
 * Binding do variant aiDebug (M12): habilita o botão de narrativa por IA
 * e gera via LiteRT-LM on-device, com narrativa de reserva em caso de
 * modelo ausente ou falha.
 */
object NarrativeModule {
    val aiAvailable: Boolean = true

    suspend fun briefing(
        context: Context,
        case: Case,
    ): NarrativeResult = LiteRtNarrativeGenerator(context.applicationContext).briefing(case)
}
