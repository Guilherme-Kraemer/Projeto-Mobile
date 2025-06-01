package com.mypills.features.reminders.domain.model

import kotlinx.datetime.*

data class Reminder(
    val id: String,
    val title: String,
    val description: String?,
    val type: ReminderType,
    val scheduledTime: Instant,
    val isRecurring: Boolean = false,
    val recurringPattern: RecurringPattern?,
    val isCompleted: Boolean = false,
    val completedAt: Instant?,
    val priority: ReminderPriority = ReminderPriority.MEDIUM,
    val notificationEnabled: Boolean = true,
    val medicationId: String?,
    val createdAt: Instant,
    val nextOccurrence: Instant? = null
) {
    val isOverdue: Boolean
        get() = !isCompleted && scheduledTime < Clock.System.now()
    
    val isUpcoming: Boolean
        get() = !isCompleted && scheduledTime > Clock.System.now() && 
                (scheduledTime.toEpochMilliseconds() - Clock.System.now().toEpochMilliseconds()) <= 3600000 // 1 hour
    
    val timeUntil: String
        get() {
            val now = Clock.System.now()
            val diffMillis = scheduledTime.toEpochMilliseconds() - now.toEpochMilliseconds()
            val diffMinutes = (diffMillis / 60000).toInt()
            
            return when {
                diffMinutes < 0 -> "Atrasado"
                diffMinutes == 0 -> "Agora"
                diffMinutes < 60 -> "${diffMinutes}min"
                diffMinutes < 1440 -> "${diffMinutes / 60}h ${diffMinutes % 60}min"
                else -> "${diffMinutes / 1440}d"
            }
        }
}

enum class ReminderType {
    MEDICATION, APPOINTMENT, TASK, CUSTOM, SHOPPING, FINANCE, EXERCISE
}

enum class ReminderPriority {
    LOW, MEDIUM, HIGH, URGENT
}

data class RecurringPattern(
    val type: RecurrenceType,
    val interval: Int = 1, // Every X days/weeks/months
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    val dayOfMonth: Int? = null,
    val endDate: LocalDate? = null,
    val maxOccurrences: Int? = null
)

enum class RecurrenceType {
    DAILY, WEEKLY, MONTHLY, YEARLY, CUSTOM
}

data class NotificationSettings(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val ledEnabled: Boolean = true,
    val customSound: String? = null,
    val snoozeEnabled: Boolean = true,
    val snoozeDuration: Int = 10, // minutes
    val autoMarkComplete: Boolean = false,
    val persistentNotification: Boolean = false
)
