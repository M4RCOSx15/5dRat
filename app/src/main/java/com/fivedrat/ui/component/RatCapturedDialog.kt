package com.fivedrat.ui.component

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.fivedrat.R

/**
 * Popup de captura: aranha cibernética capturando o rato (pixel art, loop).
 * Aparece quando o validador confirma a remoção do alvo no sandbox.
 * VideoView é built-in — sem dependência de player pesado (P4).
 */
@Composable
fun RatCapturedDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(0.85f)
                        .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.rat_captured_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(
                                Uri.parse(
                                    "android.resource://${ctx.packageName}/${R.raw.aranha_rato_pixelart}",
                                ),
                            )
                            setOnPreparedListener { it.isLooping = true }
                            start()
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 12.dp),
                )
                Text(
                    text = stringResource(R.string.rat_captured_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.rat_captured_close))
                }
            }
        }
    }
}
