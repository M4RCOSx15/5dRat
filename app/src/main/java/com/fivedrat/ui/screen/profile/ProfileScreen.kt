package com.fivedrat.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fivedrat.R
import com.fivedrat.data.AppServices
import com.fivedrat.domain.PlayerProgress
import com.fivedrat.domain.mission.Campaign

/**
 * Tela Perfil (M5) — baseada no protótipo `5drat_perfil`.
 * Métricas ilustrativas do protótipo NÃO são reproduzidas (P6/P8): os
 * valores reais chegam com a persistência da M6 — mostram "—" até lá.
 * Avatar robot_001 local, sem suavização (P1, ADR-004).
 */
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(R.drawable.robot_001),
            contentDescription = stringResource(R.string.profile_avatar_cd),
            modifier =
                Modifier
                    .size(96.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
        )
        Text(
            text = "robot_001",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.profile_division),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        val context = LocalContext.current
        val progress by AppServices
            .progress(context)
            .progress
            .collectAsState(initial = PlayerProgress.Empty)

        SectionCard(title = stringResource(R.string.profile_evolution)) {
            Text(
                text = stringResource(R.string.profile_level, progress.level),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.profile_xp, progress.xp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        SectionCard(title = stringResource(R.string.profile_history)) {
            Text(
                text =
                    stringResource(
                        R.string.profile_cases,
                        progress.missionsCompleted,
                        Campaign.cases.size,
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.profile_rats, progress.ratsCaptured),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            content()
        }
    }
}
