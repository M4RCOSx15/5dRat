package com.fivedrat.data.sql

import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.fivedrat.domain.sql.SqlExecutor
import com.fivedrat.domain.sql.SqlResult

/**
 * Executor real do SQL do jogador sobre o arquivo de missão (scratch DB).
 * Nunca toca o banco de progresso — o arquivo recebido é o sandbox.
 */
class AndroidSqlExecutor(
    private val scratchDbPath: String,
    private val readOnly: Boolean,
    private val maxRows: Int = 500,
) : SqlExecutor {
    override fun execute(sql: String): SqlResult {
        SqlGate.check(sql, readOnly)?.let { return SqlResult.Blocked(it) }

        val flags = if (readOnly) SQLiteDatabase.OPEN_READONLY else 0
        return try {
            SQLiteDatabase.openDatabase(scratchDbPath, null, flags).use { db ->
                db.rawQuery(sql, null).use { cursor ->
                    val columns = cursor.columnNames.toList()
                    val rows = mutableListOf<List<String?>>()
                    while (cursor.moveToNext() && rows.size < maxRows) {
                        rows.add(columns.indices.map { cursor.getString(it) })
                    }
                    SqlResult.Success(columns, rows)
                }
            }
        } catch (e: SQLException) {
            SqlResult.Error("Erro SQL: ${e.message.orEmpty().substringBefore('\n')}")
        }
    }
}
