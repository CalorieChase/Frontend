package com.example.caloriechase.data.route

data class GenerateRouteRequestDto(
    val startingLocation: String,
    val weightKg: Int,
    val goalType: String,
    val goalValue: Float,
    val routeType: String
)

data class RoutePointDto(
    val lat: Double,
    val lng: Double
)

data class CoinSpotDto(
    val lat: Double,
    val lng: Double,
    val value: Int
)

data class GenerateRouteResponseDto(
    val routeCoordinates: List<RoutePointDto>,
    val routePolyline: String,
    val routeType: String,
    val totalDistanceKm: Double,
    val estimatedActiveCalories: Double,
    val goldCoins: List<CoinSpotDto>
)
