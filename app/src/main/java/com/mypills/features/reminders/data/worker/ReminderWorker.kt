package com.mypills.features.reminders.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.*
import com.mypills.features.reminders.domain.repository.RemindersRepository
import com.mypills.features.reminders.data.notification.NotificationManager

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val remindersRepository: RemindersRepository,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val reminderId = inputData.getString("reminder_id") ?: return Result.failure()
            val reminder = remindersRepository.getReminderById(reminderId)
            
            if (reminder != null && !reminder.isCompleted && reminder.notificationEnabled) {
                notificationManager.showReminderNotification(reminder)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(reminder: Reminder) {
        val delay = reminder.scheduledTime.toEpochMilliseconds() - Clock.System.now().toEpochMilliseconds()
        
        if (delay <= 0) return // Past reminder

        val inputData = workDataOf("reminder_id" to reminder.id)
        
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag("reminder_${reminder.id}")
            .build()

        workManager.enqueueUniqueWork(
            "reminder_${reminder.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(reminderId: String) {
        workManager.cancelUniqueWork("reminder_$reminderId")
    }

    fun rescheduleAllReminders(reminders: List<Reminder>) {
        reminders.forEach { reminder ->
            if (!reminder.isCompleted && reminder.scheduledTime > Clock.System.now()) {
                scheduleReminder(reminder)
            }
        }
    }
}
