package com.mypills.features.reminders.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import com.mypills.core.database.dao.RemindersDao
import com.mypills.features.reminders.domain.model.*
import com.mypills.features.reminders.domain.repository.RemindersRepository
import com.mypills.features.reminders.data.mapper.*

class RemindersRepositoryImpl @Inject constructor(
    private val remindersDao: RemindersDao
) : RemindersRepository {

    override fun getActiveReminders(): Flow<List<Reminder>> =
        remindersDao.getActiveReminders().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getUpcomingReminders(until: Instant): Flow<List<Reminder>> =
        remindersDao.getUpcomingReminders(until).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getRemindersByType(type: ReminderType): Flow<List<Reminder>> =
        remindersDao.getRemindersByType(type).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getMedicationReminders(medicationId: String): Flow<List<Reminder>> =
        remindersDao.getMedicationReminders(medicationId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getReminderById(id: String): Reminder? =
        remindersDao.getReminderById(id)?.toDomain()

    override suspend fun insertReminder(reminder: Reminder) {
        remindersDao.insertReminder(reminder.toEntity())
    }

    override suspend fun updateReminder(reminder: Reminder) {
        remindersDao.updateReminder(reminder.toEntity())
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        remindersDao.deleteReminder(reminder.toEntity())
    }

    override suspend fun markAsCompleted(id: String, completedAt: Instant) {
        remindersDao.markAsCompleted(id, completedAt)
    }

    override suspend fun snoozeReminder(id: String, newTime: Instant) {
        val reminder = remindersDao.getReminderById(id)
        if (reminder != null) {
            val updatedReminder = reminder.copy(scheduledTime = newTime)
            remindersDao.updateReminder(updatedReminder)
        }
    }

    override suspend fun getMedicationReminderCount(startDate: Instant, endDate: Instant): Int =
        remindersDao.getMedicationReminderCount(startDate, endDate)

    override suspend fun getCompletedMedicationReminderCount(startDate: Instant, endDate: Instant): Int =
        remindersDao.getCompletedMedicationReminderCount(startDate, endDate)
}
