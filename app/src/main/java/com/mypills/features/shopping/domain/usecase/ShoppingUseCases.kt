package com.mypills.features.shopping.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.features.shopping.domain.model.*
import com.mypills.features.shopping.domain.repository.ShoppingRepository

class GetActiveShoppingListsUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingList>> = repository.getActiveShoppingLists()
}

class CreateShoppingListUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(name: String, budget: Double?): ShoppingList {
        val now = Clock.System.now()
        val shoppingList = ShoppingList(
            id = java.util.UUID.randomUUID().toString(),
            name = name,
            budget = budget,
            isCompleted = false,
            completedAt = null,
            totalEstimatedCost = 0.0,
            actualCost = 0.0,
            createdAt = now,
            updatedAt = now
        )
        
        repository.insertShoppingList(shoppingList)
        return shoppingList
    }
}

class AddItemToListUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(
        listId: String,
        name: String,
        category: String,
        quantity: Double = 1.0,
        unit: String = "un",
        estimatedPrice: Double? = null,
        barcode: String? = null
    ): ShoppingItem {
        // Get price suggestion if not provided
        val finalEstimatedPrice = estimatedPrice ?: getPriceSuggestion(name, category)
        
        val item = ShoppingItem(
            id = java.util.UUID.randomUUID().toString(),
            listId = listId,
            name = name,
            category = category,
            quantity = quantity,
            unit = unit,
            estimatedPrice = finalEstimatedPrice,
            actualPrice = null,
            brand = null,
            store = null,
            isCompleted = false,
            isPriority = false,
            barcode = barcode,
            notes = null,
            createdAt = Clock.System.now()
        )
        
        repository.insertShoppingItem(item)
        updateListTotals(listId)
        return item
    }
    
    private suspend fun getPriceSuggestion(productName: String, category: String): Double? {
        val thirtyDaysAgo = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            .minus(DatePeriod(days = 30))
        return repository.getAveragePrice(productName, thirtyDaysAgo)
    }
    
    private suspend fun updateListTotals(listId: String) {
        val estimatedTotal = repository.getEstimatedTotal(listId) ?: 0.0
        val actualTotal = repository.getActualTotal(listId) ?: 0.0
        
        repository.getShoppingListById(listId)?.let { list ->
            repository.updateShoppingList(
                list.copy(
                    totalEstimatedCost = estimatedTotal,
                    actualCost = actualTotal,
                    updatedAt = Clock.System.now()
                )
            )
        }
    }
}

class OptimizeShoppingListUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(listId: String): BudgetAnalysis {
        val list = repository.getShoppingListById(listId) 
            ?: throw IllegalArgumentException("Lista não encontrada")
        
        val budget = list.budget ?: return BudgetAnalysis(
            totalBudget = 0.0,
            usedBudget = list.totalEstimatedCost,
            remainingBudget = 0.0,
            recommendedReductions = emptyList(),
            alternativeProducts = emptyList()
        )
        
        // Get items for analysis
        val items = repository.getActiveItemsForList(listId)
        val itemsList = mutableListOf<ShoppingItem>()
        items.collect { itemsList.addAll(it) }
        
        val recommendations = generateRecommendations(itemsList, budget)
        val alternatives = findAlternatives(itemsList)
        
