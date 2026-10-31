package com.fivedrat.domain

/** Estado de uma missão da campanha. */
enum class MissionState { Locked, Available, InProgress, Completed }

/** Caso jogável da campanha (Kotlin puro — sem imports android, ADR-007). */
data class Mission(
    val id: String,
    val code: String,
    val title: String,
    val state: MissionState,
)

/** Progresso acumulado do jogador — fonte real das telas a partir da M6. */
data class PlayerProgress(
    val xp: Int,
    val level: Int,
    val missionsCompleted: Int,
    val ratsCaptured: Int,
) {
    companion object {
        val Empty = PlayerProgress(xp = 0, level = 1, missionsCompleted = 0, ratsCaptured = 0)
    }
}
