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
import com.example.caloriechase.ui.GoalTypeUi
import com.example.caloriechase.ui.LocationSuggestion
import com.example.caloriechase.ui.RoutePreset
import com.example.caloriechase.ui.RouteTypeUi
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SelectableChip
import com.example.caloriechase.ui.components.StatBadge
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonRed
import kotlin.math.roundToInt

@Composable
fun RoutePlannerScreen(
    presets: List<RoutePreset>,
    selectedGoalType: GoalTypeUi,
    selectedGoalValue: Float,
    selectedRouteType: RouteTypeUi,
    selectedLocation: LocationSuggestion,
    weightKg: Int,
    isGeneratingRoute: Boolean,
    routeErrorMessage: String?,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOpenLocationPicker: () -> Unit,
    onSelectGoalType: (GoalTypeUi) -> Unit,
    onGoalValueChange: (Float) -> Unit,
    onSelectRouteType: (RouteTypeUi) -> Unit,
    onDismissError: () -> Unit,
    onApplyPreset: (RoutePreset) -> Unit,
    onGenerateRoute: () -> Unit
) {
    val sliderRange = if (selectedGoalType == GoalTypeUi.Distance) 1.5f..8.0f else 80f..420f
    val sliderSteps = if (selectedGoalType == GoalTypeUi.Distance) 12 else 16
    val goalLabel = if (selectedGoalType == GoalTypeUi.Distance) {
        String.format("%.1f km", selectedGoalValue)
    } else {
        "${selectedGoalValue.roundToInt()} kcal"
    }
    val goalSupport = if (selectedGoalType == GoalTypeUi.Distance) {
        "The backend uses this distance directly for route generation."
    } else {
        "The backend converts this calorie target into a walking distance before routing."
    }

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Build your next route",
            subtitle = "Choose a real starting point, a route style, and a goal the backend can generate right now.",
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
                BodyText(selectedLocation.description)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBadge("Weight", "$weightKg kg", NeonGreen, Modifier.weight(1f))
                    StatBadge("Style", selectedRouteType.label, NeonBlue, Modifier.weight(1f))
                }
                PrimaryButton(text = "Change location", onClick = onOpenLocationPicker)
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Goal type",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalTypeUi.entries.forEach { goalType ->
                        SelectableChip(
                            text = goalType.label,
                            selected = goalType == selectedGoalType,
                            onClick = { onSelectGoalType(goalType) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                BodyText(goalSupport)
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Target",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = goalLabel,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = selectedGoalValue.coerceIn(sliderRange.start, sliderRange.endInclusive),
                    onValueChange = onGoalValueChange,
                    valueRange = sliderRange,
                    steps = sliderSteps
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBadge(
                        "Low",
                        if (selectedGoalType == GoalTypeUi.Distance) "1.5 km" else "80 kcal",
                        NeonOrange,
                        Modifier.weight(1f)
                    )
                    StatBadge(
                        "High",
                        if (selectedGoalType == GoalTypeUi.Distance) "8.0 km" else "420 kcal",
                        NeonBlue,
                        Modifier.weight(1f)
                    )
                }
            }
        }

        SurfacePanel {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Route type",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RouteTypeUi.entries.forEach { routeType ->
                        SelectableChip(
                            text = routeType.label,
                            selected = routeType == selectedRouteType,
                            onClick = { onSelectRouteType(routeType) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                BodyText(
                    when (selectedRouteType) {
                        RouteTypeUi.Auto -> "Let the backend compare loop and out-and-back options near your target."
                        RouteTypeUi.Loop -> "Start and finish at the same point with a closed route."
                        RouteTypeUi.Turnaround -> "Walk outward for about half the target and return along the path."
                    }
                )
            }
        }

        if (routeErrorMessage != null) {
            SurfacePanel(emphasized = true) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Route generation issue",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeonRed
                    )
                    BodyText(routeErrorMessage)
                    PrimaryButton(text = "Dismiss", onClick = onDismissError)
                }
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

        PrimaryButton(
            text = if (isGeneratingRoute) "Generating live route..." else "Generate live route",
            enabled = !isGeneratingRoute,
            onClick = onGenerateRoute
        )
    }
}
