package com.mypills.widgets.data

import androidx.glance.appwidget.GlanceAppWidget
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.mypills.features.medications.domain.usecase.GetAllMedicationsUseCase
import com.mypills.features.reminders.domain.usecase.GetUpcomingRemindersUseCase
import com.mypills.features.finances.domain.usecase.GetFinancialSummaryUseCase
import com.mypills.features.shopping.domain.usecase.GetActiveShoppingListsUseCase
import com.mypills.features.transport.domain.usecase.GetNearbyStopsUseCase

@ViewModelScoped
class WidgetDataProvider @Inject constructor(
    private val getMedicationsUseCase: GetAllMedicationsUseCase,
    private val getUpcomingRemindersUseCase: GetUpcomingRemindersUseCase,
    private val getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    private val getActiveShoppingListsUseCase: GetActiveShoppingListsUseCase
) {

    suspend fun getMedicationWidgetData(): MedicationWidgetData {
        val medications = getMedicationsUseCase().first()
        val upcomingReminders = getUpcomingRemindersUseCase(24).first()
        
        return MedicationWidgetData(
            totalMedications = medications.size,
            lowStockCount = medications.count { it.isLowStock },
            todayReminders = upcomingReminders.take(3),
            adherenceRate = 0.85 // Calculate from actual data
        )
    }

    suspend fun getFinanceWidgetData(): FinanceWidgetData {
        val startDate = kotlinx.datetime.Clock.System.now()
            .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            .minus(kotlinx.datetime.DatePeriod(days = 30))
        val endDate = kotlinx.datetime.Clock.System.now()
            .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
        
        val summary = getFinancialSummaryUseCase(startDate, endDate)
        
        return FinanceWidgetData(
            totalIncome = summary.totalIncome,
            totalExpenses = summary.totalExpenses,
            medicationExpenses = summary.medicationExpenses,
            balance = summary.balance
        )
    }

    suspend fun getShoppingWidgetData(): ShoppingWidgetData {
        val lists = getActiveShoppingListsUseCase().first()
        val activeList = lists.firstOrNull()
        
        return ShoppingWidgetData(
            hasActiveList = activeList != null,
            budget = activeList?.budget,
            totalCost = activeList?.totalEstimatedCost ?: 0.0,
            itemCount = 0, // Would need to fetch items
            completedItems = 0
        )
    }
}

data class MedicationWidgetData(
    val totalMedications: Int,
    val lowStockCount: Int,
    val todayReminders: List<com.mypills.features.reminders.domain.model.Reminder>,
    val adherenceRate: Double
)

data class FinanceWidgetData(
    val totalIncome: Double,
    val totalExpenses: Double,
    val medicationExpenses: Double,
    val balance: Double
)

data class ShoppingWidgetData(
    val hasActiveList: Boolean,
    val budget: Double?,
    val totalCost: Double,
    val itemCount: Int,
    val completedItems: Int
)