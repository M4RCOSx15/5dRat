package com.fivedrat.data.narrative

import com.fivedrat.domain.mission.Campaign
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplateNarrativeGeneratorTest {
    private val generator = TemplateNarrativeGenerator()
    private val case = Campaign.cases.first()

    @Test
    fun `fallback nunca se apresenta como IA`() =
        runBlocking {
            assertFalse(generator.briefing(case).aiGenerated)
        }

    @Test
    fun `fallback e deterministico`() =
        runBlocking {
            assertEquals(generator.briefing(case).text, generator.briefing(case).text)
        }

    @Test
    fun `fallback compoe sabor com dados do caso`() =
        runBlocking {
            val text = generator.briefing(case).text
            assertTrue(text.isNotBlank())
            assertTrue(text.contains(case.briefing))
            case.tables.forEach { assertTrue(text.contains(it)) }
        }
}
