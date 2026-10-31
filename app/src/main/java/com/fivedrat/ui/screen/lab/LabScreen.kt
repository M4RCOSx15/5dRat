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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.fivedrat.data.sql.AndroidSqlExecutor
import com.fivedrat.data.sql.DemoScratchDb
import com.fivedrat.domain.sql.SqlResult
import com.fivedrat.ui.component.RatCapturedDialog
import com.fivedrat.ui.theme.CodeTextStyle
import com.fivedrat.ui.theme.LocalSemanticChipColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Tela Laboratório (M4) — console SQL do protótipo `5drat_laborat_rio`.
 * Execução real é da M7 (motor isolado); aqui o console captura a consulta
 * e mostra estado vazio honesto — sem resultados ilustrativos (P8).
 */
@Composable
fun LabScreen(modifier: Modifier = Modifier) {
    var sql by remember { mutableStateOf("SELECT * FROM produtos LIMIT 10;") }
    var result by remember { mutableStateOf<SqlResult?>(null) }
    var busy by remember { mutableStateOf(false) }
    var captured by remember { mutableStateOf(false) }
    var ratPresent by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val chips = LocalSemanticChipColors.current

    // Validador simples: confere o ESTADO do sandbox (não o SQL digitado) —
    // se o rato sumiu de produtos, houve captura. Mini-MissionValidator.
    fun checkRat() {
        val r =
            AndroidSqlExecutor(
                scratchDbPath = DemoScratchDb.ensure(context).path,
                readOnly = true,
            ).execute("SELECT count(*) FROM produtos WHERE nome = 'Rato de borracha'")
        if (r is SqlResult.Success) {
            val count =
                r.rows
                    .firstOrNull()
                    ?.firstOrNull()
                    ?.toIntOrNull() ?: -1
            if (ratPresent && count == 0) {
                captured = true
            }
            ratPresent = count > 0
        }
    }

    if (captured) {
        RatCapturedDialog(onDismiss = { captured = false })
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Schema do banco ativo
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.lab_schema_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("produtos", "clientes", "pedidos").forEach { table ->
                        androidx.compose.material3.Surface(
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
            }
        }

        // Console SQL
        Text(
            text = stringResource(R.string.lab_console_title),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
                                        scratchDbPath = DemoScratchDb.ensure(context).path,
                                        readOnly = false,
                                    ).execute(sql)
                                }
                            busy = false
                            checkRat()
                        }
                    }
                },
                enabled = !busy,
            ) {
                Text(stringResource(R.string.lab_run))
            }
            OutlinedButton(onClick = {
                sql = ""
                result = null
            }) {
                Text(stringResource(R.string.lab_clear))
            }
        }

        // Resultado — estado vazio honesto
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
