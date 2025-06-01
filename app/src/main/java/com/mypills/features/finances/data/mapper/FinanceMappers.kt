package com.mypills.features.finances.data.mapper

import com.mypills.core.database.entity.*
import com.mypills.features.finances.domain.model.*

fun FinancialAccountEntity.toDomain(): FinancialAccount = FinancialAccount(
    id = id,
    name = name,
    type = type,
    balance = balance,
    currency = currency,
    isActive = isActive,
    createdAt = createdAt
)

fun FinancialAccount.toEntity(): FinancialAccountEntity = FinancialAccountEntity(
    id = id,
    name = name,
    type = type,
    balance = balance,
    currency = currency,
    isActive = isActive,
    createdAt = createdAt
)

fun FinancialTransactionEntity.toDomain(): FinancialTransaction = FinancialTransaction(
    id = id,
    accountId = accountId,
    amount = amount,
    type = type,
    category = category,
    subcategory = subcategory,
    description = description,
    date = date,
    isRecurring = isRecurring,
    recurringPeriod = recurringPeriod,
    tags = tags,
    medicationRelated = medicationRelated,
    createdAt = createdAt
)

fun FinancialTransaction.toEntity(): FinancialTransactionEntity = FinancialTransactionEntity(
    id = id,
    accountId = accountId,
    amount = amount,
    type = type,
    category = category,
    subcategory = subcategory,
    description = description,
    date = date,
    isRecurring = isRecurring,
    recurringPeriod = recurringPeriod,
    tags = tags,
    medicationRelated = medicationRelated,
    createdAt = createdAt
)

fun BudgetEntity.toDomain(): Budget = Budget(
    id = id,
    name = name,
    category = category,
    amount = amount,
    period = period,
    startDate = startDate,
    endDate = endDate,
    isActive = isActive,
    alertThreshold = alertThreshold,
    createdAt = createdAt
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    name = name,
    category = category,
    amount = amount,
    period = period,
    startDate = startDate,
    endDate = endDate,
    isActive = isActive,
    alertThreshold = alertThreshold,
    createdAt = createdAt
)
