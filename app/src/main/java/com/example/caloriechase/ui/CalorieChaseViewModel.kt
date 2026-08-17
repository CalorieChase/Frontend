package com.example.caloriechase.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Toll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriechase.data.route.RouteRepository
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonRed
import java.io.IOException
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import retrofit2.HttpException

private const val WalkingSpeedKmPerHour = 4.8f
private const val ActiveMet = 2.8f

data class AppUiState(
    val profile: UserProfile = UserProfile(
        name = "David",
        email = "david.runner@example.com",
        heightCm = 178,
        weightKg = 75,
        levelTitle = "Treasure sprinter",
        streakDays = 6
    ),
    val selectedGoalType: GoalTypeUi = GoalTypeUi.Distance,
    val selectedGoalValue: Float = 3.2f,
    val selectedRouteType: RouteTypeUi = RouteTypeUi.Auto,
    val selectedLocation: LocationSuggestion = LocationSuggestion(
        title = "Civic Center Launch Pad",
        address = "San Francisco City Hall, 1 Dr Carlton B Goodlett Pl",
        description = "Dense downtown grid that works well for adaptive and out-and-back treasure routes.",
        backendQuery = "37.7793,-122.4193"
    ),
    val activeRoute: RoutePreview = RoutePreview(
        routeName = "Adaptive Treasure Route",
        activityType = "Walk",
        distanceLabel = "3.2 km",
        durationLabel = "40 min",
        caloriesLabel = "140 kcal",
        scoreLabel = "220 pts",
        description = "A guided walking mission with a steady city pace and coin clusters spaced across the route.",
        checkpoints = listOf(
            RouteCheckpoint("Opening coin lane", "Ease into the route and collect the first cluster.", "40 pts"),
            RouteCheckpoint("Mid-route vault", "The densest reward segment sits near the middle stretch.", "80 pts"),
            RouteCheckpoint("Finish reward line", "Close the route with the last coin sweep into the finish.", "100 pts")
        ),
        routePoints = listOf(
            RoutePoint(37.7793, -122.4193),
            RoutePoint(37.7814, -122.4178),
            RoutePoint(37.7842, -122.4187),
            RoutePoint(37.7861, -122.4202),
            RoutePoint(37.7842, -122.4187),
            RoutePoint(37.7814, -122.4178),
            RoutePoint(37.7793, -122.4193)
        ),
        coinSpots = listOf(
            CoinSpot(37.7812, -122.4180, 20),
            CoinSpot(37.7840, -122.4188, 50),
            CoinSpot(37.7857, -122.4200, 20)
        ),
        routeTypeLabel = "AUTO",
        routePolyline = ""
    ),
    val runStats: List<RunStat> = listOf(
        RunStat("Distance", "3.2 km"),
        RunStat("Coins", "3"),
        RunStat("Calories", "140 kcal"),
        RunStat("Route", "AUTO")
    ),
    val isGeneratingRoute: Boolean = false,
    val routeErrorMessage: String? = null,
    val homeFocus: List<HomeFocus> = listOf(
        HomeFocus("Live steps", "8,421", "Keep moving to build your streak.", NeonGreen, Icons.Rounded.DirectionsWalk),
        HomeFocus("Focus", "Route run", "Start a guided run and collect points on the way.", NeonOrange, Icons.Rounded.Bolt)
    ),
    val dashboardMetrics: List<DashboardMetricItem> = listOf(
        DashboardMetricItem("Distance covered", "24.8 km", NeonBlue, Icons.Rounded.Route),
        DashboardMetricItem("Coins earned", "980", NeonOrange, Icons.Rounded.Toll),
        DashboardMetricItem("Calories burned", "1,420 kcal", NeonRed, Icons.Rounded.LocalFireDepartment),
        DashboardMetricItem("Routes completed", "12", NeonGreen, Icons.Rounded.DirectionsWalk)
    ),
    val recentRuns: List<RecentRunItem> = listOf(
        RecentRunItem("Morning Route", "Aug 8, 2026", "5.2 km", "450 pts"),
        RecentRunItem("Treasure Route", "Aug 6, 2026", "7.1 km", "620 pts"),
        RecentRunItem("Sunset Mission", "Aug 4, 2026", "4.3 km", "390 pts")
    ),
    val treasures: List<TreasureSpot> = listOf(
        TreasureSpot("Riverside Lantern", "Evening loop collectible", "120 pts", "0.8 km away"),
        TreasureSpot("Market Dash", "Quick urban sprint reward", "90 pts", "1.4 km away"),
        TreasureSpot("Bridge Crown", "Rare weekend checkpoint", "240 pts", "2.1 km away")
    ),
    val progressHighlights: List<ProgressHighlight> = listOf(
        ProgressHighlight("Weekly distance", "18.6 km", "3.4 km ahead of last week"),
        ProgressHighlight("Calories this week", "1,140 kcal", "Strong consistency across four sessions"),
        ProgressHighlight("Treasure completion", "82%", "Two high-value checkpoints left to unlock")
    ),
    val achievements: List<AchievementItem> = listOf(
        AchievementItem("Six-day streak", "You have completed six active days in a row.", true),
        AchievementItem("Bridge collector", "Collect every coin on one route preview.", true),
        AchievementItem("Night route master", "Finish three evening routes this week.", false)
    ),
    val locationSuggestions: List<LocationSuggestion> = listOf(
        LocationSuggestion(
            "Civic Center Launch Pad",
            "San Francisco City Hall, 1 Dr Carlton B Goodlett Pl",
            "Reliable downtown start for adaptive missions and quick loops.",
            "37.7793,-122.4193"
        ),
        LocationSuggestion(
            "Ferry Building Start",
            "1 Ferry Building, San Francisco",
            "Open waterfront sight lines with great straight-line walking options.",
            "37.7955,-122.3937"
        ),
        LocationSuggestion(
            "Mission Dolores Base",
            "Dolores Park, San Francisco",
            "Flexible neighborhood streets that support both loop and turnaround routing.",
            "37.7596,-122.4269"
        )
    ),
    val coachTips: List<CoachTip> = listOf(
        CoachTip("Route coach", "Adaptive routes are best when you want the backend to find the cleanest match."),
        CoachTip("Goal tip", "Active calories is useful when you want the planner to back-calculate distance automatically."),
        CoachTip("Device tip", "Use the Android emulator with the backend on your computer for the smoothest first test.")
    )
)

