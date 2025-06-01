// app/src/main/java/com/mypills/features/medications/domain/usecase/MedicationUseCases.kt
package com.mypills.features.medications.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.features.medications.domain.model.*
import com.mypills.features.medications.domain.repository.MedicationRepository

class GetAllMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(): Flow<List<Medication>> = repository.getAllMedications()
}

class GetMedicationByIdUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(id: String): Medication? = repository.getMedicationById(id)
}

class GetLowStockMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(): Flow<List<Medication>> = repository.getLowStockMedications()
}

class GetExpiringMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(withinDays: Int = 30): Flow<List<Medication>> = 
        repository.getExpiringMedications(withinDays)
}

class AddMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medication: Medication) {
        val medicationWithTimestamp = medication.copy(
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )
        repository.insertMedication(medicationWithTimestamp)
    }
}

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medication: Medication) {
        val updatedMedication = medication.copy(updatedAt = Clock.System.now())
        repository.updateMedication(updatedMedication)
    }
}

class DeleteMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medication: Medication) {
        repository.deleteMedication(medication)
    }
}

class TakeMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, dosageAmount: Double = 1.0) {
        // Record the medication taking
        val now = Clock.System.now()
        val log = MedicationLog(
            id = java.util.UUID.randomUUID().toString(),
            medicationId = medicationId,
            scheduledTime = now,
            takenAt = now,
            dosageAmount = dosageAmount,
            status = com.mypills.core.database.entity.MedicationStatus.TAKEN,
            notes = null,
            sideEffects = null
        )
        
        repository.insertLog(log)
        
        // Decrease medication quantity
        repository.decreaseMedicationQuantity(medicationId, dosageAmount.toInt())
    }
}

class AddMedicationScheduleUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(
        medicationId: String,
        timeOfDay: LocalTime,
        dosageAmount: Double,
        daysOfWeek: Set<DayOfWeek>,
        instructions: String? = null
    ) {
        val schedule = MedicationSchedule(
            id = java.util.UUID.randomUUID().toString(),
            medicationId = medicationId,
            timeOfDay = timeOfDay,
            dosageAmount = dosageAmount,
            isActive = true,
            daysOfWeek = daysOfWeek,
            instructions = instructions,
            createdAt = Clock.System.now()
        )
        
        repository.insertSchedule(schedule)
    }
}

class GetMedicationSchedulesUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(medicationId: String): Flow<List<MedicationSchedule>> = 
        repository.getActiveSchedulesForMedication(medicationId)
}

class GetAllActiveSchedulesUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(): Flow<List<MedicationSchedule>> = repository.getAllActiveSchedules()
}

class GetMedicationLogsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    operator fun invoke(medicationId: String, limit: Int = 30): Flow<List<MedicationLog>> = 
        repository.getMedicationLogs(medicationId, limit)
}

class GetMedicationAdherenceUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, days: Int = 30): Double {
        val startDate = Clock.System.now().minus(DateTimePeriod(days = days))
        return repository.getAdherenceRate(medicationId, startDate)
    }
}

class MarkMedicationMissedUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, scheduledTime: Instant, reason: String? = null) {
        val log = MedicationLog(
            id = java.util.UUID.randomUUID().toString(),
            medicationId = medicationId,
            scheduledTime = scheduledTime,
            takenAt = null,
            dosageAmount = 0.0,
            status = com.mypills.core.database.entity.MedicationStatus.MISSED,
            notes = reason,
            sideEffects = null
        )
        
        repository.insertLog(log)
    }
}

class RefillMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, newQuantity: Int) {
        val medication = repository.getMedicationById(medicationId)
        medication?.let {
            val updatedMedication = it.copy(
                currentQuantity = newQuantity,
                updatedAt = Clock.System.now()
            )
            repository.updateMedication(updatedMedication)
        }
    }
}

class UpdateMedicationPriceUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, newPrice: Double, pharmacy: String? = null) {
        val medication = repository.getMedicationById(medicationId)
        medication?.let {
            val updatedMedication = it.copy(
                price = newPrice,
                pharmacy = pharmacy ?: it.pharmacy,
                updatedAt = Clock.System.now()
            )
            repository.updateMedication(updatedMedication)
        }
    }
}

class GetMedicationStatsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(): MedicationStats {
        val allMedications = repository.getAllMedications()
        val medications = mutableListOf<Medication>()
        allMedications.collect { medications.addAll(it) }
        
        val lowStock = medications.filter { it.isLowStock }
        val expiring = medications.filter { it.isExpiringSoon }
        val expired = medications.filter { it.isExpired }
        
        return MedicationStats(
            totalMedications = medications.size,
            lowStockCount = lowStock.size,
            expiringCount = expiring.size,
            expiredCount = expired.size,
            totalValue = medications.sumOf { it.price ?: 0.0 }
        )
    }
}

data class MedicationStats(
    val totalMedications: Int,
    val lowStockCount: Int,
    val expiringCount: Int,
    val expiredCount: Int,
    val totalValue: Double
)