package com.fivedrat.data.scenario

import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.mission.ScenarioGenerator
import kotlin.random.Random

/**
 * Gerador determinístico por runSeed: kotlin.random.Random(seed) escolhe
 * variantes de conteúdo por caso. O objetivo (objectiveSql) permanece
 * válido — variantes mudam nomes/quantidade/ordem, nunca a invariante.
 */
class DeterministicScenarioGenerator : ScenarioGenerator {
    override fun seedSql(
        case: Case,
        runSeed: Long,
    ): List<String> {
        val rng = Random(runSeed)
        return when (case.id) {
            "case_001" -> inventoryCase(rng)
            "case_002" -> clientsCase(rng)
            "case_003" -> ordersCase(rng)
            else -> case.seedSql
        }
    }

    /** Rat de borracha entre itens sortidos — nome/categoria invariantes. */
    private fun inventoryCase(rng: Random): List<String> {
        val decoys =
            listOf(
                "('Monitor 27', 'Telas', %d)" to rng.nextInt(5, 20),
                "('Teclado', 'Acessórios', %d)" to rng.nextInt(10, 40),
                "('Mouse', 'Acessórios', %d)" to rng.nextInt(10, 40),
                "('Webcam', 'Acessórios', %d)" to rng.nextInt(2, 15),
                "('Headset', 'Áudio', %d)" to rng.nextInt(3, 12),
            ).shuffled(rng).take(3)
        val rows =
            (
                decoys.map { it.first.format(it.second) } +
                    "('Rato de borracha', 'Suspeitos', 1)"
            ).shuffled(rng)
        return listOf("INSERT INTO inventory (item, category, qty) VALUES ${rows.joinToString(",")}")
    }

    /** Impostor com nome variado — invariante: flagged=1 no nome sorteado. */
    private fun clientsCase(rng: Random): List<String> {
        val cpf =
            "%03d.%03d.%03d-44".format(
                rng.nextInt(100, 999),
                rng.nextInt(100, 999),
                rng.nextInt(100, 999),
            )
        val rows =
            listOf(
                "('Ana Souza', '$cpf', 0)",
                "('Bruno Falso', '$cpf', 0)",
                "('Carla Lima', '555.666.777-88', 0)",
            ).shuffled(rng)
        return listOf(
            "INSERT INTO clients (name, cpf, flagged) VALUES ${rows.joinToString(",")}",
        )
    }

    /** Cúmplice Silas com N pedidos roubados entre pedidos inocentes. */
    private fun ordersCase(rng: Random): List<String> {
        val innocent = listOf("Ana", "Bruno", "Carla", "Diego")
        val stolen = (1..rng.nextInt(2, 4)).map { "('Silas', 'Roubo $it')" }
        val honest =
            innocent.shuffled(rng).take(3).map {
                "('$it', '${listOf("Teclado", "Mouse", "Monitor").random(rng)}')"
            }
        val rows = (stolen + honest).shuffled(rng)
        return listOf("INSERT INTO orders (customer, item) VALUES ${rows.joinToString(",")}")
    }
}
