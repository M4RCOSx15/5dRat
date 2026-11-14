package com.fivedrat

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Valida os fluxos de navegação por bottom bar entre TODOS os destinos.
 * Executar: gradlew connectedDebugAndroidTest (emulador/dispositivo).
 */
@RunWith(AndroidJUnit4::class)
class NavigationFlowTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun go(label: String) {
        rule.onNodeWithText(label).performClick()
        rule.waitForIdle()
    }

    private fun assertOn(marker: String) {
        rule.onNodeWithText(marker, substring = true).assertExists()
    }

    @Test
    fun labToHomeAndBack() {
        go("Laboratório")
        assertOn("Console SQL")
        go("Início")
        assertOn("MISSÃO ATUAL")
        go("Laboratório")
        assertOn("Console SQL")
    }

    @Test
    fun allTabTransitions() {
        // percorre cada destino a partir de cada outro destino
        val dests =
            mapOf(
                "Início" to "MISSÃO ATUAL",
                "Missões" to "Escolha seu próximo caso",
                "Laboratório" to "Console SQL",
                "Perfil" to "MEMBRO DA DIVISÃO 5DRAT",
            )
        dests.keys.forEach { from ->
            dests.forEach { (to, marker) ->
                go(to)
                assertOn(marker)
            }
        }
    }

    @Test
    fun labToProfileAndMissions() {
        go("Laboratório")
        go("Perfil")
        assertOn("Sua evolução")
        go("Laboratório")
        go("Missões")
        assertOn("CASE_001")
    }
}
