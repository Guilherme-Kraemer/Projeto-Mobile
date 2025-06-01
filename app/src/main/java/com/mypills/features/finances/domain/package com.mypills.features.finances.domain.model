package com.mypills.features.finances.domain.model

import kotlinx.datetime.*

data class FinancialAccount(
    val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val currency: String = "BRL",
    val isActive: Boolean = true,
    val createdAt: Instant
) {
    val formattedBalance: String
        get() = "R$ %.2f".format(balance)
}

enum class AccountType {
    CHECKING, SAVINGS, CREDIT_CARD, CASH, INVESTMENT
}

data class FinancialTransaction(
    val id: String,
    val accountId: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val subcategory: String?,
    val description: String,
    val date: LocalDate,
    val isRecurring: Boolean = false,
    val recurringPeriod: String?,
    val tags: List<String> = emptyList(),
    val medicationRelated: Boolean = false,
    val createdAt: Instant
) {
    val formattedAmount: String
        get() = when (type) {
            TransactionType.INCOME -> "+R$ %.2f".format(amount)
            TransactionType.EXPENSE -> "-R$ %.2f".format(amount)
            TransactionType.TRANSFER -> "R$ %.2f".format(amount)
        }
    
    val absoluteAmount: Double
        get() = kotlin.math.abs(amount)
}

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

data class Budget(
    val id: String,
    val name: String,
    val category: String,
    val amount: Double,
    val period: BudgetPeriod,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val isActive: Boolean = true,
    val alertThreshold: Double = 0.8,
    val createdAt: Instant
) {
    val formattedAmount: String
        get() = "R$ %.2f".format(amount)
}

enum class BudgetPeriod {
    WEEKLY, MONTHLY, QUARTERLY, YEARLY
}

// Financial Calculator Models
data class CompoundInterestCalculation(
    val principal: Double,
    val rate: Double,
    val time: Int,
    val compoundingFrequency: Int,
    val finalAmount: Double,
    val totalInterest: Double
)

data class LoanCalculation(
    val principal: Double,
    val rate: Double,
    val term: Int,
    val monthlyPayment: Double,
    val totalPayment: Double,
    val totalInterest: Double
)

data class FinancialSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val medicationExpenses: Double,
    val balance: Double,
    val budgetUsage: Map<String, Double>
)

// Repository Interface
package com.mypills.features.finances.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import com.mypills.features.finances.domain.model.*

interface FinancialRepository {
    fun getAllActiveAccounts(): Flow<List<FinancialAccount>>
    suspend fun getAccountById(id: String): FinancialAccount?
    suspend fun insertAccount(account: FinancialAccount)
    suspend fun updateAccount(account: FinancialAccount)
    
    fun getTransactionsByAccount(accountId: String, limit: Int = 50): Flow<List<FinancialTransaction>>
    fun getTransactionsBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<FinancialTransaction>>
    fun getTransactionsByCategory(category: String, startDate: LocalDate): Flow<List<FinancialTransaction>>
    suspend fun insertTransaction(transaction: FinancialTransaction)
    suspend fun updateTransaction(transaction: FinancialTransaction)
    suspend fun deleteTransaction(transaction: FinancialTransaction)
    
    suspend fun getTotalIncome(startDate: LocalDate, endDate: LocalDate): Double
    suspend fun getTotalExpenses(startDate: LocalDate, endDate: LocalDate): Double
    suspend fun getMedicationExpenses(startDate: LocalDate, endDate: LocalDate): Double
    
    fun getActiveBudgets(): Flow<List<Budget>>
    suspend fun getBudgetByCategory(category: String): Budget?
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun getSpentInCategory(category: String, startDate: LocalDate, endDate: LocalDate): Double
}

// Use Cases
package com.mypills.features.finances.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.features.finances.domain.model.*
import com.mypills.features.finances.domain.repository.FinancialRepository

class GetFinancialSummaryUseCase @Inject constructor(
    private val repository: FinancialRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate): FinancialSummary {
        val totalIncome = repository.getTotalIncome(startDate, endDate)
        val totalExpenses = repository.getTotalExpenses(startDate, endDate)
        val medicationExpenses = repository.getMedicationExpenses(startDate, endDate)
        
        // Calculate budget usage (simplified)
        val budgetUsage = mapOf(
            "Medicamentos" to (medicationExpenses / 500.0), // Example budget of R$500
            "Outros" to ((totalExpenses - medicationExpenses) / 1000.0) // Example budget of R$1000
        )
        
        return FinancialSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            medicationExpenses = medicationExpenses,
            balance = totalIncome - totalExpenses,
            budgetUsage = budgetUsage
        )
    }
}

class CalculateCompoundInterestUseCase @Inject constructor() {
    operator fun invoke(
        principal: Double,
        annualRate: Double,
        years: Int,
        compoundingFrequency: Int = 12 // Monthly
    ): CompoundInterestCalculation {
        val rate = annualRate / 100.0
        val finalAmount = principal * kotlin.math.pow(
            1 + (rate / compoundingFrequency),
            (compoundingFrequency * years).toDouble()
        )
        val totalInterest = finalAmount - principal
        
        return CompoundInterestCalculation(
            principal = principal,
            rate = annualRate,
            time = years,
            compoundingFrequency = compoundingFrequency,
            finalAmount = finalAmount,
            totalInterest = totalInterest
        )
    }
}

class CalculateLoanPaymentUseCase @Inject constructor() {
    operator fun invoke(
        principal: Double,
        annualRate: Double,
        termInYears: Int
    ): LoanCalculation {
        val monthlyRate = (annualRate / 100.0) / 12.0
        val termInMonths = termInYears * 12
        
        val monthlyPayment = if (monthlyRate > 0) {
            principal * (monthlyRate * kotlin.math.pow(1 + monthlyRate, termInMonths.toDouble())) /
                    (kotlin.math.pow(1 + monthlyRate, termInMonths.toDouble()) - 1)
        } else {
            principal / termInMonths
        }
        
        val totalPayment = monthlyPayment * termInMonths
        val totalInterest = totalPayment - principal
        
        return LoanCalculation(
            principal = principal,
            rate = annualRate,
            term = termInYears,
            monthlyPayment = monthlyPayment,
            totalPayment = totalPayment,
            totalInterest = totalInterest
        )
    }
}

class AddTransactionUseCase @Inject constructor(
    private val repository: FinancialRepository
) {
    suspend operator fun invoke(transaction: FinancialTransaction) {
        repository.insertTransaction(transaction)
    }
}

class GetAllAccountsUseCase @Inject constructor(
    private val repository: FinancialRepository
) {
    operator fun invoke(): Flow<List<FinancialAccount>> = repository.getAllActiveAccounts()
}

class GetTransactionsBetweenDatesUseCase @Inject constructor(
    private val repository: FinancialRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<FinancialTransaction>> =
        repository.getTransactionsBetweenDates(startDate, endDate)
}
