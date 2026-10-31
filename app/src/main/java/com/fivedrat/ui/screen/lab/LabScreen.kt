package com.fivedrat.ui.screen.lab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.data.AppServices
import com.fivedrat.data.sql.AndroidSqlExecutor
import com.fivedrat.data.sql.CaseSandbox
import com.fivedrat.domain.mission.Campaign
import com.fivedrat.domain.mission.MissionValidator
import com.fivedrat.domain.sql.SqlResult
import com.fivedrat.ui.component.RatCapturedDialog
import com.fivedrat.ui.theme.CodeTextStyle
import com.fivedrat.ui.theme.LocalSemanticChipColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Laboratório do caso ativo (M8): sandbox próprio por caso, briefing real,
 * e o MissionValidator confere o ESTADO final após cada execução.
 */
@Composable
fun LabScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val case = Campaign.byId(AppServices.activeCaseId)
    var sql by remember(case.id) {
        mutableStateOf("SELECT * FROM ${case.tables.first()} LIMIT 10;")
    }
    var result by remember { mutableStateOf<SqlResult?>(null) }
    var busy by remember { mutableStateOf(false) }
    var captured by remember { mutableStateOf(false) }
    var alreadyComplete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val chips = LocalSemanticChipColors.current

    fun validator() =
        MissionValidator(
            AndroidSqlExecutor(
                scratchDbPath = CaseSandbox.ensure(context, case).path,
                readOnly = true,
            ),
        )

    LaunchedEffect(case.id) {
        withContext(Dispatchers.IO) {
            alreadyComplete = validator().isComplete(case)
        }
    }

    if (captured) {
        RatCapturedDialog(
            onDismiss = { captured = false },
            onNextMission = {
                captured = false
                onNavigate("missions")
            },
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Briefing do caso
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${case.code} — ${case.title}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = case.briefing,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        // Schema do caso
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            case.tables.forEach { table ->
                Surface(
                    color = chips.tableBackground,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = table,
                        style = CodeTextStyle,
                        color = chips.tableForeground,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }
        }

        // Console SQL
        OutlinedTextField(
            value = sql,
            onValueChange = { sql = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = CodeTextStyle,
            minLines = 4,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (!busy) {
                        busy = true
                        scope.launch {
                            result =
                                withContext(Dispatchers.IO) {
                                    AndroidSqlExecutor(
                                        scratchDbPath = CaseSandbox.ensure(context, case).path,
                                        readOnly = false,
                                    ).execute(sql)
                                }
                            busy = false
                            if (!alreadyComplete &&
                                withContext(Dispatchers.IO) { validator().isComplete(case) }
                            ) {
                                alreadyComplete = true
                                captured = true
                                scope.launch {
                                    AppServices.progress.recordMissionCompleted(case.id, case.xpReward)
                                    AppServices.progress.recordRatCaptured(case.id)
                                }
                            }
                        }
                    }
                },
                enabled = !busy,
            ) {
                Text(stringResource(R.string.lab_run))
            }
            OutlinedButton(
                onClick = {
                    sql = ""
                    result = null
                },
            ) {
                Text(stringResource(R.string.lab_clear))
            }
            OutlinedButton(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) { CaseSandbox.reset(context, case) }
                        result = null
                        alreadyComplete = false
                    }
                },
            ) {
                Text(stringResource(R.string.lab_reset))
            }
        }

        // Resultado
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.lab_result_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                when (val r = result) {
                    null ->
                        Text(
                            text = stringResource(R.string.lab_result_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    is SqlResult.Success -> {
                        Text(
                            text = stringResource(R.string.lab_result_rows, r.rows.size),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Text(
                            text = r.columns.joinToString(" | "),
                            style = CodeTextStyle,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        r.rows.take(10).forEach { row ->
                            Text(
                                text = row.joinToString(" | ") { it ?: "NULL" },
                                style = CodeTextStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    is SqlResult.Blocked ->
                        Text(
                            text = r.reason,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    is SqlResult.Error ->
                        Text(
                            text = r.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                }
            }
        }
    }
}
