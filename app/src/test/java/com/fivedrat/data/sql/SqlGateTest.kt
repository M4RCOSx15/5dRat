package com.fivedrat.data.sql

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SqlGateTest {
    private fun allowed(
        sql: String,
        readOnly: Boolean = false,
    ): Boolean = SqlGate.check(sql, readOnly) == null

    @Test
    fun `allows plain select`() {
        assertTrue(allowed("SELECT * FROM produtos"))
        assertTrue(allowed("select id, nome from t where x = 'a;b'"))
    }

    @Test
    fun `blocks ATTACH and DETACH - spike M1c vector`() {
        assertNotNull(SqlGate.check("ATTACH DATABASE 'progress.db' AS p", readOnly = false))
        assertNotNull(SqlGate.check("  attach  database 'x.db'", readOnly = false))
        assertNotNull(SqlGate.check("SELECT * FROM t; DETACH p", readOnly = false))
    }

    @Test
    fun `blocks PRAGMA and VACUUM`() {
        assertNotNull(SqlGate.check("PRAGMA journal_mode=DELETE", readOnly = false))
        assertNotNull(SqlGate.check("VACUUM", readOnly = false))
    }

    @Test
    fun `blocks stacked statements`() {
        assertNotNull(SqlGate.check("SELECT 1; DROP TABLE t", readOnly = false))
        assertNotNull(SqlGate.check("SELECT 1;;", readOnly = false))
        // ponto-e-vírgula dentro de string literal NÃO empilha
        assertTrue(allowed("SELECT ';'"))
    }

    @Test
    fun `readOnly mode blocks writes but allows select`() {
        assertTrue(allowed("SELECT * FROM t", readOnly = true))
        assertNotNull(SqlGate.check("DELETE FROM t", readOnly = true))
        assertNotNull(SqlGate.check("insert into t values (1)", readOnly = true))
    }

    @Test
    fun `writeOps allowed in sandbox mode`() {
        assertTrue(allowed("DELETE FROM inventory WHERE suspect = 1", readOnly = false))
        assertTrue(allowed("UPDATE inventory SET anomaly = 0", readOnly = false))
    }

    @Test
    fun `blocked keyword inside string literal does not count`() {
        assertTrue(allowed("SELECT * FROM t WHERE note = 'attach is cool'"))
    }

    @Test
    fun `blank and oversized are blocked`() {
        assertNotNull(SqlGate.check("   ", readOnly = false))
        assertNotNull(SqlGate.check("SELECT ".padEnd(9000, 'x'), readOnly = false))
    }

    @Test
    fun `comments do not smuggle second statement`() {
        assertNotNull(SqlGate.check("SELECT 1 -- comment\n; DROP TABLE t", readOnly = false))
    }
}
