package com.mypills.features.shopping.domain.model

import kotlinx.datetime.*

data class ShoppingList(
    val id: String,
    val name: String,
    val budget: Double?,
    val isCompleted: Boolean = false,
    val completedAt: Instant?,
    val totalEstimatedCost: Double = 0.0,
    val actualCost: Double = 0.0,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val budgetUsage: Double
        get() = budget?.let { totalEstimatedCost / it } ?: 0.0
    
    val isOverBudget: Boolean
        get() = budget?.let { totalEstimatedCost > it } ?: false
    
    val remainingBudget: Double
        get() = budget?.let { it - totalEstimatedCost } ?: 0.0
    
    val formattedBudget: String
        get() = budget?.let { "R$ %.2f".format(it) } ?: "Sem orçamento"
    
    val formattedEstimatedCost: String
        get() = "R$ %.2f".format(totalEstimatedCost)
}

data class ShoppingItem(
    val id: String,
    val listId: String,
    val name: String,
    val category: String,
    val quantity: Double = 1.0,
    val unit: String = "un",
    val estimatedPrice: Double?,
    val actualPrice: Double?,
    val brand: String?,
    val store: String?,
    val isCompleted: Boolean = false,
    val isPriority: Boolean = false,
    val barcode: String?,
    val notes: String?,
    val createdAt: Instant
) {
    val totalEstimatedCost: Double
        get() = (estimatedPrice ?: 0.0) * quantity
    
    val totalActualCost: Double
        get() = (actualPrice ?: 0.0) * quantity
    
    val formattedQuantity: String
        get() = if (quantity == quantity.toInt().toDouble()) {
            "${quantity.toInt()} $unit"
        } else {
            "%.1f $unit".format(quantity)
        }
    
    val priceComparison: String?
        get() = if (estimatedPrice != null && actualPrice != null) {
            val difference = actualPrice - estimatedPrice
            when {
                difference > 0.01 -> "+R$ %.2f".format(difference)
                difference < -0.01 -> "-R$ %.2f".format(-difference)
                else -> "Conforme estimado"
            }
        } else null
}

data class PriceHistory(
    val id: String,
    val productName: String,
    val barcode: String?,
    val store: String,
    val price: Double,
    val date: LocalDate,
    val location: String?,
    val source: String,
    val createdAt: Instant
)

data class ProductSuggestion(
    val productName: String,
    val category: String,
    val estimatedPrice: Double,
    val averagePrice: Double,
    val lowestPrice: Double,
    val recommendedStore: String?,
    val savings: Double,
    val confidence: Double // AI confidence score
)

data class BudgetAnalysis(
    val totalBudget: Double,
    val usedBudget: Double,
    val remainingBudget: Double,
    val recommendedReductions: List<BudgetRecommendation>,
    val alternativeProducts: List<ProductAlternative>
)

data class BudgetRecommendation(
    val itemId: String,
    val itemName: String,
    val currentCost: Double,
    val recommendedAction: String,
    val potentialSavings: Double,
    val reason: String
)

data class ProductAlternative(
    val originalItem: String,
    val alternativeProduct: String,
    val priceDifference: Double,
    val store: String,
    val quality: String // similar, better, lower
)