class CalorieChaseViewModel(
    private val routeRepository: RouteRepository = RouteRepository()
) : ViewModel() {
    var uiState by mutableStateOf(AppUiState())
        private set

    fun updateBiometrics(heightCm: Int, weightKg: Int) {
        uiState = uiState.copy(
            profile = uiState.profile.copy(heightCm = heightCm, weightKg = weightKg)
        )
    }

    fun selectGoalType(goalType: GoalTypeUi) {
        if (goalType == uiState.selectedGoalType) {
            return
        }

        val convertedValue = when (goalType) {
            GoalTypeUi.Distance -> activeCaloriesToDistance(uiState.selectedGoalValue, uiState.profile.weightKg)
            GoalTypeUi.ActiveCalories -> distanceToActiveCalories(uiState.selectedGoalValue, uiState.profile.weightKg)
        }

        uiState = uiState.copy(
            selectedGoalType = goalType,
            selectedGoalValue = convertedValue
        )
    }

    fun selectGoalValue(goalValue: Float) {
        uiState = uiState.copy(selectedGoalValue = goalValue)
    }

    fun selectRouteType(routeType: RouteTypeUi) {
        uiState = uiState.copy(selectedRouteType = routeType)
    }

    fun selectLocation(locationSuggestion: LocationSuggestion) {
        uiState = uiState.copy(selectedLocation = locationSuggestion)
    }

    fun clearRouteError() {
        uiState = uiState.copy(routeErrorMessage = null)
    }

    fun generateRoute(onSuccess: () -> Unit) {
        if (uiState.isGeneratingRoute) {
            return
        }

        val request = RoutePlannerRequest(
            startingLocation = uiState.selectedLocation.backendQuery,
            weightKg = uiState.profile.weightKg,
            goalType = uiState.selectedGoalType,
            goalValue = uiState.selectedGoalValue,
            routeType = uiState.selectedRouteType
        )

        uiState = uiState.copy(isGeneratingRoute = true, routeErrorMessage = null)

        viewModelScope.launch {
            try {
                val routePreview = routeRepository.generateRoute(request)
                uiState = uiState.copy(
                    isGeneratingRoute = false,
                    activeRoute = routePreview,
                    runStats = buildRunStats(routePreview),
                    recentRuns = listOf(
                        RecentRunItem(
                            title = routePreview.routeName,
                            date = "Aug 15, 2026",
                            distance = routePreview.distanceLabel,
                            score = routePreview.scoreLabel
                        )
                    ) + uiState.recentRuns.take(2)
                )
                onSuccess()
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    isGeneratingRoute = false,
                    routeErrorMessage = when (exception) {
                        is HttpException -> "The backend rejected the route request. Please try a different goal or route type."
                        is IOException -> "The app could not reach the backend. Make sure FastAPI is running and the emulator can access 10.0.2.2:8000."
                        else -> "Route generation failed unexpectedly. Please try again."
                    }
                )
            }
        }
    }

    private fun buildRunStats(routePreview: RoutePreview): List<RunStat> {
        return listOf(
            RunStat("Distance", routePreview.distanceLabel),
            RunStat("Coins", routePreview.coinSpots.size.toString()),
            RunStat("Calories", routePreview.caloriesLabel),
            RunStat("Route", routePreview.routeTypeLabel)
        )
    }

    private fun distanceToActiveCalories(distanceKm: Float, weightKg: Int): Float {
        val timeHours = distanceKm / WalkingSpeedKmPerHour
        return ActiveMet * weightKg * timeHours
    }

    private fun activeCaloriesToDistance(activeCalories: Float, weightKg: Int): Float {
        val timeHours = activeCalories / (ActiveMet * weightKg)
        return timeHours * WalkingSpeedKmPerHour
    }
}
