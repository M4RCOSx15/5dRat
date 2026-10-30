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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.ui.theme.CodeTextStyle
import com.fivedrat.ui.theme.LocalSemanticChipColors

/**
 * Tela Laboratório (M4) — console SQL do protótipo `5drat_laborat_rio`.
 * Execução real é da M7 (motor isolado); aqui o console captura a consulta
 * e mostra estado vazio honesto — sem resultados ilustrativos (P8).
 */
@Composable
fun LabScreen(modifier: Modifier = Modifier) {
    var sql by remember { mutableStateOf("SELECT * FROM produtos LIMIT 10;") }
    val chips = LocalSemanticChipColors.current

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
            Button(onClick = { /* execução real na M7 */ }) {
                Text(stringResource(R.string.lab_run))
            }
            OutlinedButton(onClick = { sql = "" }) {
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
                Text(
                    text = stringResource(R.string.lab_result_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
