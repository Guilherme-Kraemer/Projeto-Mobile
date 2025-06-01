package com.mypills.features.transport.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.mypills.core.database.dao.TransportDao
import com.mypills.features.transport.domain.model.*
import com.mypills.features.transport.domain.repository.TransportRepository
import com.mypills.features.transport.data.mapper.*
import com.mypills.features.transport.data.api.TransportApiService

class TransportRepositoryImpl @Inject constructor(
    private val transportDao: TransportDao,
    private val apiService: TransportApiService
) : TransportRepository {

    override fun getAllActiveRoutes(): Flow<List<BusRoute>> =
        transportDao.getAllActiveRoutes().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getRouteById(id: String): BusRoute? =
        transportDao.getRouteById(id)?.toDomain()

    override fun searchRoutes(query: String): Flow<List<BusRoute>> =
        transportDao.searchRoutes(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertRoute(route: BusRoute) {
        transportDao.insertRoute(route.toEntity())
    }

    override suspend fun updateRoute(route: BusRoute) {
        transportDao.updateRoute(route.toEntity())
    }

    override fun getAllStops(): Flow<List<BusStop>> =
        transportDao.getAllStops().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun searchStops(query: String): Flow<List<BusStop>> =
        transportDao.searchStops(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getNearbyStops(latitude: Double, longitude: Double, radiusKm: Double): List<BusStop> {
        val radiusKmToDegreesApprox = radiusKm / 111.0 // Rough conversion
        val minLat = latitude - radiusKmToDegreesApprox
        val maxLat = latitude + radiusKmToDegreesApprox
        val minLng = longitude - radiusKmToDegreesApprox
        val maxLng = longitude + radiusKmToDegreesApprox
        
        return transportDao.getNearbyStops(minLat, maxLat, minLng, maxLng)
            .map { it.toDomain() }
    }

    override suspend fun insertStop(stop: BusStop) {
        transportDao.insertStop(stop.toEntity())
    }

    override suspend fun getStopsForRoute(routeId: String): List<BusStop> =
        transportDao.getStopsForRoute(routeId).map { it.toDomain() }

    override suspend fun getRoutesForStop(stopId: String): List<BusRoute> {
        // Implementation would join route_stops table
        return emptyList() // Simplified for now
    }

    override fun getFavoriteRoutes(): Flow<List<FavoriteRoute>> =
        transportDao.getFavoriteRoutes().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertFavoriteRoute(favorite: FavoriteRoute) {
        transportDao.insertFavoriteRoute(favorite.toEntity())
    }

    override suspend fun deleteFavoriteRoute(favorite: FavoriteRoute) {
        transportDao.deleteFavoriteRoute(favorite.toEntity())
    }

    override suspend fun getRealTimeArrivals(stopId: String): List<BusArrival> {
        return try {
            val response = apiService.getArrivals(stopId)
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getJourney(fromStopId: String, toStopId: String): Journey? {
        return try {
            val response = apiService.planJourney(fromStopId, toStopId)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
