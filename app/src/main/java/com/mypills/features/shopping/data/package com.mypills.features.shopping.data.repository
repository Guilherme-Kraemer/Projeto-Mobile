package com.mypills.features.shopping.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import com.mypills.core.database.dao.ShoppingDao
import com.mypills.features.shopping.domain.model.*
import com.mypills.features.shopping.domain.repository.ShoppingRepository
import com.mypills.features.shopping.data.mapper.*
import com.mypills.features.shopping.data.api.PriceComparisonApi

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val priceApi: PriceComparisonApi
) : ShoppingRepository {

    override fun getActiveShoppingLists(): Flow<List<ShoppingList>> =
        shoppingDao.getActiveShoppingLists().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getRecentShoppingLists(limit: Int): Flow<List<ShoppingList>> =
        shoppingDao.getRecentShoppingLists(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getShoppingListById(id: String): ShoppingList? =
        shoppingDao.getShoppingListById(id)?.toDomain()

    override suspend fun insertShoppingList(list: ShoppingList) {
        shoppingDao.insertShoppingList(list.toEntity())
    }

    override suspend fun updateShoppingList(list: ShoppingList) {
        shoppingDao.updateShoppingList(list.toEntity())
    }

    override suspend fun deleteShoppingList(list: ShoppingList) {
        shoppingDao.deleteShoppingList(list.toEntity())
    }

    override fun getItemsForList(listId: String): Flow<List<ShoppingItem>> =
        shoppingDao.getItemsForList(listId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getActiveItemsForList(listId: String): Flow<List<ShoppingItem>> =
        shoppingDao.getActiveItemsForList(listId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertShoppingItem(item: ShoppingItem) {
        shoppingDao.insertShoppingItem(item.toEntity())
    }

    override suspend fun updateShoppingItem(item: ShoppingItem) {
        shoppingDao.updateShoppingItem(item.toEntity())
    }

    override suspend fun deleteShoppingItem(item: ShoppingItem) {
        shoppingDao.deleteShoppingItem(item.toEntity())
    }

    override suspend fun getAveragePrice(productName: String, sinceDate: LocalDate): Double? =
        shoppingDao.getAveragePrice(productName, sinceDate)

    override suspend fun getLowestPrice(productName: String, sinceDate: LocalDate): Double? =
        shoppingDao.getLowestPrice(productName, sinceDate)

    override fun getPriceHistory(productName: String, limit: Int): Flow<List<PriceHistory>> =
        shoppingDao.getPriceHistory(productName, limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertPriceHistory(priceHistory: PriceHistory) {
        shoppingDao.insertPriceHistory(priceHistory.toEntity())
    }

    override suspend fun getEstimatedTotal(listId: String): Double? =
        shoppingDao.getEstimatedTotal(listId)

    override suspend fun getActualTotal(listId: String): Double? =
        shoppingDao.getActualTotal(listId)

    override suspend fun searchProducts(query: String): List<ProductSuggestion> {
        return try {
            val response = priceApi.searchProduct(query)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    apiResponse.stores.map { store ->
                        ProductSuggestion(
                            productName = apiResponse.product,
                            category = "general",
                            estimatedPrice = store.price,
                            averagePrice = store.price,
                            lowestPrice = store.price,
                            recommendedStore = store.storeName,
                            savings = 0.0,
                            confidence = 0.8
                        )
                    }
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductSuggestions(category: String): List<ProductSuggestion> {
        // Mock implementation for AI suggestions
        return listOf(
            ProductSuggestion(
                productName = "Produto Genérico",
                category = category,
                estimatedPrice = 10.0,
                averagePrice = 12.0,
                lowestPrice = 8.0,
                recommendedStore = "Farmácia Popular",
                savings = 2.0,
                confidence = 0.7
            )
        )
    }
}