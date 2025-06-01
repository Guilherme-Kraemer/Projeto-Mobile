// app/src/main/java/com/mypills/features/medications/domain/model/Medication.kt
package com.mypills.features.medications.domain.model

import kotlinx.datetime.*

data class Medication(
    val id: String,
    val name: String,
    val description: String?,
    val dosage: String,
    val unit: String,
    val totalQuantity: Int,
    val currentQuantity: Int,
    val expirationDate: LocalDate?,
    val barcode: String?,
    val imageUrl: String?,
    val price: Double?,
    val pharmacy: String?,
    val prescription: Boolean = false,
    val notes: String?,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val isLowStock: Boolean
        get() = currentQuantity <= (totalQuantity * 0.2) // 20% or less
    
    val isExpiringSoon: Boolean
        get() = expirationDate?.let { expiry ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val daysUntilExpiry = expiry.toEpochDays() - today.toEpochDays()
            daysUntilExpiry in 0..30 // Expires in next 30 days
        } ?: false
    
    val isExpired: Boolean
        get() = expirationDate?.let { expiry ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            expiry < today
        } ?: false
    
    val stockPercentage: Float
        get() = if (totalQuantity > 0) currentQuantity.toFloat() / totalQuantity.toFloat() else 0f
    
    val formattedPrice: String
        get() = price?.let { "R$ %.2f".format(it) } ?: "Preço não informado"
    
    val daysUntilExpiry: Int?
        get() = expirationDate?.let { expiry ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            (expiry.toEpochDays() - today.toEpochDays()).toInt()
        }
}

data class MedicationSchedule(
    val id: String,
    val medicationId: String,
    val timeOfDay: LocalTime,
    val dosageAmount: Double,
    val isActive: Boolean = true,
    val daysOfWeek: Set<DayOfWeek>,
    val instructions: String?,
    val createdAt: Instant
) {
    val formattedTime: String
        get() = timeOfDay.toString()
    
    val daysOfWeekText: String
        get() = when {
            daysOfWeek.size == 7 -> "Todos os dias"
            daysOfWeek.containsAll(setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) &&
                    !daysOfWeek.contains(DayOfWeek.SATURDAY) && !daysOfWeek.contains(DayOfWeek.SUNDAY) -> "Dias úteis"
            daysOfWeek.containsAll(setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) &&
                    daysOfWeek.size == 2 -> "Fins de semana"
            else -> daysOfWeek.joinToString(", ") { 
                when (it) {
                    DayOfWeek.MONDAY -> "Seg"
                    DayOfWeek.TUESDAY -> "Ter"
                    DayOfWeek.WEDNESDAY -> "Qua"
                    DayOfWeek.THURSDAY -> "Qui"
                    DayOfWeek.FRIDAY -> "Sex"
                    DayOfWeek.SATURDAY -> "Sáb"
                    DayOfWeek.SUNDAY -> "Dom"
                }
            }
        }
}

data class MedicationLog(
    val id: String,
    val medicationId: String,
    val scheduledTime: Instant,
    val takenAt: Instant?,
    val dosageAmount: Double,
    val status: com.mypills.core.database.entity.MedicationStatus,
    val notes: String?,
    val sideEffects: String?
) {
    val wasOnTime: Boolean
        get() = takenAt?.let { taken ->
            val scheduledDateTime = scheduledTime.toLocalDateTime(TimeZone.currentSystemDefault())
            val takenDateTime = taken.toLocalDateTime(TimeZone.currentSystemDefault())
            val diff = takenDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() - 
                      scheduledDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            kotlin.math.abs(diff) <= 30 * 60 * 1000 // Within 30 minutes
        } ?: false
}

// Repository Interface
package com.mypills.features.medications.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import com.mypills.features.medications.domain.model.*

interface MedicationRepository {
    fun getAllMedications(): Flow<List<Medication>>
    suspend fun getMedicationById(id: String): Medication?
    fun getLowStockMedications(): Flow<List<Medication>>
    fun getExpiringMedications(withinDays: Int = 30): Flow<List<Medication>>
    suspend fun insertMedication(medication: Medication)
    suspend fun updateMedication(medication: Medication)
    suspend fun deleteMedication(medication: Medication)
    suspend fun decreaseMedicationQuantity(medicationId: String, amount: Int)
    
    fun getActiveSchedulesForMedication(medicationId: String): Flow<List<MedicationSchedule>>
    fun getAllActiveSchedules(): Flow<List<MedicationSchedule>>
    suspend fun insertSchedule(schedule: MedicationSchedule)
    suspend fun updateSchedule(schedule: MedicationSchedule)
    suspend fun deleteSchedule(schedule: MedicationSchedule)
    
    fun getMedicationLogs(medicationId: String, limit: Int = 30): Flow<List<MedicationLog>>
    fun getLogsBetweenDates(startDate: kotlinx.datetime.Instant, endDate: kotlinx.datetime.Instant): Flow<List<MedicationLog>>
    suspend fun insertLog(log: MedicationLog)
    suspend fun getAdherenceRate(medicationId: String, startDate: kotlinx.datetime.Instant): Double
}