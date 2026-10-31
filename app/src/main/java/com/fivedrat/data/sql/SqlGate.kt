package com.fivedrat.data.sql

/**
 * Portão de instrução única para o SQL do jogador (achados do spike M1c):
 * - ATTACH/DETACH alcançam arquivos irmãos do sandbox → bloqueados SEMPRE.
 * - PRAGMA pode alterar comportamento do banco → bloqueado.
 * - Apenas uma instrução por execução — resto é rejeitado, nunca descartado
 *   silenciosamente.
 * - readOnly (missões de consulta) restringe a SELECT.
 * Kotlin puro: 100% testável em JVM.
 */
object SqlGate {
    private const val MAX_LENGTH = 8_192

    private val ALWAYS_BLOCKED =
        listOf(
            Regex("""\battach\b""", RegexOption.IGNORE_CASE),
            Regex("""\bdetach\b""", RegexOption.IGNORE_CASE),
            Regex("""\bpragma\b""", RegexOption.IGNORE_CASE),
            Regex("""\bvacuum\b""", RegexOption.IGNORE_CASE),
        )

    private val WRITE_OPS =
        Regex(
            """^\s*(insert|update|delete|replace|drop|alter|create|truncate)\b""",
            RegexOption.IGNORE_CASE,
        )

    /** Nulidade = aprovado; texto = motivo didático do bloqueio. */
    fun check(
        sql: String,
        readOnly: Boolean,
    ): String? {
        if (sql.isBlank()) return "Consulta vazia."
        if (sql.length > MAX_LENGTH) return "Consulta grande demais para o laboratório."

        val withoutLiterals =
            sql
                .replace(Regex("'([^']|'')*'"), "''")
                .replace(Regex("""--[^\n]*"""), "")
                .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")

        for (blocked in ALWAYS_BLOCKED) {
            if (blocked.containsMatchIn(withoutLiterals)) {
                return "Comando bloqueado no sandbox (ATTACH/DETACH/PRAGMA/VACUUM)."
            }
        }

        if (!isSingleStatement(withoutLiterals)) {
            return "Apenas uma instrução por execução."
        }

        if (readOnly && !withoutLiterals.trim().startsWith("select", ignoreCase = true)) {
            return "Esta missão é somente consulta — use SELECT."
        }
        return null
    }

    /** Verdadeiro se houver exatamente uma instrução (strings literais já neutras). */
    private fun isSingleStatement(sql: String): Boolean {
        val trimmed = sql.trim().removeSuffix(";")
        return !trimmed.contains(';')
    }
}
