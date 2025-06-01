package com.mypills.features.transport.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import com.mypills.features.transport.domain.model.*
import com.mypills.features.transport.domain.repository.TransportRepository

class GetNearbyStopsUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 1.0
    ): List<BusStop> {
        return repository.getNearbyStops(latitude, longitude, radiusKm)
            .sortedBy { it.distanceTo(latitude, longitude) }
    }
}

class GetRealTimeArrivalsUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    suspend operator fun invoke(stopId: String): List<BusArrival> {
        return repository.getRealTimeArrivals(stopId)
            .sortedBy { it.estimatedArrival }
    }
}

class PlanJourneyUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    suspend operator fun invoke(fromStopId: String, toStopId: String): Journey? {
        return repository.getJourney(fromStopId, toStopId)
    }
}

class SearchStopsUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    operator fun invoke(query: String): Flow<List<BusStop>> {
        return repository.searchStops(query)
    }
}

class GetFavoriteRoutesUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    operator fun invoke(): Flow<List<FavoriteRoute>> {
        return repository.getFavoriteRoutes()
    }
}

class AddFavoriteRouteUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    suspend operator fun invoke(favorite: FavoriteRoute) {
        repository.insertFavoriteRoute(favorite)
    }
}

class GetAllRoutesUseCase @Inject constructor(
    private val repository: TransportRepository
) {
    operator fun invoke(): Flow<List<BusRoute>> {
        return repository.getAllActiveRoutes()
    }
}