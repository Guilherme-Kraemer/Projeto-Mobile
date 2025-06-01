package com.mypills.features.transport.domain.repository

import kotlinx.coroutines.flow.Flow
import com.mypills.features.transport.domain.model.*

interface TransportRepository {
    fun getAllActiveRoutes(): Flow<List<BusRoute>>
    suspend fun getRouteById(id: String): BusRoute?
    fun searchRoutes(query: String): Flow<List<BusRoute>>
    suspend fun insertRoute(route: BusRoute)
    suspend fun updateRoute(route: BusRoute)
    
    fun getAllStops(): Flow<List<BusStop>>
    fun searchStops(query: String): Flow<List<BusStop>>
    suspend fun getNearbyStops(latitude: Double, longitude: Double, radiusKm: Double = 1.0): List<BusStop>
    suspend fun insertStop(stop: BusStop)
    
    suspend fun getStopsForRoute(routeId: String): List<BusStop>
    suspend fun getRoutesForStop(stopId: String): List<BusRoute>
    
    fun getFavoriteRoutes(): Flow<List<FavoriteRoute>>
    suspend fun insertFavoriteRoute(favorite: FavoriteRoute)
    suspend fun deleteFavoriteRoute(favorite: FavoriteRoute)
    
    suspend fun getRealTimeArrivals(stopId: String): List<BusArrival>
    suspend fun getJourney(fromStopId: String, toStopId: String): Journey?
}
