package com.mypills.features.shopping.data.mapper

import com.mypills.core.database.entity.*
import com.mypills.features.shopping.domain.model.*

fun ShoppingListEntity.toDomain(): ShoppingList = ShoppingList(
    id = id,
    name = name,
    budget = budget,
    isCompleted = isCompleted,
    completedAt = completedAt,
    totalEstimatedCost = totalEstimatedCost,
    actualCost = actualCost,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ShoppingList.toEntity(): ShoppingListEntity = ShoppingListEntity(
    id = id,
    name = name,
    budget = budget,
    isCompleted = isCompleted,
    completedAt = completedAt,
    totalEstimatedCost = totalEstimatedCost,
    actualCost = actualCost,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ShoppingItemEntity.toDomain(): ShoppingItem = ShoppingItem(
    id = id,
    listId = listId,
    name = name,
    category = category,
    quantity = quantity,
    unit = unit,
    estimatedPrice = estimatedPrice,
    actualPrice = actualPrice,
    brand = brand,
    store = store,
    isCompleted = isCompleted,
    isPriority = isPriority,
    barcode = barcode,
    notes = notes,
    createdAt = createdAt
)

fun ShoppingItem.toEntity(): ShoppingItemEntity = ShoppingItemEntity(
    id = id,
    listId = listId,
    name = name,
    category = category,
    quantity = quantity,
    unit = unit,
    estimatedPrice = estimatedPrice,
    actualPrice = actualPrice,
    brand = brand,
    store = store,
    isCompleted = isCompleted,
    isPriority = isPriority,
    barcode = barcode,
    notes = notes,
    createdAt = createdAt
)

fun PriceHistoryEntity.toDomain(): PriceHistory = PriceHistory(
    id = id,
    productName = productName,
    barcode = barcode,
    store = store,
    price = price,
    date = date,
    location = location,
    source = source,
    createdAt = createdAt
)

fun PriceHistory.toEntity(): PriceHistoryEntity = PriceHistoryEntity(
    id = id,
    productName = productName,
    barcode = barcode,
    store = store,
    price = price,
    date = date,
    location = location,
    source = source,
    createdAt = createdAt
)
