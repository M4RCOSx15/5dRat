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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.ui.theme.CodeTextStyle

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
                Text(
                    text = stringResource(R.string.home_mission_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.home_mission_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = stringResource(R.string.home_mission_sql_hint),
                        style = CodeTextStyle,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp),
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = { onNavigate("missions") }) {
                    Text(stringResource(R.string.home_continue_mission))
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
