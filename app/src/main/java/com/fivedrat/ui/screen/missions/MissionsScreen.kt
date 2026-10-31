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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.data.AppServices
import com.fivedrat.domain.PlayerProgress
import com.fivedrat.domain.mission.Campaign
import com.fivedrat.domain.mission.Case

/**
 * Casos reais da campanha: desbloqueio por progresso (índice > completadas
 * fica bloqueado), play seleciona o caso e navega ao Laboratório.
 */
@Composable
fun MissionsScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by AppServices.progress.progress.collectAsState(initial = PlayerProgress.Empty)

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
            text =
                stringResource(
                    R.string.missions_available,
                    Campaign.cases.countIndexed { i -> i <= progress.missionsCompleted },
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Campaign.cases.forEachIndexed { index, case ->
            val completed = index < progress.missionsCompleted
            val unlocked = index <= progress.missionsCompleted
            MissionItem(
                case = case,
                completed = completed,
                unlocked = unlocked,
                onPlay = {
                    AppServices.activeCaseId = case.id
                    onNavigate("lab")
                },
            )
        }
    }
}

private inline fun <T> List<T>.countIndexed(predicate: (Int) -> Boolean): Int = indices.count(predicate)

@Composable
private fun MissionItem(
    case: Case,
    completed: Boolean,
    unlocked: Boolean,
    onPlay: () -> Unit,
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
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
                    text = case.code,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = case.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color =
                        if (unlocked) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                )
            }
            Spacer(Modifier.width(8.dp))
            when {
                completed ->
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.missions_done),
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                !unlocked ->
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.missions_locked),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                else ->
                    IconButton(onClick = onPlay) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.missions_open),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
            }
        }
    }
}
