package com.fivedrat.data.scenario

import com.fivedrat.domain.mission.Campaign
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeterministicScenarioGeneratorTest {
    private val gen = DeterministicScenarioGenerator()

    @Test
    fun `same runSeed produces identical seed`() {
        val a = gen.seedSql(Campaign.byId("case_001"), 42L)
        val b = gen.seedSql(Campaign.byId("case_001"), 42L)
        assertEquals(a, b)
    }

    @Test
    fun `different runSeeds produce variants across cases`() {
        Campaign.cases.forEach { case ->
            val variants = (1L..20L).map { gen.seedSql(case, it).joinToString() }.toSet()
            assertTrue("variantes insuficientes em ${case.id}", variants.size > 1)
        }
    }

    @Test
    fun `objective target always present in seeds`() {
        (1L..50L).forEach { seed ->
            val c1 = gen.seedSql(Campaign.byId("case_001"), seed).joinToString()
            assertTrue(c1.contains("'Rato de borracha'"))
            val c3 = gen.seedSql(Campaign.byId("case_003"), seed).joinToString()
            assertTrue(c3.contains("'Silas'"))
        }
    }

    @Test
    fun `fallback to static seed for unknown case`() {
        val unknown = Campaign.byId("case_001").copy(id = "unknown")
        assertEquals(unknown.seedSql, gen.seedSql(unknown, 7L))
    }
}
