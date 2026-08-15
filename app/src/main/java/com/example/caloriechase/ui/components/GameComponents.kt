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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.RouteCheckpoint
import com.example.caloriechase.ui.RoutePoint
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
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonBlue.copy(alpha = 0.12f),
                                NeonGreen.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )

                    val routePoints = routePreview.routePoints.ifEmpty {
                        listOf(
                            RoutePoint(37.7793, -122.4193),
                            RoutePoint(37.7814, -122.4178),
                            RoutePoint(37.7842, -122.4187),
                            RoutePoint(37.7861, -122.4202),
                            RoutePoint(37.7842, -122.4187),
                            RoutePoint(37.7814, -122.4178),
                            RoutePoint(37.7793, -122.4193)
                        )
                    }
                    val allPoints = routePoints + routePreview.coinSpots.map { RoutePoint(it.lat, it.lng) }
                    val minLat = allPoints.minOf { it.lat }
                    val maxLat = allPoints.maxOf { it.lat }
                    val minLng = allPoints.minOf { it.lng }
                    val maxLng = allPoints.maxOf { it.lng }
                    val latSpan = (maxLat - minLat).takeIf { it > 0.00001 } ?: 0.00001
                    val lngSpan = (maxLng - minLng).takeIf { it > 0.00001 } ?: 0.00001
                    val horizontalPadding = size.width * 0.12f
                    val verticalPadding = size.height * 0.14f

                    fun project(point: RoutePoint): Offset {
                        val xRatio = ((point.lng - minLng) / lngSpan).toFloat()
                        val yRatio = ((point.lat - minLat) / latSpan).toFloat()
                        val x = horizontalPadding + xRatio * (size.width - horizontalPadding * 2f)
                        val y = size.height - verticalPadding - yRatio * (size.height - verticalPadding * 2f)
                        return Offset(x, y)
                    }

                    val routeOffsets = routePoints.map(::project)
                    val coinOffsets = routePreview.coinSpots.map { project(RoutePoint(it.lat, it.lng)) }

                    for (index in 0 until routeOffsets.lastIndex) {
                        drawLine(
                            color = NeonBlue,
                            start = routeOffsets[index],
                            end = routeOffsets[index + 1],
                            strokeWidth = 10f,
                            cap = StrokeCap.Round
                        )
                    }

                    coinOffsets.forEach { point ->
                        drawCircle(
                            color = NeonOrange,
                            radius = 9f,
                            center = point
                        )
                    }

                    routeOffsets.firstOrNull()?.let { start ->
                        drawCircle(color = NeonBlue, radius = 12f, center = start)
                    }
                    routeOffsets.lastOrNull()?.let { finish ->
                        drawCircle(color = NeonGreen, radius = 14f, center = finish)
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
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatBadge("Route", routePreview.routeTypeLabel, NeonGreen, Modifier.weight(1f))
                StatBadge("Coins", routePreview.coinSpots.size.toString(), NeonOrange, Modifier.weight(1f))
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
                        accent = if (stat.label == "Coins") NeonOrange else NeonBlue,
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
