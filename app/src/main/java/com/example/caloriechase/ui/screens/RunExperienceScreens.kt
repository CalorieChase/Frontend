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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.RunSessionState
import com.example.caloriechase.ui.RunStat
import com.example.caloriechase.ui.components.ActiveRunMapCard
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.RunStatsRow
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SecondaryButton
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import java.util.Locale
import kotlinx.coroutines.delay

private const val ActiveMet = 2.8f
private const val RouteFinishRadiusMeters = 25f

@Composable
fun ActiveRunScreen(
    routePreview: RoutePreview,
    runSession: RunSessionState,
    weightKg: Int,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLocationUpdate: (latitude: Double, longitude: Double) -> Unit,
    onCoinCelebrationShown: () -> Unit,
    onFinishRun: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(hasLocationPermission(context)) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        hasLocationPermission = grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grantResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    val liveStats = remember(runSession, weightKg) {
        buildLiveRunStats(runSession = runSession, weightKg = weightKg)
    }
    val targetDistanceMeters = (routePreview.totalDistanceKm * 1_000.0).toFloat()
    val distanceProgress = if (targetDistanceMeters <= 0f) {
        0f
    } else {
        (runSession.distanceMeters / targetDistanceMeters).coerceIn(0f, 1f)
    }
    val coinProgress = if (routePreview.coinSpots.isEmpty()) {
        0f
    } else {
        runSession.collectedCoinIndices.size.toFloat() / routePreview.coinSpots.size.toFloat()
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

    LaunchedEffect(runSession.lastCollectedCoinValue) {
        if (runSession.lastCollectedCoinValue == null) {
            return@LaunchedEffect
        }

        delay(1_500)
        onCoinCelebrationShown()
    }

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            onDispose { }
        } else {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val listener = LocationListener { location ->
                onLocationUpdate(location.latitude, location.longitude)
            }

            if (locationManager != null) {
                getBestLastKnownLocation(context)?.let { lastKnownLocation ->
                    onLocationUpdate(lastKnownLocation.latitude, lastKnownLocation.longitude)
                }

                runCatching {
                    locationManager.getProviders(true)
                }.getOrDefault(emptyList()).forEach { provider ->
                    runCatching {
                        locationManager.requestLocationUpdates(
                            provider,
                            1_500L,
                            0f,
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

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Live route session",
            subtitle = "Move along the route, get close to coins, and finish with a real session summary.",
            onBack = onBack
        )

        ActiveRunMapCard(
            routePreview = routePreview,
            currentLocation = runSession.currentLocation,
            collectedCoinIndices = runSession.collectedCoinIndices
        )

        if (!hasLocationPermission) {
            SurfacePanel(emphasized = true) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Location access needed",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText("CalorieChase needs your location during the run to track distance and collect nearby coins automatically.")
                    PrimaryButton(
                        text = "Grant location access",
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )
                }
            }
        }

        runSession.lastCollectedCoinValue?.let { collectedValue ->
            SurfacePanel(emphasized = true) {
                Text(
                    text = "+$collectedValue pts coin collected",
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonGreen
                )
            }
        }

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Live progress",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                RunStatsRow(stats = liveStats)
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Mission progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                ProgressMetric(
                    label = "Distance covered",
                    value = "${formatDistanceLabel(runSession.distanceMeters)} of ${routePreview.distanceLabel}",
                    progress = distanceProgress
                )
                ProgressMetric(
                    label = "Coins collected",
                    value = "${runSession.collectedCoinIndices.size} of ${routePreview.coinSpots.size}",
                    progress = coinProgress
                )
                BodyText(finishStatusLine(runSession.finishDistanceMeters))
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Route checkpoints",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                routePreview.checkpoints.forEachIndexed { index, checkpoint ->
                    BodyText("${index + 1}. ${checkpoint.title} - ${checkpoint.reward}")
                }
            }
        }

        PrimaryButton(text = "Finish session", onClick = onFinishRun)
    }
}

