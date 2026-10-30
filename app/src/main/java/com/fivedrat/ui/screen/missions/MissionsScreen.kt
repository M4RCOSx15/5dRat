package com.fivedrat.ui.screen.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R

/** Caso disponível para seleção. Conteúdo real do jogo — migra ao domínio na M6. */
private data class MissionCard(
    val code: String,
    val title: String,
    val locked: Boolean,
)

// Lista estática dos casos da campanha — M6 lê o progresso real e o bloqueio dinâmico.
private val missions =
    listOf(
        MissionCard("CASE_001", "O rato no inventário", locked = false),
        MissionCard("CASE_002", "Identidade falsa", locked = false),
        MissionCard("CASE_003", "Cúmplices no banco", locked = true),
    )

@Composable
fun MissionsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.missions_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.missions_available, missions.count { !it.locked }),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        missions.forEach { mission ->
            MissionItem(mission)
        }
    }
}

@Composable
private fun MissionItem(mission: MissionCard) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (mission.locked) {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerLowest
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mission.code,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = mission.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color =
                        if (mission.locked) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                )
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (mission.locked) Icons.Default.Lock else Icons.Default.PlayArrow,
                contentDescription =
                    if (mission.locked) {
                        stringResource(R.string.missions_locked)
                    } else {
                        stringResource(R.string.missions_open)
                    },
                tint =
                    if (mission.locked) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
            )
        }
    }
}
