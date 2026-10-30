package com.fivedrat

import android.database.sqlite.SQLiteDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Spike M1c (descartável): verifica no dispositivo o que o binding
 * android.database.sqlite realmente expõe para isolamento.
 * Executar com: gradlew connectedDebugAndroidTest (requer emulador/dispositivo).
 */
@RunWith(AndroidJUnit4::class)
class SqlSandboxAndroidSpikeTest {
    @Test
    fun scratchDbIsFileScopedAndReadOnlyFlagWorks() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val scratch = File(context.filesDir, "spike_scratch.db")
        scratch.delete()

        SQLiteDatabase.openOrCreateDatabase(scratch, null).use { db ->
            db.execSQL("CREATE TABLE t (id INTEGER PRIMARY KEY, v TEXT)")
            db.execSQL("INSERT INTO t (v) VALUES ('rato')")
        }

        SQLiteDatabase
            .openDatabase(
                scratch.path,
                null,
                SQLiteDatabase
                    .OPEN_READONLY,
            ).use { db ->
                db.rawQuery("SELECT v FROM t", null).use { c ->
                    assertTrue(c.moveToFirst())
                }
                try {
                    db.execSQL("DELETE FROM t")
                    throw AssertionError("escrita em readonly deveria falhar")
                } catch (expected: android.database.SQLException) {
                    // esperado
                }
            }

        scratch.delete()
    }
}
