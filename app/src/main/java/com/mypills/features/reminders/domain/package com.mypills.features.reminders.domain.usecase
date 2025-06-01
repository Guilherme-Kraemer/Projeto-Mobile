package com.mypills.features.reminders.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.features.reminders.domain.model.*
import com.mypills.features.reminders.domain.repository.RemindersRepository

class GetActiveRemindersUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    operator fun invoke(): Flow<List<Reminder>> = repository.getActiveReminders()
}

class GetUpcomingRemindersUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    operator fun invoke(hours: Int = 24): Flow<List<Reminder>> {
        val until = Clock.System.now().plus(DateTimePeriod(hours = hours))
        return repository.getUpcomingReminders(until)
    }
}

class CreateReminderUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    suspend operator fun invoke(reminder: Reminder): List<Reminder> {
        val remindersToCreate = if (reminder.isRecurring && reminder.recurringPattern != null) {
            generateRecurringReminders(reminder)
        } else {
            listOf(reminder)
        }
        
        remindersToCreate.forEach { repository.insertReminder(it) }
        return remindersToCreate
    }
    
    private fun generateRecurringReminders(baseReminder: Reminder): List<Reminder> {
        val pattern = baseReminder.recurringPattern ?: return listOf(baseReminder)
        val reminders = mutableListOf<Reminder>()
        val startDateTime = baseReminder.scheduledTime.toLocalDateTime(TimeZone.currentSystemDefault())
        
        var currentDateTime = startDateTime
        var occurrenceCount = 0
        
        while (occurrenceCount < (pattern.maxOccurrences ?: 365)) { // Max 1 year if no limit
            if (pattern.endDate != null && currentDateTime.date > pattern.endDate) break
            
            val reminderTime = currentDateTime.toInstant(TimeZone.currentSystemDefault())
            
            reminders.add(
                baseReminder.copy(
                    id = "${baseReminder.id}_${occurrenceCount}",
                    scheduledTime = reminderTime,
                    nextOccurrence = if (occurrenceCount == 0) calculateNextOccurrence(currentDateTime, pattern) else null
                )
            )
            
            currentDateTime = calculateNextOccurrence(currentDateTime, pattern)
            occurrenceCount++
        }
        
        return reminders
    }
    
    private fun calculateNextOccurrence(current: LocalDateTime, pattern: RecurringPattern): LocalDateTime {
        return when (pattern.type) {
            RecurrenceType.DAILY -> current.plus(DateTimePeriod(days = pattern.interval))
            RecurrenceType.WEEKLY -> current.plus(DateTimePeriod(days = pattern.interval * 7))
            RecurrenceType.MONTHLY -> current.plus(DateTimePeriod(months = pattern.interval))
            RecurrenceType.YEARLY -> current.plus(DateTimePeriod(years = pattern.interval))
            RecurrenceType.CUSTOM -> {
                if (pattern.daysOfWeek.isNotEmpty()) {
                    // Find next occurrence based on days of week
                    var nextDate = current.date.plus(DatePeriod(days = 1))
                    while (!pattern.daysOfWeek.contains(nextDate.dayOfWeek)) {
                        nextDate = nextDate.plus(DatePeriod(days = 1))
                    }
                    LocalDateTime(nextDate, current.time)
                } else {
                    current.plus(DateTimePeriod(days = 1))
                }
            }
        }
    }
}

class CompleteReminderUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    suspend operator fun invoke(reminderId: String) {
        repository.markAsCompleted(reminderId, Clock.System.now())
    }
}

class SnoozeReminderUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    suspend operator fun invoke(reminderId: String, minutes: Int = 10) {
        val newTime = Clock.System.now().plus(DateTimePeriod(minutes = minutes))
        repository.snoozeReminder(reminderId, newTime)
    }
}

class GetMedicationAdherenceUseCase @Inject constructor(
    private val repository: RemindersRepository
) {
    suspend operator fun invoke(days: Int = 30): Double {
        val startDate = Clock.System.now().minus(DateTimePeriod(days = days))
        val endDate = Clock.System.now()
        
        val totalReminders = repository.getMedicationReminderCount(startDate, endDate)
        val completedReminders = repository.getCompletedMedicationReminderCount(startDate, endDate)
        
        return if (totalReminders > 0) {
            completedReminders.toDouble() / totalReminders.toDouble()
        } else {
            0.0
        }
    }
}
