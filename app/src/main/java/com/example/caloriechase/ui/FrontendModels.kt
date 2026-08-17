package com.example.caloriechase.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class UserProfile(
    val name: String,
    val email: String,
    val heightCm: Int,
    val weightKg: Int,
    val levelTitle: String,
    val streakDays: Int
)

data class HomeFocus(
    val label: String,
    val value: String,
    val supporting: String,
    val accent: Color,
    val icon: ImageVector
)

data class DashboardMetricItem(
    val label: String,
    val value: String,
    val accent: Color,
    val icon: ImageVector
)

data class RecentRunItem(
    val title: String,
    val date: String,
    val distance: String,
    val score: String
)

data class TreasureSpot(
    val title: String,
    val subtitle: String,
    val reward: String,
    val distanceAway: String
)

data class ProgressHighlight(
    val title: String,
    val value: String,
    val supporting: String
)

data class AchievementItem(
    val title: String,
    val description: String,
    val unlocked: Boolean
)

data class LocationSuggestion(
    val title: String,
    val address: String,
    val description: String,
    val backendQuery: String
)

enum class GoalTypeUi(val apiValue: String, val label: String) {
    Distance("distance", "Distance"),
    ActiveCalories("active_calories", "Calories")
}

enum class RouteTypeUi(val apiValue: String, val label: String) {
    Auto("AUTO", "Auto"),
    Loop("LOOP", "Loop"),
    Turnaround("TURNAROUND", "Turnaround")
}

data class RoutePoint(
    val lat: Double,
    val lng: Double
)

data class CoinSpot(
    val lat: Double,
    val lng: Double,
    val value: Int
)

data class RouteCheckpoint(
    val title: String,
    val detail: String,
    val reward: String
)

data class RoutePreview(
    val routeName: String,
    val activityType: String,
    val distanceLabel: String,
    val durationLabel: String,
    val caloriesLabel: String,
    val scoreLabel: String,
    val description: String,
    val checkpoints: List<RouteCheckpoint>,
    val routePoints: List<RoutePoint>,
    val coinSpots: List<CoinSpot>,
    val routeTypeLabel: String,
    val routePolyline: String
)

data class RoutePlannerRequest(
    val startingLocation: String,
    val weightKg: Int,
    val goalType: GoalTypeUi,
    val goalValue: Float,
    val routeType: RouteTypeUi
)

data class RoutePlannerSummary(
    val goalHeadline: String,
    val goalDetail: String,
    val routeDetail: String
)

data class RunStat(
    val label: String,
    val value: String
)

data class CoachTip(
    val title: String,
    val message: String
)
