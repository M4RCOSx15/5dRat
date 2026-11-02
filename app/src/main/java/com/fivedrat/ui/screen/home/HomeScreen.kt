package com.fivedrat.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.data.AppServices
import com.fivedrat.domain.PlayerProgress
import com.fivedrat.domain.mission.Campaign

/**
 * Tela Início (M2) — baseada no protótipo `5drat_in_cio`.
 * Dados ilustrativos do protótipo (XP, contagens, CASE_FILE #) NÃO são
 * reproduzidos: a fonte real chega na M6 (persistência/domínio).
 */
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // MISSÃO ATUAL — cartão destaque do protocolo ativo
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val context = LocalContext.current
                val progress by AppServices
                    .progress(context)
                    .progress
                    .collectAsState(initial = PlayerProgress.Empty)
                val current = Campaign.cases.getOrNull(progress.missionsCompleted)
                Text(
                    text = stringResource(R.string.home_mission_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = current?.title ?: stringResource(R.string.home_campaign_done),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text =
                        stringResource(
                            R.string.home_progress,
                            progress.level,
                            progress.xp,
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (current != null) {
                            AppServices.activeCaseId = current.id
                            onNavigate("lab")
                        } else {
                            onNavigate("missions")
                        }
                    },
                ) {
                    Text(
                        text =
                            if (current != null) {
                                stringResource(R.string.home_continue_mission)
                            } else {
                                stringResource(R.string.home_review_cases)
                            },
                    )
                }
            }
        }

        // Atalhos
        Text(
            text = stringResource(R.string.home_shortcuts),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { onNavigate("missions") },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.home_new_mission))
            }
            OutlinedButton(
                onClick = { onNavigate("lab") },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.home_train_sql))
            }
        }

        // Dica do Detetive — cartão informacional
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.home_detective_tip_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.home_detective_tip_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}
