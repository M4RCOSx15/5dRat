package com.fivedrat.data

import com.fivedrat.domain.PlayerProgress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryProgressRepositoryTest {
    @Test
    fun `mission completion adds xp once - idempotent by mission id`() =
        runBlocking {
            val repo = InMemoryProgressRepository()
            repo.recordMissionCompleted("CASE_001", 120)
            val again = repo.recordMissionCompleted("CASE_001", 120)
            assertEquals(120, again.xp)
            assertEquals(1, again.missionsCompleted)
        }

    @Test
    fun `rat capture counts distinct missions only`() =
        runBlocking {
            val repo = InMemoryProgressRepository()
            repo.recordRatCaptured("CASE_001")
            repo.recordRatCaptured("CASE_001")
            repo.recordRatCaptured("CASE_002")
            assertEquals(2, repo.progress.first().ratsCaptured)
        }

    @Test
    fun `level derives from accumulated xp`() =
        runBlocking {
            val repo = InMemoryProgressRepository()
            val progress = repo.recordMissionCompleted("CASE_001", 350)
            assertEquals(2, progress.level)
            assertEquals(PlayerProgress.Empty.level, 1)
        }
}
