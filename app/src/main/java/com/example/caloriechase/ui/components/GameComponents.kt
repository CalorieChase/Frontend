package com.example.caloriechase.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.RouteCheckpoint
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.RunStat
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.SurfaceOutline

@Composable
fun RouteMapCard(
    routePreview: RoutePreview,
    modifier: Modifier = Modifier
) {
    SurfacePanel(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .border(1.dp, SurfaceOutline, RoundedCornerShape(24.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathPoints = listOf(
                        Offset(size.width * 0.14f, size.height * 0.72f),
                        Offset(size.width * 0.28f, size.height * 0.54f),
                        Offset(size.width * 0.42f, size.height * 0.62f),
                        Offset(size.width * 0.56f, size.height * 0.38f),
                        Offset(size.width * 0.72f, size.height * 0.52f),
                        Offset(size.width * 0.84f, size.height * 0.24f)
                    )

                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonBlue.copy(alpha = 0.12f),
                                NeonGreen.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )

                    for (index in 0 until pathPoints.lastIndex) {
                        drawLine(
                            color = NeonBlue,
                            start = pathPoints[index],
                            end = pathPoints[index + 1],
                            strokeWidth = 10f,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.cornerPathEffect(28f)
                        )
                    }

                    pathPoints.forEachIndexed { index, point ->
                        drawCircle(
                            color = if (index == pathPoints.lastIndex) NeonGreen else NeonOrange,
                            radius = if (index == pathPoints.lastIndex) 14f else 10f,
                            center = point
                        )
                    }
                }
            }

            Text(
                text = routePreview.routeName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            BodyText(routePreview.description)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBadge("Distance", routePreview.distanceLabel, NeonBlue, Modifier.weight(1f))
                StatBadge("Duration", routePreview.durationLabel, NeonGreen, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBadge("Calories", routePreview.caloriesLabel, NeonOrange, Modifier.weight(1f))
                StatBadge("Score", routePreview.scoreLabel, NeonBlue, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CheckpointCard(
    checkpoint: RouteCheckpoint,
    modifier: Modifier = Modifier
) {
    SurfacePanel(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = checkpoint.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            BodyText(checkpoint.detail)
            Text(
                text = checkpoint.reward,
                style = MaterialTheme.typography.labelLarge,
                color = NeonOrange
            )
        }
    }
}

@Composable
fun RunStatsRow(
    stats: List<RunStat>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        stats.chunked(2).forEach { rowStats ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowStats.forEach { stat ->
                    StatBadge(
                        label = stat.label,
                        value = stat.value,
                        accent = if (stat.label == "Score") NeonOrange else NeonBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowStats.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MiniLegendRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DotLegend(color = NeonBlue, text = "Route line")
        DotLegend(color = NeonOrange, text = "Coins")
        DotLegend(color = NeonGreen, text = "Finish")
    }
}
