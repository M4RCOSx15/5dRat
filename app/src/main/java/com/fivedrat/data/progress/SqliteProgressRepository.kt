package com.fivedrat.data.progress

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.fivedrat.domain.PlayerProgress
import com.fivedrat.domain.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

/**
 * ProgressRepository real: banco progress.db separado do sandbox de missões.
 * Eventos idempotentes por mission_id+kind (INSERT OR IGNORE) e progresso
 * derivado por agregação — o SQL do jogador nunca toca este arquivo.
 */
class SqliteProgressRepository(
    context: Context,
) : ProgressRepository {
    private val dbFile = File(context.filesDir, "progress.db")

    private val state = MutableStateFlow(readProgress())
    override val progress: Flow<PlayerProgress> = state

    override suspend fun recordRatCaptured(missionId: String): PlayerProgress = record(missionId, "rat", 0)

    override suspend fun recordMissionCompleted(
        missionId: String,
        xp: Int,
    ): PlayerProgress = record(missionId, "mission_completed", xp)

    private fun record(
        missionId: String,
        kind: String,
        xp: Int,
    ): PlayerProgress {
        SQLiteDatabase.openOrCreateDatabase(dbFile, null).use { db ->
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS mission_events (" +
                    "mission_id TEXT NOT NULL, kind TEXT NOT NULL, xp INTEGER NOT NULL, " +
                    "PRIMARY KEY (mission_id, kind))",
            )
            db.execSQL(
                "INSERT OR IGNORE INTO mission_events (mission_id, kind, xp) VALUES (?, ?, ?)",
                arrayOf(missionId, kind, xp),
            )
        }
        val updated = readProgress()
        state.value = updated
        return updated
    }

    private fun readProgress(): PlayerProgress {
        if (!dbFile.exists()) return PlayerProgress.Empty
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
            db
                .rawQuery(
                    "SELECT " +
                        "COALESCE(SUM(CASE WHEN kind='mission_completed' THEN xp END), 0), " +
                        "COUNT(CASE WHEN kind='mission_completed' THEN 1 END), " +
                        "COUNT(CASE WHEN kind='rat' THEN 1 END) " +
                        "FROM mission_events",
                    null,
                ).use { c ->
                    if (!c.moveToFirst()) return@use PlayerProgress.Empty
                    val xp = c.getInt(0)
                    PlayerProgress(
                        xp = xp,
                        level = 1 + xp / XP_PER_LEVEL,
                        missionsCompleted = c.getInt(1),
                        ratsCaptured = c.getInt(2),
                    )
                }
        }
    }

    private companion object {
        const val XP_PER_LEVEL = 300
    }
}
