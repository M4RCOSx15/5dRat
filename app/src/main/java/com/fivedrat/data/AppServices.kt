package com.fivedrat.data

import android.content.Context
import com.fivedrat.data.progress.SqliteProgressRepository
import com.fivedrat.domain.ProgressRepository
import com.fivedrat.domain.mission.Campaign

/**
 * Composição manual (DI simples — ADR-002): repositório de progresso real
 * (SQLite) e caso ativo selecionado na tela de Missões.
 */
object AppServices {
    private var cached: ProgressRepository? = null

    fun progress(context: Context): ProgressRepository =
        cached ?: SqliteProgressRepository(context.applicationContext).also { cached = it }

    var activeCaseId: String = Campaign.cases.first().id
}
