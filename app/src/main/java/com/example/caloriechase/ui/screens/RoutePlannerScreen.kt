package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.LocationSuggestion
import com.example.caloriechase.ui.RoutePreset
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.CalorieTextField
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SelectableChip
import com.example.caloriechase.ui.components.StatBadge
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonOrange

@Composable
fun RoutePlannerScreen(
    presets: List<RoutePreset>,
    selectedActivity: String,
    selectedDistanceKm: Float,
    selectedPrompt: String,
    selectedLocation: LocationSuggestion,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOpenLocationPicker: () -> Unit,
    onSelectActivity: (String) -> Unit,
    onDistanceChange: (Float) -> Unit,
    onPromptChange: (String) -> Unit,
    onApplyPreset: (RoutePreset) -> Unit,
    onGenerateRoute: () -> Unit
) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Build your next route",
            subtitle = "This mirrors the Java flow with placeholder generation instead of backend requests.",
            onBack = onBack
        )

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Starting point",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText(selectedLocation.title)
                BodyText(selectedLocation.address)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBadge("Mode", selectedActivity, NeonOrange, Modifier.weight(1f))
                    StatBadge("Goal", String.format("%.1f km", selectedDistanceKm), NeonBlue, Modifier.weight(1f))
                }
                PrimaryButton(text = "Change location", onClick = onOpenLocationPicker)
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Activity type",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("Walk", "Jog", "Run").forEach { activity ->
                        SelectableChip(
                            text = activity,
                            selected = activity == selectedActivity,
                            onClick = { onSelectActivity(activity) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Target distance",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = String.format("%.1f km", selectedDistanceKm),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = selectedDistanceKm,
                    onValueChange = onDistanceChange,
                    valueRange = 1.5f..10f
                )
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Route brief",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                CalorieTextField(
                    value = selectedPrompt,
                    onValueChange = onPromptChange,
                    label = "Describe the kind of route you want"
                )
            }
        }

        presets.forEach { preset ->
            SurfacePanel {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = preset.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText(preset.supporting)
                    PrimaryButton(
                        text = "Use ${String.format("%.1f", preset.distanceKm)} km preset",
                        onClick = { onApplyPreset(preset) }
                    )
                }
            }
        }

        PrimaryButton(text = "Generate placeholder route", onClick = onGenerateRoute)
    }
}
