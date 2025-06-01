package com.mypills.features.reminders.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import com.mypills.features.reminders.domain.model.*

interface RemindersRepository {
    fun getActiveReminders(): Flow<List<Reminder>>
    fun getUpcomingReminders(until: Instant): Flow<List<Reminder>>
    fun getRemindersByType(type: ReminderType): Flow<List<Reminder>>
    fun getMedicationReminders(medicationId: String): Flow<List<Reminder>>
    suspend fun getReminderById(id: String): Reminder?
    suspend fun insertReminder(reminder: Reminder)
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun markAsCompleted(id: String, completedAt: Instant)
    suspend fun snoozeReminder(id: String, newTime: Instant)
    suspend fun getMedicationReminderCount(startDate: Instant, endDate: Instant): Int
    suspend fun getCompletedMedicationReminderCount(startDate: Instant, endDate: Instant): Int
}
