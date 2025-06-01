package com.mypills.features.shopping.data.api

import retrofit2.Response
import retrofit2.http.*

// API for price comparison (example integration)
data class PriceApiResponse(
    val product: String,
    val stores: List<StorePrice>
)

data class StorePrice(
    val storeName: String,
    val price: Double,
    val inStock: Boolean,
    val lastUpdated: String
)

interface PriceComparisonApi {
    @GET("search")
    suspend fun searchProduct(@Query("q") query: String): Response<PriceApiResponse>
    
    @GET("barcode/{code}")
    suspend fun getProductByBarcode(@Path("code") barcode: String): Response<PriceApiResponse>
    
    @GET("stores/{storeId}/products")
    suspend fun getStoreProducts(@Path("storeId") storeId: String): Response<List<PriceApiResponse>>
}