package com.example.caloriechase.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import com.example.caloriechase.R
import com.example.caloriechase.ui.RouteCheckpoint
import com.example.caloriechase.ui.RoutePreview
import com.example.caloriechase.ui.RunStat
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.SurfaceOutline
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch

@Composable
fun RouteMapCard(
    routePreview: RoutePreview,
    modifier: Modifier = Modifier
) {
    SurfacePanel(modifier = modifier) {
        val startMarkerIcon = remember {
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        }
        val coinMarkerIcon = rememberBitmapDescriptor(
            drawableResId = R.drawable.coin,
            size = 28.dp
        )
        val finishMarkerIcon = rememberBitmapDescriptor(
            drawableResId = R.drawable.treasure,
            size = 36.dp
        )
        val routePoints = routePreview.routePoints.map { LatLng(it.lat, it.lng) }
        val fallbackPoint = LatLng(37.7793, -122.4193)
        val initialCameraPoint = routePoints.firstOrNull() ?: fallbackPoint
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(initialCameraPoint, 15.5f)
        }
        val coroutineScope = rememberCoroutineScope()
        var isMapLoaded by remember { mutableStateOf(false) }
        val startPoint = routePoints.firstOrNull()
        val endPoint = routePoints.lastOrNull()
        val coinPoints = routePreview.coinSpots.map { LatLng(it.lat, it.lng) }
        val allMapPoints = (routePoints + coinPoints).ifEmpty {
            listOf(fallbackPoint)
        }

        LaunchedEffect(routePreview.routePoints, routePreview.coinSpots, isMapLoaded) {
            if (!isMapLoaded) {
                if (allMapPoints.size == 1) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(allMapPoints.first(), 16f)
                }
                return@LaunchedEffect
            }

            if (allMapPoints.size == 1) {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(allMapPoints.first(), 16f))
            } else {
                val boundsBuilder = LatLngBounds.builder()
                allMapPoints.forEach(boundsBuilder::include)
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 160)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .border(1.dp, SurfaceOutline, RoundedCornerShape(24.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isBuildingEnabled = true),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        compassEnabled = true,
                        mapToolbarEnabled = false
                    ),
                    onMapLoaded = {
                        isMapLoaded = true
                    },
                    onMapClick = { latLng ->
                        if (!isMapLoaded) {
                            return@GoogleMap
                        }

                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLng,
                                    (cameraPositionState.position.zoom + 1.5f).coerceAtMost(19f)
                                )
                            )
                        }
                    }
                ) {
                    if (routePoints.size >= 2) {
                        Polyline(
                            points = routePoints,
                            color = NeonBlue,
                            width = 14f
                        )
                    }

                    startPoint?.let { point ->
                        Marker(
                            state = rememberUpdatedMarkerState(position = point),
                            title = "Start",
                            icon = startMarkerIcon
                        )
                    }
                    endPoint?.let { point ->
                        Marker(
                            state = rememberUpdatedMarkerState(position = point),
                            title = "Finish",
                            icon = finishMarkerIcon
                        )
                    }
                    routePreview.coinSpots.forEachIndexed { index, coin ->
                        Marker(
                            state = rememberUpdatedMarkerState(position = LatLng(coin.lat, coin.lng)),
                            title = "Coin ${index + 1}",
                            snippet = "${coin.value} pts",
                            icon = coinMarkerIcon
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

@Composable
private fun rememberBitmapDescriptor(
    @DrawableRes drawableResId: Int,
    size: Dp
): BitmapDescriptor? {
    val context = LocalContext.current
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }.coerceAtLeast(1)

    return remember(context, drawableResId, sizePx) {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId) ?: return@remember null
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, sizePx, sizePx, true)
        BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }
}
