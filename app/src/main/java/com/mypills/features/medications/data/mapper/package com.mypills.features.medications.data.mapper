package com.mypills.features.medications.data.mapper

import com.mypills.core.database.entity.*
import com.mypills.features.medications.domain.model.*

fun MedicationEntity.toDomain(): Medication = Medication(
    id = id,
    name = name,
    description = description,
    dosage = dosage,
    unit = unit,
    totalQuantity = totalQuantity,
    currentQuantity = currentQuantity,
    expirationDate = expirationDate,
    barcode = barcode,
    imageUrl = imageUrl,
    price = price,
    pharmacy = pharmacy,
    prescription = prescription,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Medication.toEntity(): MedicationEntity = MedicationEntity(
    id = id,
    name = name,
    description = description,
    dosage = dosage,
    unit = unit,
    totalQuantity = totalQuantity,
    currentQuantity = currentQuantity,
    expirationDate = expirationDate,
    barcode = barcode,
    imageUrl = imageUrl,
    price = price,
    pharmacy = pharmacy,
    prescription = prescription,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun MedicationScheduleEntity.toDomain(): MedicationSchedule = MedicationSchedule(
    id = id,
    medicationId = medicationId,
    timeOfDay = timeOfDay,
    dosageAmount = dosageAmount,
    isActive = isActive,
    daysOfWeek = daysOfWeek,
    instructions = instructions,
    createdAt = createdAt
)

fun MedicationSchedule.toEntity(): MedicationScheduleEntity = MedicationScheduleEntity(
    id = id,
    medicationId = medicationId,
    timeOfDay = timeOfDay,
    dosageAmount = dosageAmount,
    isActive = isActive,
    daysOfWeek = daysOfWeek,
    instructions = instructions,
    createdAt = createdAt
)

fun MedicationLogEntity.toDomain(): MedicationLog = MedicationLog(
    id = id,
    medicationId = medicationId,
    scheduledTime = scheduledTime,
    takenAt = takenAt,
    dosageAmount = dosageAmount,
    status = status,
    notes = notes,
    sideEffects = sideEffects
)

fun MedicationLog.toEntity(): MedicationLogEntity = MedicationLogEntity(
    id = id,
    medicationId = medicationId,
    scheduledTime = scheduledTime,
    takenAt = takenAt,
    dosageAmount = dosageAmount,
    status = status,
    notes = notes,
    sideEffects = sideEffects
)