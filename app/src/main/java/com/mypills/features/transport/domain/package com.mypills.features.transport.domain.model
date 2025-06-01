package com.mypills.features.transport.domain.model

import kotlinx.datetime.*

data class BusRoute(
    val id: String,
    val routeNumber: String,
    val routeName: String,
    val company: String,
    val startLocation: String,
    val endLocation: String,
    val distance: Double?,
    val estimatedDuration: Int?,
    val fare: Double?,
    val isActive: Boolean = true,
    val lastUpdated: Instant
)

data class BusStop(
    val id: String,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val hasAccessibility: Boolean = false,
    val amenities: List<String> = emptyList()
) {
    fun distanceTo(lat: Double, lng: Double): Double {
        // Haversine formula for distance calculation
        val earthRadius = 6371.0 // km
        val dLat = Math.toRadians(lat - latitude)
        val dLng = Math.toRadians(lng - longitude)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(latitude)) * kotlin.math.cos(Math.toRadians(lat)) *
                kotlin.math.sin(dLng / 2) * kotlin.math.sin(dLng / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return earthRadius * c
    }
}

data class RouteStop(
    val id: String,
    val routeId: String,
    val stopId: String,
    val sequence: Int,
    val estimatedTime: Int
)

data class FavoriteRoute(
    val id: String,
    val name: String,
    val fromStopId: String,
    val toStopId: String,
    val routeIds: List<String>,
    val createdAt: Instant
)

data class BusArrival(
    val routeId: String,
    val stopId: String,
    val estimatedArrival: Instant,
    val delay: Int = 0, // minutes
    val vehicleId: String?,
    val lastUpdated: Instant
) {
    val arrivalText: String
        get() {
            val now = Clock.System.now()
            val diffMinutes = (estimatedArrival.toEpochMilliseconds() - now.toEpochMilliseconds()) / 60000
            return when {
                diffMinutes <= 0 -> "Chegando"
                diffMinutes <= 1 -> "1 minuto"
                else -> "${diffMinutes.toInt()} minutos"
            }
        }
}

data class Journey(
    val fromStop: BusStop,
    val toStop: BusStop,
    val routes: List<JourneySegment>,
    val totalDuration: Int,
    val totalDistance: Double,
    val totalFare: Double
)

data class JourneySegment(
    val route: BusRoute,
    val fromStop: BusStop,
    val toStop: BusStop,
    val departureTime: Instant?,
    val arrivalTime: Instant?,
    val duration: Int,
    val stops: List<BusStop>
)
