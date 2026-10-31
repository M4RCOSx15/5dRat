package com.fivedrat.data

import com.fivedrat.domain.ProgressRepository
import com.fivedrat.domain.mission.Campaign

/**
 * Composição manual (DI simples — ADR-002): repositório de progresso
 * compartilhado e caso ativo selecionado na tela de Missões.
 * Progresso é em memória por ora — persistência real na continuação da M6.
 */
object AppServices {
    val progress: ProgressRepository = InMemoryProgressRepository()
    var activeCaseId: String = Campaign.cases.first().id
}
