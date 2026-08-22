package com.example.caloriechase.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.caloriechase.ui.DashboardMetricItem
import com.example.caloriechase.ui.HomeFocus
import com.example.caloriechase.ui.RecentRunItem
import com.example.caloriechase.ui.RoutePoint
import com.example.caloriechase.ui.UserProfile
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.DarkCardElevated
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.SurfaceOutline
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomePlaceholderScreen(
    profile: UserProfile,
    focusCards: List<HomeFocus>,
    modifier: Modifier = Modifier,
    onPlanRoute: () -> Unit
) {
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
                            text = "Good morning, ${profile.name}!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        BodyText("Stay consistent today. Your route, pace, and progress are ready.")
                        Text(
                            text = "${profile.levelTitle} - ${profile.streakDays}-day streak",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonGreen
                        )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                focusCards.forEach { card ->
                    HomeFocusCard(
                        modifier = Modifier.weight(1f),
                        label = card.label,
                        value = card.value,
                        supporting = card.supporting,
                        accent = card.accent,
                        icon = card.icon
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current location",
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
            HomeLocationMapCard()
        }

        item {
            PrimaryButton(text = "Start guided route", onClick = onPlanRoute)
        }
    }
}

@Composable
fun DashboardPlaceholderScreen(
    metrics: List<DashboardMetricItem>,
    recentRuns: List<RecentRunItem>,
    modifier: Modifier = Modifier
) {
    val selectedDay = remember { LocalDate.now().dayOfWeek }
    val updatedDateLabel = remember { formatDashboardDate(LocalDate.now()) }

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
                    Text(
                        text = "Updated for $updatedDateLabel",
                        style = MaterialTheme.typography.labelLarge,
                        color = NeonGreen
                    )
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
            WeeklyRhythmCard(selectedDay = selectedDay)
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
        modifier = modifier.height(170.dp),
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
private fun WeeklyRhythmCard(selectedDay: DayOfWeek) {
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
private fun HomeLocationMapCard() {
    val context = LocalContext.current
    val defaultPoint = remember { RoutePoint(37.7793, -122.4193) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(defaultPoint.lat, defaultPoint.lng), 14f)
    }
    var currentLocation by remember { mutableStateOf<RoutePoint?>(null) }
    var hasLocationPermission by remember { mutableStateOf(hasLocationPermission(context)) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        hasLocationPermission = grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grantResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            onDispose { }
        } else {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val listener = LocationListener { location ->
                currentLocation = RoutePoint(location.latitude, location.longitude)
            }

            if (locationManager != null) {
                getBestLastKnownLocation(context)?.let { lastKnownLocation ->
                    currentLocation = RoutePoint(lastKnownLocation.latitude, lastKnownLocation.longitude)
                }

                runCatching { locationManager.getProviders(true) }.getOrDefault(emptyList()).forEach { provider ->
                    runCatching {
                        locationManager.requestLocationUpdates(
                            provider,
                            5_000L,
                            10f,
                            listener,
                            Looper.getMainLooper()
                        )
                    }
                }
            }

            onDispose {
                if (locationManager != null) {
                    runCatching { locationManager.removeUpdates(listener) }
                }
            }
        }
    }

    LaunchedEffect(currentLocation) {
        val point = currentLocation ?: return@LaunchedEffect
        cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(point.lat, point.lng), 16f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceOutline)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isBuildingEnabled = true),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    mapToolbarEnabled = false,
                    compassEnabled = false
                )
            ) {
                val markerPoint = currentLocation ?: defaultPoint
                Marker(
                    state = rememberUpdatedMarkerState(position = LatLng(markerPoint.lat, markerPoint.lng)),
                    title = "You are here",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniMapChip(
                    icon = Icons.Rounded.Map,
                    text = if (hasLocationPermission) "Current position" else "Location locked"
                )
                MiniMapChip(
                    icon = Icons.Rounded.MyLocation,
                    text = if (currentLocation != null) "GPS ready" else "Waiting"
                )
            }

            if (!hasLocationPermission) {
                SurfacePanel(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enable location to show your live map pin.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        FilledTonalButton(onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }) {
                            Text("Grant access")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniMapChip(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f))
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
    metric: DashboardMetricItem
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
private fun RecentRunCard(run: RecentRunItem) {
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
                    imageVector = Icons.AutoMirrored.Rounded.DirectionsRun,
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

private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun getBestLastKnownLocation(context: Context): Location? {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
    val providers = runCatching { locationManager.getProviders(true) }.getOrDefault(emptyList())
    return providers.mapNotNull { provider ->
        runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
    }.minByOrNull { location -> location.accuracy.takeIf { it > 0f } ?: Float.MAX_VALUE }
}

private fun formatDashboardDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, uuuu", Locale.US))
}