@Composable
fun RunSummaryScreen(
    routePreview: RoutePreview,
    runSession: RunSessionState,
    weightKg: Int,
    modifier: Modifier = Modifier,
    onBackHome: () -> Unit,
    onTryAnother: () -> Unit
) {
    val summaryStats = remember(runSession, weightKg) {
        buildSummaryStats(runSession = runSession, weightKg = weightKg, routePreview = routePreview)
    }
    val collectedCoins = runSession.collectedCoinIndices.size
    val didReachFinish = runSession.finishDistanceMeters?.let { it <= RouteFinishRadiusMeters } == true

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Session summary",
            subtitle = "Your live route session is complete.",
            onBack = null
        )

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = routePreview.routeName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText(
                    if (didReachFinish) {
                        "You closed the route near the finish marker and locked in the score you collected on the map."
                    } else {
                        "You ended the session before reaching the finish marker, but your live distance, calories, and score were still recorded."
                    }
                )
            }
        }

        RunStatsRow(stats = summaryStats)

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Highlights",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                SummaryLine("Covered ${formatDistanceLabel(runSession.distanceMeters)} across a ${routePreview.routeTypeLabel.lowercase(Locale.US)} mission.")
                SummaryLine("Collected $collectedCoins of ${routePreview.coinSpots.size} route coins for ${runSession.score} pts.")
                SummaryLine("Burned an estimated ${formatCaloriesLabel(estimateActiveCalories(runSession.elapsedMillis, weightKg))} during ${formatElapsedTime(runSession.elapsedMillis)} of movement.")
                Text(
                    text = if (didReachFinish) {
                        "Finish chest unlocked: ${runSession.score} pts"
                    } else {
                        "Session reward saved: ${runSession.score} pts"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonGreen
                )
            }
        }

        PrimaryButton(text = "Back to home", onClick = onBackHome)
        SecondaryButton(text = "Plan another route", onClick = onTryAnother)
    }
}

@Composable
private fun ProgressMetric(
    label: String,
    value: String,
    progress: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = NeonOrange
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = NeonGreen,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun SummaryLine(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("-", color = NeonGreen)
        BodyText(text)
    }
}

private fun buildLiveRunStats(
    runSession: RunSessionState,
    weightKg: Int
): List<RunStat> {
    return listOf(
        RunStat("Time", formatElapsedTime(runSession.elapsedMillis)),
        RunStat("Distance", formatDistanceLabel(runSession.distanceMeters)),
        RunStat("Coins", runSession.collectedCoinIndices.size.toString()),
        RunStat("Score", "${runSession.score} pts"),
        RunStat("Calories", formatCaloriesLabel(estimateActiveCalories(runSession.elapsedMillis, weightKg))),
        RunStat("Finish", formatFinishDistance(runSession.finishDistanceMeters))
    )
}

private fun buildSummaryStats(
    runSession: RunSessionState,
    weightKg: Int,
    routePreview: RoutePreview
): List<RunStat> {
    return listOf(
        RunStat("Time", formatElapsedTime(runSession.elapsedMillis)),
        RunStat("Distance", formatDistanceLabel(runSession.distanceMeters)),
        RunStat("Coins", "${runSession.collectedCoinIndices.size}/${routePreview.coinSpots.size}"),
        RunStat("Score", "${runSession.score} pts"),
        RunStat("Calories", formatCaloriesLabel(estimateActiveCalories(runSession.elapsedMillis, weightKg))),
        RunStat("Target", routePreview.distanceLabel)
    )
}

private fun estimateActiveCalories(elapsedMillis: Long, weightKg: Int): Int {
    val hours = elapsedMillis / 3_600_000f
    return (ActiveMet * weightKg * hours).toInt()
}

private fun formatElapsedTime(elapsedMillis: Long): String {
    val totalSeconds = (elapsedMillis / 1_000L).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}

private fun formatDistanceLabel(distanceMeters: Float): String {
    return String.format(Locale.US, "%.2f km", distanceMeters / 1_000f)
}

private fun formatCaloriesLabel(calories: Int): String {
    return "$calories kcal"
}

private fun formatFinishDistance(finishDistanceMeters: Float?): String {
    if (finishDistanceMeters == null) {
        return "Unknown"
    }
    if (finishDistanceMeters < 1_000f) {
        return "${finishDistanceMeters.toInt()} m"
    }
    return String.format(Locale.US, "%.1f km", finishDistanceMeters / 1_000f)
}

private fun finishStatusLine(finishDistanceMeters: Float?): String {
    if (finishDistanceMeters == null) {
        return "Waiting for a live position update to measure distance to the finish point."
    }
    if (finishDistanceMeters <= RouteFinishRadiusMeters) {
        return "You are inside the finish zone. Wrap up the session when you are ready."
    }
    return "You are ${formatFinishDistance(finishDistanceMeters)} away from the route finish marker."
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