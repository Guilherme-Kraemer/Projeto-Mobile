package com.mypills.features.finances.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import com.mypills.core.database.dao.FinancesDao
import com.mypills.features.finances.domain.model.*
import com.mypills.features.finances.domain.repository.FinancialRepository
import com.mypills.features.finances.data.mapper.*

class FinancialRepositoryImpl @Inject constructor(
    private val financesDao: FinancesDao
) : FinancialRepository {

    override fun getAllActiveAccounts(): Flow<List<FinancialAccount>> =
        financesDao.getAllActiveAccounts().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getAccountById(id: String): FinancialAccount? =
        financesDao.getAccountById(id)?.toDomain()

    override suspend fun insertAccount(account: FinancialAccount) {
        financesDao.insertAccount(account.toEntity())
    }

    override suspend fun updateAccount(account: FinancialAccount) {
        financesDao.updateAccount(account.toEntity())
    }

    override fun getTransactionsByAccount(accountId: String, limit: Int): Flow<List<FinancialTransaction>> =
        financesDao.getTransactionsByAccount(accountId, limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTransactionsBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<FinancialTransaction>> =
        financesDao.getTransactionsBetweenDates(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTransactionsByCategory(category: String, startDate: LocalDate): Flow<List<FinancialTransaction>> =
        financesDao.getTransactionsByCategory(category, startDate).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertTransaction(transaction: FinancialTransaction) {
        financesDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: FinancialTransaction) {
        financesDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: FinancialTransaction) {
        financesDao.deleteTransaction(transaction.toEntity())
    }

    override suspend fun getTotalIncome(startDate: LocalDate, endDate: LocalDate): Double =
        financesDao.getTotalIncome(startDate, endDate) ?: 0.0

    override suspend fun getTotalExpenses(startDate: LocalDate, endDate: LocalDate): Double =
        financesDao.getTotalExpenses(startDate, endDate) ?: 0.0

    override suspend fun getMedicationExpenses(startDate: LocalDate, endDate: LocalDate): Double =
        financesDao.getMedicationExpenses(startDate, endDate) ?: 0.0

    override fun getActiveBudgets(): Flow<List<Budget>> =
        financesDao.getActiveBudgets().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getBudgetByCategory(category: String): Budget? =
        financesDao.getBudgetByCategory(category)?.toDomain()

    override suspend fun insertBudget(budget: Budget) {
        financesDao.insertBudget(budget.toEntity())
    }

    override suspend fun updateBudget(budget: Budget) {
        financesDao.updateBudget(budget.toEntity())
    }

    override suspend fun getSpentInCategory(category: String, startDate: LocalDate, endDate: LocalDate): Double =
        financesDao.getSpentInCategory(category, startDate, endDate) ?: 0.0
}