        return BudgetAnalysis(
            totalBudget = budget,
            usedBudget = list.totalEstimatedCost,
            remainingBudget = list.remainingBudget,
            recommendedReductions = recommendations,
            alternativeProducts = alternatives
        )
    }
    
    private suspend fun generateRecommendations(
        items: List<ShoppingItem>,
        budget: Double
    ): List<BudgetRecommendation> {
        val recommendations = mutableListOf<BudgetRecommendation>()
        val totalCost = items.sumOf { it.totalEstimatedCost }
        
        if (totalCost <= budget) return recommendations
        
        val overage = totalCost - budget
        
        // Suggest removing non-priority items first
        items.filter { !it.isPriority && it.estimatedPrice != null }
            .sortedByDescending { it.totalEstimatedCost }
            .take(3)
            .forEach { item ->
                recommendations.add(
                    BudgetRecommendation(
                        itemId = item.id,
                        itemName = item.name,
                        currentCost = item.totalEstimatedCost,
                        recommendedAction = "Remover",
                        potentialSavings = item.totalEstimatedCost,
                        reason = "Item não prioritário com custo alto"
                    )
                )
            }
        
        // Suggest reducing quantities
        items.filter { it.quantity > 1 && it.estimatedPrice != null }
            .sortedByDescending { it.totalEstimatedCost }
            .take(2)
            .forEach { item ->
                val reducedQuantity = kotlin.math.max(1.0, item.quantity - 1.0)
                val savings = (item.quantity - reducedQuantity) * (item.estimatedPrice ?: 0.0)
                
                recommendations.add(
                    BudgetRecommendation(
                        itemId = item.id,
                        itemName = item.name,
                        currentCost = item.totalEstimatedCost,
                        recommendedAction = "Reduzir quantidade para $reducedQuantity",
                        potentialSavings = savings,
                        reason = "Redução de quantidade para otimizar orçamento"
                    )
                )
            }
        
        return recommendations
    }
    
    private suspend fun findAlternatives(items: List<ShoppingItem>): List<ProductAlternative> {
        val alternatives = mutableListOf<ProductAlternative>()
        
        items.forEach { item ->
            // Simulate finding cheaper alternatives
            val suggestions = repository.getProductSuggestions(item.category)
            suggestions.filter { it.estimatedPrice < (item.estimatedPrice ?: Double.MAX_VALUE) }
                .take(1)
                .forEach { suggestion ->
                    alternatives.add(
                        ProductAlternative(
                            originalItem = item.name,
                            alternativeProduct = suggestion.productName,
                            priceDifference = (item.estimatedPrice ?: 0.0) - suggestion.estimatedPrice,
                            store = suggestion.recommendedStore ?: "Loja genérica",
                            quality = "similar"
                        )
                    )
                }
        }
        
        return alternatives
    }
}

class UpdateItemPriceUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(itemId: String, actualPrice: Double, store: String? = null) {
        // Get the item and update with actual price
        repository.getActiveItemsForList("").collect { items ->
            val item = items.find { it.id == itemId }
            if (item != null) {
                val updatedItem = item.copy(
                    actualPrice = actualPrice,
                    store = store ?: item.store
                )
                repository.updateShoppingItem(updatedItem)
                
                // Add to price history
                val priceHistory = PriceHistory(
                    id = java.util.UUID.randomUUID().toString(),
                    productName = item.name,
                    barcode = item.barcode,
                    store = store ?: "Loja não informada",
                    price = actualPrice,
                    date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                    location = null,
                    source = "manual",
                    createdAt = Clock.System.now()
                )
                repository.insertPriceHistory(priceHistory)
                
                // Update list totals
                updateListTotals(item.listId)
            }
        }
    }
    
    private suspend fun updateListTotals(listId: String) {
        val estimatedTotal = repository.getEstimatedTotal(listId) ?: 0.0
        val actualTotal = repository.getActualTotal(listId) ?: 0.0
        
        repository.getShoppingListById(listId)?.let { list ->
            repository.updateShoppingList(
                list.copy(
                    totalEstimatedCost = estimatedTotal,
                    actualCost = actualTotal,
                    updatedAt = Clock.System.now()
                )
            )
        }
    }
}

class GetPriceInsightsUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(productName: String): PriceInsight? {
        val thirtyDaysAgo = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            .minus(DatePeriod(days = 30))
        
        val averagePrice = repository.getAveragePrice(productName, thirtyDaysAgo)
        val lowestPrice = repository.getLowestPrice(productName, thirtyDaysAgo)
        
        return if (averagePrice != null && lowestPrice != null) {
            PriceInsight(
                productName = productName,
                averagePrice = averagePrice,
                lowestPrice = lowestPrice,
                potentialSavings = averagePrice - lowestPrice,
                recommendation = generatePriceRecommendation(averagePrice, lowestPrice)
            )
        } else null
    }
    
    private fun generatePriceRecommendation(average: Double, lowest: Double): String {
        val savingsPercentage = ((average - lowest) / average) * 100
        return when {
            savingsPercentage > 20 -> "Procure por promoções - economia potencial de ${savingsPercentage.toInt()}%"
            savingsPercentage > 10 -> "Compare preços - possível economia de ${savingsPercentage.toInt()}%"
            else -> "Preço está dentro da média"
        }
    }
}

data class PriceInsight(
    val productName: String,
    val averagePrice: Double,
    val lowestPrice: Double,
    val potentialSavings: Double,
    val recommendation: String
)
