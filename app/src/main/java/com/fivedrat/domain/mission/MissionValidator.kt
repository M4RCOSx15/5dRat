package com.fivedrat.domain.mission

import com.fivedrat.domain.sql.SqlExecutor
import com.fivedrat.domain.sql.SqlResult

/**
 * Fronteira seção 5: valida o ESTADO final do sandbox (nunca o SQL digitado).
 * Conexão própria, somente-leitura, consulta qualificada da definição do caso.
 */
class MissionValidator(
    private val executor: SqlExecutor,
) {
    /** Verdadeiro quando objectiveSql retorna 1 linha avaliando "1". */
    fun isComplete(case: Case): Boolean {
        val result = executor.execute(case.objectiveSql)
        return result is SqlResult.Success &&
            result.rows.firstOrNull()?.firstOrNull() == "1"
    }
}
