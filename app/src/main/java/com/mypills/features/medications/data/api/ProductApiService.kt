package com.mypills.features.medications.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class ProductResponse(
    val code: String,
    val status: Int,
    val product: ProductInfo?
)

data class ProductInfo(
    val product_name: String?,
    val brands: String?,
    val generic_name: String?,
    val quantity: String?,
    val serving_size: String?,
    val image_url: String?
)

interface ProductApiService {
    @GET("product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<ProductResponse>
}
