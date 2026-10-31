package com.fivedrat.data

import com.fivedrat.domain.PlayerProgress
import com.fivedrat.domain.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Implementação em memória do ProgressRepository — provisória para as telas
 * funcionarem com estado real até a persistência Room/DataStore (M6).
 * Mantém idempotência por missionId conforme o contrato.
 */
class InMemoryProgressRepository : ProgressRepository {
    private val completed = mutableSetOf<String>()
    private val captured = mutableSetOf<String>()
    private val state = MutableStateFlow(PlayerProgress.Empty)

    override val progress: Flow<PlayerProgress> = state

    override suspend fun recordRatCaptured(missionId: String): PlayerProgress {
        if (captured.add(missionId)) {
            state.value = state.value.copy(ratsCaptured = captured.size)
        }
        return state.value
    }

    override suspend fun recordMissionCompleted(
        missionId: String,
        xp: Int,
    ): PlayerProgress {
        if (completed.add(missionId)) {
            val newXp = state.value.xp + xp
            state.value =
                state.value.copy(
                    xp = newXp,
                    level = 1 + newXp / XP_PER_LEVEL,
                    missionsCompleted = completed.size,
                )
        }
        return state.value
    }

    private companion object {
        const val XP_PER_LEVEL = 300
    }
}
