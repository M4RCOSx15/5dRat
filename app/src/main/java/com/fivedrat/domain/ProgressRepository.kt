package com.fivedrat.domain

import kotlinx.coroutines.flow.Flow

/**
 * Contrato de fronteira (seção 5 do Prompt Mestre): eventos, XP, conquistas
 * e histórico do jogador — idempotente. Implementação Room/DataStore na M6.
 */
interface ProgressRepository {
    val progress: Flow<PlayerProgress>

    /** Registra uma captura de rato; idempotente por `missionId`. */
    suspend fun recordRatCaptured(missionId: String): PlayerProgress

    /** Registra conclusão de missão com XP; idempotente por `missionId`. */
    suspend fun recordMissionCompleted(
        missionId: String,
        xp: Int,
    ): PlayerProgress
}
