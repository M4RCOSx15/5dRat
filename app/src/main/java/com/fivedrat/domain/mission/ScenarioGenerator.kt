package com.fivedrat.domain.mission

/**
 * Fronteira seção 5: gera a seed SQL de um caso a partir de um runSeed.
 * Determinístico — mesmo runSeed produz exatamente a mesma seed (testável,
 * rejogável); runSeeds distintos produzem variantes (itens, nomes, ordem,
 * quantidade de alvos) sem mudar o objetivo validado.
 */
interface ScenarioGenerator {
    fun seedSql(
        case: Case,
        runSeed: Long,
    ): List<String>
}
