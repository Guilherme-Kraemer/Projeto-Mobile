package com.mypills.features.reminders.data.mapper

import com.mypills.core.database.entity.ReminderEntity
import com.mypills.features.reminders.domain.model.*

fun ReminderEntity.toDomain(): Reminder = Reminder(
    id = id,
    title = title,
    description = description,
    type = type,
    scheduledTime = scheduledTime,
    isRecurring = isRecurring,
    recurringPattern = null, // Would need to parse from recurringPattern string
    isCompleted = isCompleted,
    completedAt = completedAt,
    priority = priority,
    notificationEnabled = notificationEnabled,
    medicationId = medicationId,
    createdAt = createdAt
)

fun Reminder.toEntity(): ReminderEntity = ReminderEntity(
    id = id,
    title = title,
    description = description,
    type = type,
    scheduledTime = scheduledTime,
    isRecurring = isRecurring,
    recurringPattern = recurringPattern?.toString(), // Simplified serialization
    isCompleted = isCompleted,
    completedAt = completedAt,
    priority = priority,
    notificationEnabled = notificationEnabled,
    medicationId = medicationId,
    createdAt = createdAt
)
