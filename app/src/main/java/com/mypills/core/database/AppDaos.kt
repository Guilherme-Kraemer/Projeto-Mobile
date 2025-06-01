package com.mypills.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.core.database.entity.*

// 💊 MEDICATIONS DAO
@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: String): MedicationEntity?

    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE currentQuantity <= 10 ORDER BY currentQuantity ASC")
    fun getLowStockMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE expirationDate <= :date ORDER BY expirationDate ASC")
    fun getExpiringMedications(date: LocalDate): Flow<List<MedicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    @Query("UPDATE medications SET currentQuantity = currentQuantity - :amount WHERE id = :medicationId")
    suspend fun decreaseQuantity(medicationId: String, amount: Int)

    @Query("SELECT * FROM medication_schedules WHERE medicationId = :medicationId AND isActive = 1")
    fun getActiveSchedulesForMedication(medicationId: String): Flow<List<MedicationScheduleEntity>>

    @Query("SELECT * FROM medication_schedules WHERE isActive = 1 ORDER BY timeOfDay ASC")
    fun getAllActiveSchedules(): Flow<List<MedicationScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: MedicationScheduleEntity)

    @Update
    suspend fun updateSchedule(schedule: MedicationScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: MedicationScheduleEntity)

    @Query("SELECT * FROM medication_logs WHERE medicationId = :medicationId ORDER BY scheduledTime DESC LIMIT :limit")
    fun getMedicationLogs(medicationId: String, limit: Int = 30): Flow<List<MedicationLogEntity>>

    @Query("SELECT * FROM medication_logs WHERE scheduledTime >= :startDate AND scheduledTime <= :endDate ORDER BY scheduledTime DESC")
    fun getLogsBetweenDates(startDate: Instant, endDate: Instant): Flow<List<MedicationLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedicationLogEntity)

    @Query("SELECT COUNT(*) * 1.0 / (SELECT COUNT(*) FROM medication_logs WHERE medicationId = :medicationId AND scheduledTime >= :startDate) FROM medication_logs WHERE medicationId = :medicationId AND status = 'TAKEN' AND scheduledTime >= :startDate")
    suspend fun getAdherenceRate(medicationId: String, startDate: Instant): Double
}

// 💰 FINANCES DAO
@Dao
interface FinancesDao {
    @Query("SELECT * FROM financial_accounts WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveAccounts(): Flow<List<FinancialAccountEntity>>

    @Query("SELECT * FROM financial_accounts WHERE id = :id")
    suspend fun getAccountById(id: String): FinancialAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: FinancialAccountEntity)

    @Update
    suspend fun updateAccount(account: FinancialAccountEntity)

    @Query("SELECT * FROM financial_transactions WHERE accountId = :accountId ORDER BY date DESC LIMIT :limit")
    fun getTransactionsByAccount(accountId: String, limit: Int = 50): Flow<List<FinancialTransactionEntity>>

    @Query("SELECT * FROM financial_transactions WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getTransactionsBetweenDates(startDate: LocalDate, endDate: LocalDate): Flow<List<FinancialTransactionEntity>>

    @Query("SELECT * FROM financial_transactions WHERE category = :category AND date >= :startDate ORDER BY date DESC")
    fun getTransactionsByCategory(category: String, startDate: LocalDate): Flow<List<FinancialTransactionEntity>>

    @Query("SELECT SUM(amount) FROM financial_transactions WHERE type = 'INCOME' AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalIncome(startDate: LocalDate, endDate: LocalDate): Double?

    @Query("SELECT SUM(ABS(amount)) FROM financial_transactions WHERE type = 'EXPENSE' AND date >= :startDate AND date <= :endDate")
    suspend fun getTotalExpenses(startDate: LocalDate, endDate: LocalDate): Double?

    @Query("SELECT SUM(ABS(amount)) FROM financial_transactions WHERE type = 'EXPENSE' AND medicationRelated = 1 AND date >= :startDate AND date <= :endDate")
    suspend fun getMedicationExpenses(startDate: LocalDate, endDate: LocalDate): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: FinancialTransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: FinancialTransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: FinancialTransactionEntity)

    @Query("SELECT * FROM budgets WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE category = :category AND isActive = 1")
    suspend fun getBudgetByCategory(category: String): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("SELECT SUM(ABS(amount)) FROM financial_transactions WHERE type = 'EXPENSE' AND category = :category AND date >= :startDate AND date <= :endDate")
    suspend fun getSpentInCategory(category: String, startDate: LocalDate, endDate: LocalDate): Double?
}

// 🚌 TRANSPORT DAO
@Dao
interface TransportDao {
    @Query("SELECT * FROM bus_routes WHERE isActive = 1 ORDER BY routeNumber ASC")
    fun getAllActiveRoutes(): Flow<List<BusRouteEntity>>

    @Query("SELECT * FROM bus_routes WHERE id = :id")
    suspend fun getRouteById(id: String): BusRouteEntity?

    @Query("SELECT * FROM bus_routes WHERE routeNumber LIKE '%' || :query || '%' OR routeName LIKE '%' || :query || '%'")
    fun searchRoutes(query: String): Flow<List<BusRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: BusRouteEntity)

    @Update
    suspend fun updateRoute(route: BusRouteEntity)

