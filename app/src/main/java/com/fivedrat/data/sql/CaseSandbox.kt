package com.fivedrat.data.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.fivedrat.data.scenario.DeterministicScenarioGenerator
import com.fivedrat.domain.mission.Case
import com.fivedrat.domain.mission.ScenarioGenerator
import java.io.File
import kotlin.random.Random

/**
 * Sandbox por caso: arquivo {caseId}.db no diretório privado, criado de
 * schema+seed gerada pelo ScenarioGenerator com runSeed gravada em _meta.
 * Descartável — reset recria do zero com runSeed nova (nova variante).
 */
object CaseSandbox {
    private val generator: ScenarioGenerator = DeterministicScenarioGenerator()

    fun ensure(
        context: Context,
        case: Case,
    ): File {
        val db = File(context.filesDir, "${case.id}.db")
        if (!db.exists()) {
            create(context, case)
        }
        return db
    }

    private fun create(
        context: Context,
        case: Case,
    ): File {
        val db = File(context.filesDir, "${case.id}.db")
        val runSeed = Random.nextLong()
        SQLiteDatabase.openOrCreateDatabase(db, null).use { conn ->
            conn.execSQL("CREATE TABLE _meta (key TEXT PRIMARY KEY, value TEXT)")
            conn.execSQL("INSERT INTO _meta (key, value) VALUES ('runSeed', '$runSeed')")
            case.schemaSql.forEach { conn.execSQL(it) }
            generator.seedSql(case, runSeed).forEach { conn.execSQL(it) }
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
        create(context, case)
    }
}
