package com.fivedrat.data.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File

/**
 * Banco sandbox do Laboratório: loja_demo.db em diretório privado do app,
 * criado na primeira execução. É o banco "de jogo" — o jogador pode quebrá-lo
 * à vontade (reset o recria); o banco de progresso fica intocado.
 */
object DemoScratchDb {
    fun ensure(context: Context): File {
        val db = File(context.filesDir, "loja_demo.db")
        if (!db.exists()) {
            SQLiteDatabase.openOrCreateDatabase(db, null).use { conn ->
                seed(conn)
            }
        }
        return db
    }

    fun reset(context: Context) {
        context.filesDir.listFiles { f -> f.name.startsWith("loja_demo.db") }?.forEach { it.delete() }
        ensure(context)
    }

    private fun seed(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE produtos (id INTEGER PRIMARY KEY, nome TEXT, categoria TEXT, preco REAL)")
        db.execSQL("CREATE TABLE clientes (id INTEGER PRIMARY KEY, nome TEXT, cidade TEXT)")
        db.execSQL("CREATE TABLE pedidos (id INTEGER PRIMARY KEY, cliente_id INTEGER, produto_id INTEGER, qtd INTEGER)")
        db.execSQL(
            "INSERT INTO produtos (nome, categoria, preco) VALUES " +
                "('Teclado mecânico', 'Acessórios', 299.90)," +
                "('Mouse sem fio', 'Acessórios', 149.90)," +
                "('Monitor 27', 'Telas', 1299.00)," +
                "('Rato de borracha', 'Suspeitos', 0.00)",
        )
        db.execSQL(
            "INSERT INTO clientes (nome, cidade) VALUES " +
                "('Ana', 'São Paulo'),('Bruno', 'Curitiba'),('Carla', 'Recife')",
        )
        db.execSQL("INSERT INTO pedidos (cliente_id, produto_id, qtd) VALUES (1, 1, 2), (2, 3, 1), (3, 2, 5)")
    }
}
