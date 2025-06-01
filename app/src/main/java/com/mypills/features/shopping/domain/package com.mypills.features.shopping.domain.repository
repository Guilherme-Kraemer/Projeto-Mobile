package com.mypills.features.shopping.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import com.mypills.features.shopping.domain.model.*

interface ShoppingRepository {
    fun getActiveShoppingLists(): Flow<List<ShoppingList>>
    fun getRecentShoppingLists(limit: Int = 10): Flow<List<ShoppingList>>
    suspend fun getShoppingListById(id: String): ShoppingList?
    suspend fun insertShoppingList(list: ShoppingList)
    suspend fun updateShoppingList(list: ShoppingList)
    suspend fun deleteShoppingList(list: ShoppingList)
    
    fun getItemsForList(listId: String): Flow<List<ShoppingItem>>
    fun getActiveItemsForList(listId: String): Flow<List<ShoppingItem>>
    suspend fun insertShoppingItem(item: ShoppingItem)
    suspend fun updateShoppingItem(item: ShoppingItem)
    suspend fun deleteShoppingItem(item: ShoppingItem)
    
    suspend fun getAveragePrice(productName: String, sinceDate: LocalDate): Double?
    suspend fun getLowestPrice(productName: String, sinceDate: LocalDate): Double?
    fun getPriceHistory(productName: String, limit: Int = 30): Flow<List<PriceHistory>>
    suspend fun insertPriceHistory(priceHistory: PriceHistory)
    
    suspend fun getEstimatedTotal(listId: String): Double?
    suspend fun getActualTotal(listId: String): Double?
    
    suspend fun searchProducts(query: String): List<ProductSuggestion>
    suspend fun getProductSuggestions(category: String): List<ProductSuggestion>
}
