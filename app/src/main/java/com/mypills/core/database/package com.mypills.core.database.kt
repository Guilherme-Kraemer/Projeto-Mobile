package com.mypills.core.database.entity

import androidx.room.*
import kotlinx.datetime.*

// 💊 MEDICATIONS MODULE
@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val dosage: String,
    val unit: String, // mg, ml, comprimidos, etc
    val totalQuantity: Int,
    val currentQuantity: Int,
    val expirationDate: LocalDate?,
    val barcode: String?,
    val imageUrl: String?,
    val price: Double?,
    val pharmacy: String?,
    val prescription: Boolean = false,
    val notes: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Entity(
    tableName = "medication_schedules",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId")]
)
data class MedicationScheduleEntity(
    @PrimaryKey val id: String,
    val medicationId: String,
    val timeOfDay: LocalTime,
    val dosageAmount: Double,
    val isActive: Boolean = true,
    val daysOfWeek: Set<DayOfWeek>, // Conversor personalizado necessário
    val instructions: String?,
    val createdAt: Instant
)

@Entity(
    tableName = "medication_logs",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId"), Index("takenAt")]
)
data class MedicationLogEntity(
    @PrimaryKey val id: String,
    val medicationId: String,
    val scheduledTime: Instant,
    val takenAt: Instant?,
    val dosageAmount: Double,
    val status: MedicationStatus,
    val notes: String?,
    val sideEffects: String?
)

enum class MedicationStatus {
    SCHEDULED, TAKEN, MISSED, SKIPPED, DELAYED
}

// 💰 FINANCES MODULE
@Entity(tableName = "financial_accounts")
data class FinancialAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val currency: String = "BRL",
    val isActive: Boolean = true,
    val createdAt: Instant
)

enum class AccountType {
    CHECKING, SAVINGS, CREDIT_CARD, CASH, INVESTMENT
}

@Entity(
    tableName = "financial_transactions",
    foreignKeys = [
        ForeignKey(
            entity = FinancialAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("accountId"), Index("date"), Index("category")]
)
data class FinancialTransactionEntity(
    @PrimaryKey val id: String,
    val accountId: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val subcategory: String?,
    val description: String,
    val date: LocalDate,
    val isRecurring: Boolean = false,
    val recurringPeriod: String?, // monthly, weekly, etc
    val tags: List<String> = emptyList(),
    val medicationRelated: Boolean = false,
    val createdAt: Instant
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val amount: Double,
    val period: BudgetPeriod,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val isActive: Boolean = true,
    val alertThreshold: Double = 0.8, // 80%
    val createdAt: Instant
)

enum class BudgetPeriod {
    WEEKLY, MONTHLY, QUARTERLY, YEARLY
}

// 🚌 TRANSPORT MODULE
@Entity(tableName = "bus_routes")
data class BusRouteEntity(
    @PrimaryKey val id: String,
    val routeNumber: String,
    val routeName: String,
    val company: String,
    val startLocation: String,
    val endLocation: String,
    val distance: Double?, // km
    val estimatedDuration: Int?, // minutes
    val fare: Double?,
    val isActive: Boolean = true,
    val lastUpdated: Instant
)

@Entity(
    tableName = "bus_stops",
    indices = [Index("latitude"), Index("longitude")]
)
data class BusStopEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val hasAccessibility: Boolean = false,
    val amenities: List<String> = emptyList() // shelter, bench, etc
)

@Entity(
    tableName = "route_stops",
    foreignKeys = [
        ForeignKey(
            entity = BusRouteEntity::class,
            parentColumns = ["id"],
            childColumns = ["routeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BusStopEntity::class,
            parentColumns = ["id"],
            childColumns = ["stopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routeId"), Index("stopId")]
)
data class RouteStopEntity(
    @PrimaryKey val id: String,
    val routeId: String,
    val stopId: String,
    val sequence: Int,
    val estimatedTime: Int // minutes from start
)

@Entity(tableName = "favorite_routes")
data class FavoriteRouteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val fromStopId: String,
    val toStopId: String,
    val routeIds: List<String>, // Multiple routes possible
    val createdAt: Instant
)

// ⏰ REMINDERS MODULE
@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val type: ReminderType,
    val scheduledTime: Instant,
    val isRecurring: Boolean = false,
    val recurringPattern: String?, // daily, weekly, monthly
    val isCompleted: Boolean = false,
    val completedAt: Instant?,
    val priority: ReminderPriority = ReminderPriority.MEDIUM,
    val notificationEnabled: Boolean = true,
    val medicationId: String?, // Link to medication if applicable
    val createdAt: Instant
)

enum class ReminderType {
    MEDICATION, APPOINTMENT, TASK, CUSTOM, SHOPPING, FINANCE
}

enum class ReminderPriority {
    LOW, MEDIUM, HIGH, URGENT
}

// 🛒 SHOPPING MODULE
@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val budget: Double?,
    val isCompleted: Boolean = false,
    val completedAt: Instant?,
    val totalEstimatedCost: Double = 0.0,
    val actualCost: Double = 0.0,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId"), Index("category")]
)
data class ShoppingItemEntity(
    @PrimaryKey val id: String,
    val listId: String,
    val name: String,
    val category: String,
    val quantity: Double = 1.0,
    val unit: String = "un", // un, kg, L, etc
    val estimatedPrice: Double?,
    val actualPrice: Double?,
    val brand: String?,
    val store: String?,
    val isCompleted: Boolean = false,
    val isPriority: Boolean = false,
    val barcode: String?,
    val notes: String?,
    val createdAt: Instant
)

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
    @PrimaryKey val id: String,
    val productName: String,
    val barcode: String?,
    val store: String,
    val price: Double,
    val date: LocalDate,
    val location: String?, // City or neighborhood
    val source: String, // manual, api, scraping
    val createdAt: Instant
)

// 🤖 ASSISTANT MODULE
@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Instant,
    val lastMessageAt: Instant,
    val isActive: Boolean = true
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("conversationId"), Index("timestamp")]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Instant,
    val messageType: MessageType = MessageType.TEXT,
    val metadata: String? = null // JSON for additional data
)

enum class MessageType {
    TEXT, IMAGE, MEDICATION_INFO, PRICE_SUGGESTION, ROUTE_SUGGESTION
}

// 📊 ANALYTICS & WIDGETS
@Entity(tableName = "app_usage")
data class AppUsageEntity(
    @PrimaryKey val id: String,
    val feature: String,
    val action: String,
    val metadata: String?, // JSON
    val timestamp: Instant
)

@Entity(tableName = "widget_configs")
data class WidgetConfigEntity(
    @PrimaryKey val widgetId: Int,
    val type: WidgetType,
    val configuration: String, // JSON configuration
    val lastUpdated: Instant
)

enum class WidgetType {
    MEDICATION_SUMMARY, FINANCE_OVERVIEW, SHOPPING_LIST, NEXT_BUS
}