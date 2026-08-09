package com.example.caloriechase.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Toll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.DarkCardElevated
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonRed
import com.example.caloriechase.ui.theme.SurfaceOutline
import java.time.DayOfWeek
import java.time.LocalDate

private data class DashboardMetric(
    val label: String,
    val value: String,
    val accent: Color,
    val icon: ImageVector
)

private data class RecentRun(
    val title: String,
    val date: String,
    val distance: String,
    val score: String
)

@Composable
fun HomePlaceholderScreen(modifier: Modifier = Modifier) {
    val selectedDay = remember { LocalDate.now().dayOfWeek }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SurfacePanel(emphasized = true) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonBlue
                        )
                        Text(
                            text = "Good morning, David!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText("Stay consistent today. Your route, pace, and progress are ready.")
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = NeonOrange
                        )
                    }
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HomeFocusCard(
                    modifier = Modifier.weight(1f),
                    label = "Live steps",
                    value = "8,421",
                    supporting = "Keep moving to build your streak.",
                    accent = NeonGreen,
                    icon = Icons.Rounded.DirectionsWalk
                )
                HomeFocusCard(
                    modifier = Modifier.weight(1f),
                    label = "Focus",
                    value = "Route run",
                    supporting = "Start a guided run and collect points on the way.",
                    accent = NeonOrange,
                    icon = Icons.Rounded.Bolt
                )
            }
        }

        item {
            SurfacePanel {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Weekly rhythm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText("Use the week at a glance to stay intentional with your training.")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val days = listOf(
                            DayOfWeek.MONDAY to "Mon",
                            DayOfWeek.TUESDAY to "Tue",
                            DayOfWeek.WEDNESDAY to "Wed",
                            DayOfWeek.THURSDAY to "Thu",
                            DayOfWeek.FRIDAY to "Fri",
                            DayOfWeek.SATURDAY to "Sat",
                            DayOfWeek.SUNDAY to "Sun"
                        )
                        days.forEach { (day, label) ->
                            DayChip(
                                modifier = Modifier.weight(1f),
                                label = label,
                                selected = day == selectedDay
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Route preview",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Live map",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonBlue
                )
            }
        }

        item {
            RoutePreviewCard()
        }

        item {
            PrimaryButton(text = "Start guided route", onClick = {})
        }

        item {
            SecondaryButton(text = "Try 3D map preview", onClick = {})
        }
    }
}

@Composable
fun DashboardPlaceholderScreen(modifier: Modifier = Modifier) {
    val metrics = listOf(
        DashboardMetric("Distance covered", "24.8 km", NeonBlue, Icons.Rounded.Route),
        DashboardMetric("Coins earned", "980", NeonOrange, Icons.Rounded.Toll),
        DashboardMetric("Calories burned", "1,420 kcal", NeonRed, Icons.Rounded.LocalFireDepartment),
        DashboardMetric("Runs completed", "12", NeonGreen, Icons.Rounded.DirectionsRun)
    )
    val recentRuns = listOf(
        RecentRun("Morning Run", "Aug 8, 2026", "5.2 km", "450 pts"),
        RecentRun("Treasure Route", "Aug 6, 2026", "7.1 km", "620 pts"),
        RecentRun("Sunset Sprint", "Aug 4, 2026", "4.3 km", "390 pts")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SurfacePanel(emphasized = true) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Performance overview",
                        style = MaterialTheme.typography.labelLarge,
                        color = NeonBlue
                    )
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText("Track how your recent runs are stacking up across distance, rewards, calories, and consistency.")
                }
            }
        }

        items(metrics.chunked(2)) { rowMetrics ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowMetrics.forEach { metric ->
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        metric = metric
                    )
                }
                if (rowMetrics.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent runs",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Last ${recentRuns.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonBlue
                )
            }
        }

        items(recentRuns) { run ->
            RecentRunCard(run = run)
        }
    }
}

@Composable
private fun HomeFocusCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    supporting: String,
    accent: Color,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceOutline)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(20.dp))
                Text(text = label, style = MaterialTheme.typography.labelLarge, color = accent)
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            BodyText(supporting)
        }
    }
}

@Composable
private fun DayChip(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean
) {
    val accent = if (selected) NeonGreen else MaterialTheme.colorScheme.onBackground
    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) NeonGreen.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 1.dp,
                color = if (selected) NeonGreen.copy(alpha = 0.5f) else SurfaceOutline,
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal),
                color = accent
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(NeonGreen, CircleShape)
            )
        }
    }
}

@Composable
private fun RoutePreviewCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceOutline)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pathPoints = listOf(
                    Offset(size.width * 0.18f, size.height * 0.72f),
                    Offset(size.width * 0.32f, size.height * 0.55f),
                    Offset(size.width * 0.44f, size.height * 0.62f),
                    Offset(size.width * 0.58f, size.height * 0.36f),
                    Offset(size.width * 0.72f, size.height * 0.48f),
                    Offset(size.width * 0.84f, size.height * 0.24f)
                )
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeonBlue.copy(alpha = 0.08f),
                            NeonGreen.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
                for (index in 0 until pathPoints.lastIndex) {
                    drawLine(
                        color = NeonBlue,
                        start = pathPoints[index],
                        end = pathPoints[index + 1],
                        strokeWidth = 8f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.cornerPathEffect(28f)
                    )
                }
                pathPoints.forEachIndexed { index, offset ->
                    drawCircle(
                        color = if (index == pathPoints.lastIndex) NeonGreen else NeonOrange,
                        radius = if (index == pathPoints.lastIndex) 14f else 10f,
                        center = offset
                    )
                }
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(size.width * 0.12f, size.height * 0.2f),
                    end = Offset(size.width * 0.86f, size.height * 0.2f),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(size.width * 0.16f, size.height * 0.78f),
                    end = Offset(size.width * 0.78f, size.height * 0.78f),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoutePreviewChip(icon = Icons.Rounded.Map, text = "5.2 km")
                    RoutePreviewChip(icon = Icons.Rounded.CalendarMonth, text = "24 min")
                }
                SurfacePanel {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Canal Loop",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            BodyText("Balanced pace with two reward checkpoints.")
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "420 pts",
                                style = MaterialTheme.typography.titleMedium,
                                color = NeonOrange
                            )
                            Text(
                                text = "Live ready",
                                style = MaterialTheme.typography.labelLarge,
                                color = NeonGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutePreviewChip(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.86f))
            .border(1.dp, SurfaceOutline, RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(18.dp))
        Text(text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    metric: DashboardMetric
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, metric.accent.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = metric.icon,
                contentDescription = metric.label,
                tint = metric.accent,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = metric.value,
                style = MaterialTheme.typography.titleLarge,
                color = metric.accent
            )
            BodyText(metric.label)
        }
    }
}

@Composable
private fun RecentRunCard(run: RecentRun) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceOutline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonGreen.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.DirectionsRun,
                    contentDescription = null,
                    tint = NeonGreen
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = run.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = run.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = run.distance,
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonBlue
                )
                Text(
                    text = run.score,
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeonOrange
                )
            }
        }
    }
}
