package com.example.caloriechase.ui.screens

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.LocationSuggestion
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

private val DefaultMapPoint = LatLng(37.7793, -122.4193)

@Composable
fun LocationPickerScreen(
    selectedLocation: LocationSuggestion,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSelectLocation: (LocationSuggestion) -> Unit,
    onConfirm: () -> Unit
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val initialPoint = remember(selectedLocation.backendQuery) {
        parseLatLng(selectedLocation.backendQuery) ?: DefaultMapPoint
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPoint, 15f)
    }
    val markerState = rememberMarkerState(position = initialPoint)
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isBuildingEnabled = true),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true
            ),
            onMapClick = { latLng ->
                markerState.position = latLng
                coroutineScope.launch {
                    isSearching = true
                    val pickedLocation = reverseGeocodeLocation(context, latLng)
                    isSearching = false
                    onSelectLocation(pickedLocation)
                    onConfirm()
                }
            }
        ) {
            Marker(
                state = markerState,
                title = "Starting point"
            )
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = { Text("Search for a place or address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        searchQuery = searchQuery.trim()
                        if (searchQuery.isBlank()) {
                            return@launch
                        }

                        isSearching = true
                        val searchResult = geocodeLocationName(context, searchQuery)
                        isSearching = false
                        if (searchResult != null) {
                            markerState.position = LatLng(searchResult.latitude, searchResult.longitude)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(markerState.position, 16f)
                            )
                        }
                    }
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (isSearching) {
                    CircularProgressIndicator(modifier = Modifier.padding(12.dp))
                } else {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                searchQuery = searchQuery.trim()
                                if (searchQuery.isBlank()) {
                                    return@launch
                                }

                                isSearching = true
                                val searchResult = geocodeLocationName(context, searchQuery)
                                isSearching = false
                                if (searchResult != null) {
                                    markerState.position = LatLng(searchResult.latitude, searchResult.longitude)
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(markerState.position, 16f)
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search place"
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

private fun parseLatLng(value: String): LatLng? {
    val parts = value.split(",").map(String::trim)
    if (parts.size != 2) {
        return null
    }

    val lat = parts[0].toDoubleOrNull()
    val lng = parts[1].toDoubleOrNull()
    if (lat == null || lng == null) {
        return null
    }

    return LatLng(lat, lng)
}

private suspend fun geocodeLocationName(
    context: android.content.Context,
    query: String
): Address? {
    val geocoder = Geocoder(context, Locale.getDefault())
    if (!Geocoder.isPresent()) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocationName(query, 1) { addresses ->
                continuation.resume(addresses.firstOrNull())
            }
        }
    } else {
        withContext(Dispatchers.IO) {
            @Suppress("DEPRECATION")
            geocoder.getFromLocationName(query, 1)?.firstOrNull()
        }
    }
}

private suspend fun reverseGeocodeLocation(
    context: android.content.Context,
    latLng: LatLng
): LocationSuggestion {
    val geocoder = Geocoder(context, Locale.getDefault())
    val address = if (Geocoder.isPresent()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addresses ->
                    continuation.resume(addresses.firstOrNull())
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()
            }
        }
    } else {
        null
    }

    return LocationSuggestion(
        title = address?.featureName ?: formatCoordinateTitle(latLng),
        address = address?.getAddressLine(0) ?: formatCoordinateAddress(latLng),
        description = "Pinned from the live map.",
        backendQuery = "${latLng.latitude},${latLng.longitude}"
    )
}

private fun formatCoordinateTitle(latLng: LatLng): String {
    return String.format(Locale.US, "Pinned point %.4f, %.4f", latLng.latitude, latLng.longitude)
}

private fun formatCoordinateAddress(latLng: LatLng): String {
    return String.format(Locale.US, "%.5f, %.5f", latLng.latitude, latLng.longitude)
}
