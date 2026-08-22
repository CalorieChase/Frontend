package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.RoutePoint
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.CheckpointCard
import com.example.caloriechase.ui.components.MiniLegendRow
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.RouteMapCard
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenScaffold
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonGreen
import java.util.Locale
import kotlin.math.abs

@Composable
fun RoutePreviewScreen(
    routePreview: RoutePreview,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onRemixRoute: () -> Unit,
    onStartRun: () -> Unit
) {
    ScreenScaffold(
        title = "Route Overview",
        modifier = modifier.fillMaxSize(),
        onBack = onBack
    ) { innerPadding ->
        ScreenColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            applySafeDrawingInsets = false
        ) {
            SurfacePanel(emphasized = true) {
                Text(
                    text = "${routePreview.activityType} mission ready - ${routePreview.coinSpots.size} coins - ${routePreview.scoreLabel}",
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonGreen
                )
            }

            RouteMapCard(routePreview = routePreview)
            MiniLegendRow()

            SurfacePanel {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Endpoint",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = endpointTitle(routePreview.routePoints),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    BodyText(endpointDetail(routePreview.routePoints))
                }
            }

            routePreview.checkpoints.forEach { checkpoint ->
                CheckpointCard(checkpoint = checkpoint)
            }

            PrimaryButton(text = "Start route session", onClick = onStartRun)
            SecondaryButton(text = "Generate another route", onClick = onRemixRoute)
        }
    }
}

private fun endpointTitle(routePoints: List<RoutePoint>): String {
    val start = routePoints.firstOrNull()
    val end = routePoints.lastOrNull()
    if (start == null || end == null) {
        return "Endpoint unavailable"
    }
    if (isSamePoint(start, end)) {
        return "Returns to starting point"
    }
    return String.format(Locale.US, "%.5f, %.5f", end.lat, end.lng)
}

private fun endpointDetail(routePoints: List<RoutePoint>): String {
    val start = routePoints.firstOrNull()
    val end = routePoints.lastOrNull()
    if (start == null || end == null) {
        return "This route response did not include enough coordinates to resolve the ending point."
    }
    if (isSamePoint(start, end)) {
        return "This route ends where it began, so the finish point is the same as your selected starting location."
    }
    return "The generated route finishes at the coordinate above."
}

private fun isSamePoint(first: RoutePoint, second: RoutePoint): Boolean {
    return abs(first.lat - second.lat) < 0.0001 &&
        abs(first.lng - second.lng) < 0.0001
}
