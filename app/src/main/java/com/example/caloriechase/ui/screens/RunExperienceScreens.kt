package com.example.caloriechase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.RunStat
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.RouteMapCard
import com.example.caloriechase.ui.components.RunStatsRow
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.SurfaceOutline

@Composable
fun ActiveRunScreen(
    routePreview: RoutePreview,
    stats: List<RunStat>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onFinishRun: () -> Unit
) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Live route session",
            subtitle = "Follow the generated route, sweep the coin lanes, and finish strong.",
            onBack = onBack
        )

        RouteMapCard(routePreview = routePreview)

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Live progress",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                RunStatsRow(stats = stats)
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Checkpoint tracker",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                routePreview.checkpoints.forEachIndexed { index, checkpoint ->
                    BodyText("${index + 1}. ${checkpoint.title} • ${checkpoint.reward}")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                .border(1.dp, SurfaceOutline, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${routePreview.coinSpots.size} coin drops loaded across a ${routePreview.routeTypeLabel.lowercase()} route.",
                style = MaterialTheme.typography.bodyLarge,
                color = NeonGreen,
                modifier = Modifier.padding(20.dp)
            )
        }

        PrimaryButton(text = "Finish session", onClick = onFinishRun)
    }
}

@Composable
fun RunSummaryScreen(
    routePreview: RoutePreview,
    stats: List<RunStat>,
    modifier: Modifier = Modifier,
    onBackHome: () -> Unit,
    onTryAnother: () -> Unit
) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Session summary",
            subtitle = "Your generated treasure route is complete.",
            onBack = null
        )

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = routePreview.routeName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText("Strong finish. The route, coins, and score all came from the live backend response.")
            }
        }

        RunStatsRow(stats = stats)

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Highlights",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                listOf(
                    "Covered ${routePreview.distanceLabel} on a ${routePreview.routeTypeLabel.lowercase()} mission.",
                    "Collected ${routePreview.coinSpots.size} coin drops across ${routePreview.checkpoints.size} checkpoint zones.",
                    "Finished with an estimated ${routePreview.caloriesLabel} burn."
                ).forEach { line ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("•", color = NeonGreen)
                        BodyText(line)
                    }
                }
                Text(
                    text = "Reward chest ready: ${routePreview.scoreLabel}",
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonGreen
                )
            }
        }

        PrimaryButton(text = "Back to home", onClick = onBackHome)
        SecondaryButton(text = "Plan another route", onClick = onTryAnother)
    }
}
