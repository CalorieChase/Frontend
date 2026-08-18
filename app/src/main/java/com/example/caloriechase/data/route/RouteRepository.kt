package com.example.caloriechase.data.route

import com.example.caloriechase.ui.CoinSpot
import com.example.caloriechase.ui.RouteCheckpoint
import com.example.caloriechase.ui.RoutePlannerRequest
import com.example.caloriechase.ui.RoutePoint
import com.example.caloriechase.ui.RoutePreview
import kotlin.math.roundToInt

class RouteRepository(
    private val apiService: RouteApiService = RouteApiFactory.create()
) {
    suspend fun generateRoute(request: RoutePlannerRequest): RoutePreview {
        val response = apiService.generateRoute(
            GenerateRouteRequestDto(
                startingLocation = request.startingLocation,
                weightKg = request.weightKg,
                goalType = request.goalType.apiValue,
                goalValue = request.goalValue,
                routeType = request.routeType.apiValue
            )
        )

        val routePoints = response.routeCoordinates.map { RoutePoint(lat = it.lat, lng = it.lng) }
        val coinSpots = response.goldCoins.map { CoinSpot(lat = it.lat, lng = it.lng, value = it.value) }
        val durationMinutes = ((response.totalDistanceKm / 4.8) * 60.0).roundToInt().coerceAtLeast(10)
        val totalScore = coinSpots.sumOf { it.value }
        val checkpoints = buildCheckpoints(coinSpots, response.totalDistanceKm)

        return RoutePreview(
            routeName = when (response.routeType) {
                "LOOP" -> "Loop Treasure Route"
                "TURNAROUND" -> "Out-and-back Treasure Route"
                else -> "Adaptive Treasure Route"
            },
            activityType = "Walk",
            totalDistanceKm = response.totalDistanceKm,
            estimatedActiveCalories = response.estimatedActiveCalories.roundToInt(),
            distanceLabel = String.format("%.1f km", response.totalDistanceKm),
            durationLabel = "$durationMinutes min",
            caloriesLabel = "${response.estimatedActiveCalories.roundToInt()} kcal",
            scoreLabel = "$totalScore pts",
            description = buildDescription(request, response.routeType, coinSpots.size),
            checkpoints = checkpoints,
            routePoints = routePoints,
            coinSpots = coinSpots,
            routeTypeLabel = response.routeType,
            routePolyline = response.routePolyline
        )
    }

    private fun buildDescription(
        request: RoutePlannerRequest,
        routeType: String,
        coinCount: Int
    ): String {
        val goalLabel = when (request.goalType) {
            com.example.caloriechase.ui.GoalTypeUi.Distance -> String.format("%.1f km", request.goalValue)
            com.example.caloriechase.ui.GoalTypeUi.ActiveCalories -> "${request.goalValue.roundToInt()} active kcal"
        }
        val routeStyle = when (routeType) {
            "LOOP" -> "loop"
            "TURNAROUND" -> "out-and-back"
            else -> "adaptive"
        }
        return "A $routeStyle walking route tuned for $goalLabel with $coinCount coin pickups along the path."
    }

    private fun buildCheckpoints(coinSpots: List<CoinSpot>, distanceKm: Double): List<RouteCheckpoint> {
        if (coinSpots.isEmpty()) {
            return listOf(
                RouteCheckpoint(
                    title = "Route start",
                    detail = "Your generated route is ready. Follow the line and keep a steady pace.",
                    reward = "0 pts"
                )
            )
        }

        val indexedCoins = coinSpots.mapIndexed { index, coin -> index to coin }
        val checkpointIndices = listOf(0, coinSpots.lastIndex / 2, coinSpots.lastIndex).distinct()

        return checkpointIndices.mapIndexed { checkpointNumber, coinIndex ->
            val coin = indexedCoins[coinIndex].second
            val progressRatio = (coinIndex + 1).toDouble() / coinSpots.size.toDouble()
            val distanceAtCheckpoint = distanceKm * progressRatio
            RouteCheckpoint(
                title = when (checkpointNumber) {
                    0 -> "Opening coin lane"
                    1 -> "Mid-route vault"
                    else -> "Finish reward line"
                },
                detail = "Aim for the coin cluster around ${String.format("%.1f", distanceAtCheckpoint)} km into the route.",
                reward = "${coin.value} pts"
            )
        }
    }
}
