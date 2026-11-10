package com.fivedrat.domain.narrative

import com.fivedrat.domain.mission.Campaign
import org.junit.Assert.assertTrue
import org.junit.Test

class NarrativePromptTest {
    private val case = Campaign.cases.first()

    @Test
    fun `system instruction restringe o tom e o idioma`() {
        val system = NarrativePrompt.SYSTEM_INSTRUCTION
        assertTrue(system.contains("português brasileiro"))
        assertTrue(system.contains("sem revelar a solução"))
    }

    @Test
    fun `prompt inclui identidade e contexto do caso`() {
        val prompt = NarrativePrompt.build(case)
        assertTrue(prompt.contains(case.title))
        assertTrue(prompt.contains(case.code))
        assertTrue(prompt.contains(case.briefing))
        case.tables.forEach { assertTrue(prompt.contains(it)) }
    }

    @Test
    fun `prompt proibe solucao e limita tamanho`() {
        val prompt = NarrativePrompt.build(case)
        assertTrue(prompt.contains("no máximo 3 frases"))
        assertTrue(prompt.contains("Não explique como resolver"))
    }
}
