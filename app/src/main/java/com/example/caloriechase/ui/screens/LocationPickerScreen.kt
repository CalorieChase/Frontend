package com.example.caloriechase.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat
import com.example.caloriechase.ui.LocationSuggestion
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

private val DefaultMapPoint = LatLng(37.7793, -122.4193)

@OptIn(ExperimentalMaterial3Api::class)
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
        position = CameraPosition.fromLatLngZoom(initialPoint, 17f)
    }
    var hasLocationPermission by remember { mutableStateOf(hasLocationPermission(context)) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var markerPosition by remember(initialPoint) { mutableStateOf(initialPoint) }
    var pendingLocation by remember { mutableStateOf<LocationSuggestion?>(null) }
    val markerState = rememberUpdatedMarkerState(position = markerPosition)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grantResults ->
        hasLocationPermission = grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grantResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            coroutineScope.launch {
                val currentLocation = getBestLastKnownLocation(context)
                if (currentLocation != null) {
                    val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f)
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            val currentLocation = getBestLastKnownLocation(context)
            if (currentLocation != null) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(currentLocation.latitude, currentLocation.longitude),
                        17f
                    )
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isBuildingEnabled = true,
                isMyLocationEnabled = hasLocationPermission
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = hasLocationPermission,
                compassEnabled = true,
                mapToolbarEnabled = true
            ),
            onMapClick = { latLng ->
                markerPosition = latLng
                coroutineScope.launch {
                    isSearching = true
                    val pickedLocation = reverseGeocodeLocation(context, latLng)
                    isSearching = false
                    pendingLocation = pickedLocation
                }
            }
        ) {
            Marker(
                state = markerState,
                title = pendingLocation?.title ?: "Starting point",
                snippet = pendingLocation?.address
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
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
                            markerPosition = LatLng(searchResult.latitude, searchResult.longitude)
                            pendingLocation = addressToLocationSuggestion(searchResult)
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(searchResult.latitude, searchResult.longitude),
                                    17f
                                )
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
                                    markerPosition = LatLng(searchResult.latitude, searchResult.longitude)
                                    pendingLocation = addressToLocationSuggestion(searchResult)
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(searchResult.latitude, searchResult.longitude),
                                            17f
                                        )
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

        FilledTonalIconButton(
            onClick = {
                if (hasLocationPermission) {
                    coroutineScope.launch {
                        val currentLocation = getBestLastKnownLocation(context)
                        if (currentLocation != null) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(currentLocation.latitude, currentLocation.longitude),
                                    17f
                                )
                            )
                        }
                    }
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 104.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.MyLocation,
                contentDescription = "Move to my location"
            )
        }
    }

    pendingLocation?.let { pickedLocation ->
        ModalBottomSheet(
            onDismissRequest = { pendingLocation = null }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Location picked",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = pickedLocation.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                BodyText(pickedLocation.address)
                Spacer(modifier = Modifier.height(8.dp))
                BodyText(pickedLocation.description)
                Spacer(modifier = Modifier.height(20.dp))
                PrimaryButton(
                    text = "Confirm starting point",
                    onClick = {
                        onSelectLocation(pickedLocation)
                        onConfirm()
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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

private fun addressToLocationSuggestion(address: Address): LocationSuggestion {
    return LocationSuggestion(
        title = address.featureName ?: address.locality ?: "Pinned place",
        address = address.getAddressLine(0) ?: formatCoordinateAddress(LatLng(address.latitude, address.longitude)),
        description = "Picked from the search result.",
        backendQuery = "${address.latitude},${address.longitude}"
    )
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

private fun formatCoordinateTitle(latLng: LatLng): String {
    return String.format(Locale.US, "Pinned point %.4f, %.4f", latLng.latitude, latLng.longitude)
}

private fun formatCoordinateAddress(latLng: LatLng): String {
    return String.format(Locale.US, "%.5f, %.5f", latLng.latitude, latLng.longitude)
}
