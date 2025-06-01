package com.mypills.features.transport.data.api

import retrofit2.Response
import retrofit2.http.*

// Example API for public transport (adapt to your city's API)
data class ApiRoute(
    val id: String,
    val shortName: String,
    val longName: String,
    val agencyId: String,
    val type: Int
)

data class ApiStop(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val wheelchairBoarding: Int?
)

data class ApiArrival(
    val routeId: String,
    val stopId: String,
    val arrivalTime: Long,
    val delay: Int,
    val vehicleId: String?
)

interface TransportApiService {
    @GET("routes")
    suspend fun getRoutes(): Response<List<ApiRoute>>
    
    @GET("stops")
    suspend fun getStops(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("radius") radiusMeters: Int = 1000
    ): Response<List<ApiStop>>
    
    @GET("arrivals/{stopId}")
    suspend fun getArrivals(@Path("stopId") stopId: String): Response<List<ApiArrival>>
    
    @GET("journey")
    suspend fun planJourney(
        @Query("from") fromStopId: String,
        @Query("to") toStopId: String,
        @Query("time") departureTime: Long? = null
    ): Response<ApiJourney>
}

data class ApiJourney(
    val legs: List<ApiJourneyLeg>,
    val duration: Int,
    val distance: Double
)

data class ApiJourneyLeg(
    val routeId: String,
    val fromStopId: String,
    val toStopId: String,
    val departure: Long,
    val arrival: Long,
    val intermediateStops: List<String>
)
