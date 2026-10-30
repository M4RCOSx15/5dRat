package com.fivedrat

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.sql.DriverManager
import java.util.Properties

/**
 * Spike M1c (descartável): prova os mecanismos de isolamento de SQL do jogador
 * usando sqlite-jdbc em JVM. O objetivo é responder se ATTACH consegue sair
 * do sandbox, se modo read-only bloqueia escrita e como tratar
 * multi-statements — antes de construir o motor real na M7 sobre
 * android.database.sqlite.
 */
class SqlSandboxSpikeTest {
    @get:Rule
    val tmp = TemporaryFolder()

    private lateinit var scratchPath: String
    private lateinit var progressPath: String

    @Before
    fun setUp() {
        val scratch = tmp.newFile("scratch.db")
        scratchPath = scratch.absolutePath
        val progress = tmp.newFile("progress.db")
        progressPath = progress.absolutePath

        DriverManager.getConnection("jdbc:sqlite:$progressPath").use { conn ->
            conn.createStatement().execute(
                "CREATE TABLE player_progress (id INTEGER PRIMARY KEY, xp INTEGER, secret TEXT)",
            )
            conn.createStatement().execute(
                "INSERT INTO player_progress (xp, secret) VALUES (999, 'flag-de-progresso')",
            )
        }
        DriverManager.getConnection("jdbc:sqlite:$scratchPath").use { conn ->
            conn.createStatement().execute(
                "CREATE TABLE inventory (id INTEGER PRIMARY KEY, item TEXT, suspect INTEGER)",
            )
            conn.createStatement().execute(
                "INSERT INTO inventory (item, suspect) VALUES ('rato', 1), ('queijo', 0)",
            )
        }
    }

    @Test
    fun `player sql works on scratch db`() {
        DriverManager.getConnection("jdbc:sqlite:$scratchPath").use { conn ->
            val rs =
                conn
                    .createStatement()
                    .executeQuery("SELECT item FROM inventory WHERE suspect = 1")
            assertTrue(rs.next())
            assertEquals("rato", rs.getString(1))
            conn.createStatement().execute("DELETE FROM inventory WHERE suspect = 1")
        }
    }

    @Test
    fun `ATTACH can reach sibling files - gate is required`() {
        // PROVA o vetor: ATTACH relativo/absoluto lê o banco de progresso
        // a partir da conexão do jogador. Defesa real: bloquear ATTACH antes
        // de executar (authorizer não exposto pelo binding → gate de instrução).
        DriverManager.getConnection("jdbc:sqlite:$scratchPath").use { conn ->
            conn.createStatement().execute("ATTACH DATABASE '$progressPath' AS p")
            val rs =
                conn
                    .createStatement()
                    .executeQuery("SELECT secret FROM p.player_progress")
            assertTrue("ATTACH deve alcançar arquivo irmão", rs.next())
            assertEquals("flag-de-progresso", rs.getString(1))
        }
    }

    @Test
    fun `read only mode blocks writes`() {
        val props = Properties().apply { setProperty("open_mode", "1") } // SQLITE_OPEN_READONLY
        DriverManager.getConnection("jdbc:sqlite:$scratchPath", props).use { conn ->
            val rs = conn.createStatement().executeQuery("SELECT count(*) FROM inventory")
            assertTrue(rs.next())
            try {
                conn.createStatement().execute("DELETE FROM inventory")
                fail("escrita em readonly deveria falhar")
            } catch (expected: Exception) {
                assertTrue(expected.message.orEmpty().contains("readonly", ignoreCase = true))
            }
        }
    }

    @Test
    fun `stacked input executes only first statement`() {
        // ACHADO real: JDBC executa somente a 1a instrução e descarta o resto
        // silenciosamente — comportamento compatível com o gate single-statement
        // desejado na M7 (mas android execSQL/compileStatement deve ser verificado
        // no androidTest para confirmar o mesmo no binding do Android).
        DriverManager.getConnection("jdbc:sqlite:$scratchPath").use { conn ->
            conn.createStatement().execute(
                "INSERT INTO inventory (item, suspect) VALUES ('x', 0); DELETE FROM inventory",
            )
            val rs =
                conn.createStatement().executeQuery("SELECT count(*) FROM inventory")
            rs.next()
            assertEquals("só o INSERT rodou; o DELETE empilhado foi descartado", 3, rs.getInt(1))
        }
    }
}
