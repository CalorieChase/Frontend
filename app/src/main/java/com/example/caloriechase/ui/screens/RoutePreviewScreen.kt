package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.components.MiniLegendRow
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.RouteMapCard
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.CheckpointCard
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonGreen

@Composable
fun RoutePreviewScreen(
    routePreview: RoutePreview,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onRemixRoute: () -> Unit,
    onStartRun: () -> Unit
) {
    ScreenColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(24.dp)
    ) {
        ScreenHeader(
            title = "Route overview",
            subtitle = "Preview the generated frontend route before you jump into the live game session.",
            onBack = onBack
        )

        SurfacePanel(emphasized = true) {
            Text(
                text = "${routePreview.activityType} mode ready • ${routePreview.scoreLabel} available",
                style = MaterialTheme.typography.titleMedium,
                color = NeonGreen
            )
        }

        RouteMapCard(routePreview = routePreview)
        MiniLegendRow()

        routePreview.checkpoints.forEach { checkpoint ->
            CheckpointCard(checkpoint = checkpoint)
        }

        PrimaryButton(text = "Start placeholder run", onClick = onStartRun)
        SecondaryButton(text = "Remix this route", onClick = onRemixRoute)
    }
}
