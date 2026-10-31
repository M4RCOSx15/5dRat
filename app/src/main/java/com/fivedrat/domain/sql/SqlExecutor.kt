package com.fivedrat.domain.sql

/** Resultado de uma consulta executada no sandbox de missão. */
sealed interface SqlResult {
    data class Success(
        val columns: List<String>,
        val rows: List<List<String?>>,
    ) : SqlResult

    data class Blocked(
        val reason: String,
    ) : SqlResult

    data class Error(
        val message: String,
    ) : SqlResult
}

/**
 * Fronteira seção 5: executor de SQL do jogador sobre um banco sandbox.
 * Implementação real usa android.database.sqlite; contrato é Kotlin puro.
 */
interface SqlExecutor {
    fun execute(sql: String): SqlResult
}
