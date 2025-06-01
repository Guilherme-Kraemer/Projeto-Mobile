package com.mypills.widgets.config

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun scheduleWidgetUpdates() {
        // Schedule periodic updates for all widgets
        val updateRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "widget_updates",
            ExistingPeriodicWorkPolicy.REPLACE,
            updateRequest
        )
    }

    fun updateAllWidgets() {
        scope.launch {
            try {
                val glanceManager = GlanceAppWidgetManager(context)
                
                // Update medication widget
                com.mypills.widgets.medication.MedicationWidget().updateAll(context)
                
                // Update finance widget
                com.mypills.widgets.finance.FinanceWidget().updateAll(context)
                
                // Update shopping widget
                com.mypills.widgets.shopping.ShoppingListWidget().updateAll(context)
                
                // Update transport widget
                com.mypills.widgets.transport.TransportWidget().updateAll(context)
                
            } catch (e: Exception) {
                // Log error but don't crash
                println("Error updating widgets: ${e.message}")
            }
        }
    }

    fun updateWidget(widgetClass: Class<out GlanceAppWidget>) {
        scope.launch {
            try {
                val widget = widgetClass.getDeclaredConstructor().newInstance()
                widget.updateAll(context)
            } catch (e: Exception) {
                println("Error updating specific widget: ${e.message}")
            }
        }
    }
}

@HiltWorker
class WidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val widgetManager: WidgetManager
) : CoroutineWorker(context, workerParams) {

    @AssistedFactory
    interface Factory {
        fun create(context: Context, params: WorkerParameters): WidgetUpdateWorker
    }

    override suspend fun doWork(): Result {
        return try {
            widgetManager.updateAllWidgets()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
