package com.fivedrat.data.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.fivedrat.domain.mission.Case
import java.io.File

/**
 * Sandbox por caso: arquivo {caseId}.db no diretório privado, criado de
 * schema+seed da definição. Descartável — reset recria do zero.
 */
object CaseSandbox {
    fun ensure(
        context: Context,
        case: Case,
    ): File {
        val db = File(context.filesDir, "${case.id}.db")
        if (!db.exists()) {
            SQLiteDatabase.openOrCreateDatabase(db, null).use { conn ->
                case.schemaSql.forEach { conn.execSQL(it) }
                case.seedSql.forEach { conn.execSQL(it) }
            }
        }
        return db
    }

    fun reset(
        context: Context,
        case: Case,
    ) {
        context.filesDir
            .listFiles { f -> f.name.startsWith("${case.id}.db") }
            ?.forEach { it.delete() }
        ensure(context, case)
    }
}