    @Query("SELECT * FROM bus_stops ORDER BY name ASC")
    fun getAllStops(): Flow<List<BusStopEntity>>

    @Query("SELECT * FROM bus_stops WHERE name LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    fun searchStops(query: String): Flow<List<BusStopEntity>>

    @Query("SELECT * FROM bus_stops WHERE latitude BETWEEN :minLat AND :maxLat AND longitude BETWEEN :minLng AND :maxLng")
    suspend fun getNearbyStops(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): List<BusStopEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStop(stop: BusStopEntity)

    @Query("""
        SELECT bs.* FROM bus_stops bs
        INNER JOIN route_stops rs ON bs.id = rs.stopId
        WHERE rs.routeId = :routeId
        ORDER BY rs.sequence ASC
    """)
    suspend fun getStopsForRoute(routeId: String): List<BusStopEntity>

    @Query("SELECT * FROM favorite_routes ORDER BY name ASC")
    fun getFavoriteRoutes(): Flow<List<FavoriteRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRoute(favorite: FavoriteRouteEntity)

    @Delete
    suspend fun deleteFavoriteRoute(favorite: FavoriteRouteEntity)
}

// ⏰ REMINDERS DAO
@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminders WHERE isCompleted = 0 ORDER BY scheduledTime ASC")
    fun getActiveReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE scheduledTime <= :until AND isCompleted = 0 ORDER BY scheduledTime ASC")
    fun getUpcomingReminders(until: Instant): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE type = :type AND isCompleted = 0 ORDER BY scheduledTime ASC")
    fun getRemindersByType(type: ReminderType): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE medicationId = :medicationId ORDER BY scheduledTime DESC")
    fun getMedicationReminders(medicationId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("UPDATE reminders SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun markAsCompleted(id: String, completedAt: Instant)

    @Query("SELECT COUNT(*) FROM reminders WHERE type = 'MEDICATION' AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    suspend fun getMedicationReminderCount(startDate: Instant, endDate: Instant): Int

    @Query("SELECT COUNT(*) FROM reminders WHERE type = 'MEDICATION' AND isCompleted = 1 AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    suspend fun getCompletedMedicationReminderCount(startDate: Instant, endDate: Instant): Int
}

// 🛒 SHOPPING DAO
@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_lists WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentShoppingLists(limit: Int = 10): Flow<List<ShoppingListEntity>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getShoppingListById(id: String): ShoppingListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(list: ShoppingListEntity)

    @Update
    suspend fun updateShoppingList(list: ShoppingListEntity)

    @Delete
    suspend fun deleteShoppingList(list: ShoppingListEntity)

    @Query("SELECT * FROM shopping_items WHERE listId = :listId ORDER BY isPriority DESC, name ASC")
    fun getItemsForList(listId: String): Flow<List<ShoppingItemEntity>>

    @Query("SELECT * FROM shopping_items WHERE listId = :listId AND isCompleted = 0 ORDER BY isPriority DESC, name ASC")
    fun getActiveItemsForList(listId: String): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItemEntity)

    @Update
    suspend fun updateShoppingItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItemEntity)

    @Query("SELECT AVG(price) FROM price_history WHERE productName = :productName AND date >= :sinceDate")
    suspend fun getAveragePrice(productName: String, sinceDate: LocalDate): Double?

    @Query("SELECT MIN(price) FROM price_history WHERE productName = :productName AND date >= :sinceDate")
    suspend fun getLowestPrice(productName: String, sinceDate: LocalDate): Double?

    @Query("SELECT * FROM price_history WHERE productName = :productName ORDER BY date DESC LIMIT :limit")
    fun getPriceHistory(productName: String, limit: Int = 30): Flow<List<PriceHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceHistory(priceHistory: PriceHistoryEntity)

    @Query("SELECT SUM(estimatedPrice * quantity) FROM shopping_items WHERE listId = :listId")
    suspend fun getEstimatedTotal(listId: String): Double?

    @Query("SELECT SUM(actualPrice * quantity) FROM shopping_items WHERE listId = :listId AND actualPrice IS NOT NULL")
    suspend fun getActualTotal(listId: String): Double?
}

// 🤖 ASSISTANT DAO
@Dao
interface AssistantDao {
    @Query("SELECT * FROM conversations WHERE isActive = 1 ORDER BY lastMessageAt DESC")
    fun getActiveConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: String): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessage(conversationId: String): MessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("DELETE FROM conversations WHERE id = :conversationId")
    suspend fun deleteConversation(conversationId: String)
}

// 📊 ANALYTICS DAO
@Dao
interface AnalyticsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageEvent(usage: AppUsageEntity)

    @Query("SELECT COUNT(*) FROM app_usage WHERE feature = :feature AND timestamp >= :since")
    suspend fun getFeatureUsageCount(feature: String, since: Instant): Int

    @Query("SELECT * FROM widget_configs WHERE widgetId = :widgetId")
    suspend fun getWidgetConfig(widgetId: Int): WidgetConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidgetConfig(config: WidgetConfigEntity)

    @Update
    suspend fun updateWidgetConfig(config: WidgetConfigEntity)

    @Query("DELETE FROM widget_configs WHERE widgetId = :widgetId")
    suspend fun deleteWidgetConfig(widgetId: Int)
}