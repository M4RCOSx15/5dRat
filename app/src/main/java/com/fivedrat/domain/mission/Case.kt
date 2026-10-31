package com.fivedrat.domain.mission

/**
 * Definição de caso jogável da campanha (Kotlin puro).
 * objectiveSql é a checagem de ESTADO final que o MissionValidator executa:
 * deve retornar 1 linha com valor "1" quando o objetivo foi cumprido.
 */
data class Case(
    val id: String,
    val code: String,
    val title: String,
    val briefing: String,
    val tables: List<String>,
    val schemaSql: List<String>,
    val seedSql: List<String>,
    val objectiveSql: String,
    val xpReward: Int,
)

object Campaign {
    val cases =
        listOf(
            Case(
                id = "case_001",
                code = "CASE_001",
                title = "O rato no inventário",
                briefing = "Um rato de borracha se infiltrou no inventário da loja. Encontre-o e remova-o da tabela.",
                tables = listOf("inventory"),
                schemaSql =
                    listOf(
                        "CREATE TABLE inventory (id INTEGER PRIMARY KEY, item TEXT, category TEXT, qty INTEGER)",
                    ),
                seedSql =
                    listOf(
                        "INSERT INTO inventory (item, category, qty) VALUES " +
                            "('Monitor 27', 'Telas', 12),('Teclado', 'Acessórios', 30)," +
                            "('Rato de borracha', 'Suspeitos', 1),('Mouse', 'Acessórios', 25)",
                    ),
                objectiveSql = "SELECT (count(*) = 0) FROM inventory WHERE item = 'Rato de borracha'",
                xpReward = 120,
            ),
            Case(
                id = "case_002",
                code = "CASE_002",
                title = "Identidade falsa",
                briefing =
                    "Um cliente cadastrou CPF duplicado. " +
                        "Marque o registro fraudulento como flagged = 1 (só o impostor).",
                tables = listOf("clients"),
                schemaSql =
                    listOf(
                        "CREATE TABLE clients (id INTEGER PRIMARY KEY, name TEXT, cpf TEXT, flagged INTEGER)",
                    ),
                seedSql =
                    listOf(
                        "INSERT INTO clients (name, cpf, flagged) VALUES " +
                            "('Ana Souza', '111.222.333-44', 0),('Bruno Falso', '111.222.333-44', 0)," +
                            "('Carla Lima', '555.666.777-88', 0)",
                    ),
                objectiveSql = "SELECT (count(*) = 1) FROM clients WHERE flagged = 1 AND name = 'Bruno Falso'",
                xpReward = 150,
            ),
            Case(
                id = "case_003",
                code = "CASE_003",
                title = "Cúmplices no banco",
                briefing = "Descubra pedidos feitos pelo cúmplice 'Silas' e remova-os todos da tabela orders.",
                tables = listOf("orders"),
                schemaSql =
                    listOf(
                        "CREATE TABLE orders (id INTEGER PRIMARY KEY, customer TEXT, item TEXT)",
                    ),
                seedSql =
                    listOf(
                        "INSERT INTO orders (customer, item) VALUES " +
                            "('Ana', 'Teclado'),('Silas', 'Roubo 1'),('Bruno', 'Mouse'),('Silas', 'Roubo 2')",
                    ),
                objectiveSql = "SELECT (count(*) = 0) FROM orders WHERE customer = 'Silas'",
                xpReward = 180,
            ),
        )

    fun byId(id: String): Case = cases.first { it.id == id }
}
