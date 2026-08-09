package com.example.caloriechase.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Toll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange
import com.example.caloriechase.ui.theme.NeonRed

data class AppUiState(
    val profile: UserProfile = UserProfile(
        name = "David",
        email = "david.runner@example.com",
        heightCm = 178,
        weightKg = 75,
        levelTitle = "Treasure sprinter",
        streakDays = 6
    ),
    val selectedActivity: String = "Run",
    val selectedDistanceKm: Float = 5.2f,
    val selectedPrompt: String = "Scenic route with two treasure checkpoints and a steady pace.",
    val selectedLocation: LocationSuggestion = LocationSuggestion(
        title = "Canal South Gate",
        address = "West Canal Walk, Riverside District",
        description = "Flat terrain with strong lighting and space for cooldown laps."
    ),
    val activeRoute: RoutePreview = RoutePreview(
        routeName = "Canal Loop",
        activityType = "Run",
        distanceLabel = "5.2 km",
        durationLabel = "24 min",
        caloriesLabel = "420 kcal",
        scoreLabel = "420 pts",
        description = "A balanced route with one bridge climb, river views, and two reward clusters.",
        checkpoints = listOf(
            RouteCheckpoint("Warm-up stretch", "Easy opening pace through the plaza.", "60 pts"),
            RouteCheckpoint("Bridge sprint", "Short incline with a speed burst segment.", "150 pts"),
            RouteCheckpoint("Treasure finish", "Final push into the canal gate finish zone.", "210 pts")
        )
    ),
    val runStats: List<RunStat> = listOf(
        RunStat("Distance", "3.6 km"),
        RunStat("Score", "280 pts"),
        RunStat("Steps", "4,812"),
        RunStat("Time", "18:42")
    ),
    val homeFocus: List<HomeFocus> = listOf(
        HomeFocus("Live steps", "8,421", "Keep moving to build your streak.", NeonGreen, Icons.Rounded.DirectionsWalk),
        HomeFocus("Focus", "Route run", "Start a guided run and collect points on the way.", NeonOrange, Icons.Rounded.Bolt)
    ),
    val dashboardMetrics: List<DashboardMetricItem> = listOf(
        DashboardMetricItem("Distance covered", "24.8 km", NeonBlue, Icons.Rounded.Route),
        DashboardMetricItem("Coins earned", "980", NeonOrange, Icons.Rounded.Toll),
        DashboardMetricItem("Calories burned", "1,420 kcal", NeonRed, Icons.Rounded.LocalFireDepartment),
        DashboardMetricItem("Runs completed", "12", NeonGreen, Icons.Rounded.DirectionsRun)
    ),
    val recentRuns: List<RecentRunItem> = listOf(
        RecentRunItem("Morning Run", "Aug 8, 2026", "5.2 km", "450 pts"),
        RecentRunItem("Treasure Route", "Aug 6, 2026", "7.1 km", "620 pts"),
        RecentRunItem("Sunset Sprint", "Aug 4, 2026", "4.3 km", "390 pts")
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
    val routePresets: List<RoutePreset> = listOf(
        RoutePreset("Quick energy reset", "Short city loop with easy pacing.", 2.5f),
        RoutePreset("Treasure hunter", "Mid-distance route with dense checkpoints.", 5.2f),
        RoutePreset("Endurance push", "Longer route with wider pacing windows.", 7.8f)
    ),
    val locationSuggestions: List<LocationSuggestion> = listOf(
        LocationSuggestion("Canal South Gate", "West Canal Walk, Riverside District", "Flat terrain with river scenery."),
        LocationSuggestion("City Stadium", "East Track Avenue, Midtown", "Best for interval-heavy guided runs."),
        LocationSuggestion("Sunrise Park", "North Garden Loop, Hillview", "Good for warm-up laps and cooldowns.")
    ),
    val coachTips: List<CoachTip> = listOf(
        CoachTip("AI route coach", "Choose a scenic route today if you want a steadier heart-rate profile."),
        CoachTip("Pacing insight", "Your last two high-score runs started faster than average. Ease in for the first 400 meters."),
        CoachTip("Recovery check", "A jog preset works well today if you want to protect your streak without overreaching.")
    )
)

class CalorieChaseViewModel : ViewModel() {
    var uiState by mutableStateOf(AppUiState())
        private set

    fun updateBiometrics(heightCm: Int, weightKg: Int) {
        uiState = uiState.copy(
            profile = uiState.profile.copy(heightCm = heightCm, weightKg = weightKg)
        )
    }

    fun selectActivity(activity: String) {
        uiState = uiState.copy(
            selectedActivity = activity,
            activeRoute = uiState.activeRoute.copy(activityType = activity)
        )
    }

    fun selectDistance(distanceKm: Float) {
        uiState = uiState.copy(
            selectedDistanceKm = distanceKm,
            activeRoute = uiState.activeRoute.copy(
                distanceLabel = String.format("%.1f km", distanceKm)
            )
        )
    }

    fun updatePrompt(prompt: String) {
        uiState = uiState.copy(selectedPrompt = prompt)
    }

    fun selectLocation(locationSuggestion: LocationSuggestion) {
        uiState = uiState.copy(selectedLocation = locationSuggestion)
    }
}
