package com.fivedrat

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Smoke test: prova que a infraestrutura de testes unitários executa de ponta a ponta.
 * Não mede comportamento real — a cobertura de domínio começa na M6+.
 */
class BuildSanityTest {
    @Test
    fun `unit test infrastructure runs`() {
        assertTrue(true)
    }
}
