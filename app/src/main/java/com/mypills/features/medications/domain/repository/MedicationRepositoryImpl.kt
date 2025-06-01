// app/src/main/java/com/mypills/features/medications/data/repository/MedicationRepositoryImpl.kt
package com.mypills.features.medications.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.*
import com.mypills.core.database.dao.MedicationDao
import com.mypills.features.medications.domain.model.*
import com.mypills.features.medications.domain.repository.MedicationRepository
import com.mypills.features.medications.data.mapper.*

class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override fun getAllMedications(): Flow<List<Medication>> =
        medicationDao.getAllMedications().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getMedicationById(id: String): Medication? =
        medicationDao.getMedicationById(id)?.toDomain()

    override fun getLowStockMedications(): Flow<List<Medication>> =
        medicationDao.getLowStockMedications().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getExpiringMedications(withinDays: Int): Flow<List<Medication>> {
        val cutoffDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .plus(DatePeriod(days = withinDays))
        
        return medicationDao.getExpiringMedications(cutoffDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertMedication(medication: Medication) {
        medicationDao.insertMedication(medication.toEntity())
    }

    override suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(medication.toEntity())
    }

    override suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(medication.toEntity())
    }

    override suspend fun decreaseMedicationQuantity(medicationId: String, amount: Int) {
        medicationDao.decreaseQuantity(medicationId, amount)
    }

    override fun getActiveSchedulesForMedication(medicationId: String): Flow<List<MedicationSchedule>> =
        medicationDao.getActiveSchedulesForMedication(medicationId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getAllActiveSchedules(): Flow<List<MedicationSchedule>> =
        medicationDao.getAllActiveSchedules().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertSchedule(schedule: MedicationSchedule) {
        medicationDao.insertSchedule(schedule.toEntity())
    }

    override suspend fun updateSchedule(schedule: MedicationSchedule) {
        medicationDao.updateSchedule(schedule.toEntity())
    }

    override suspend fun deleteSchedule(schedule: MedicationSchedule) {
        medicationDao.deleteSchedule(schedule.toEntity())
    }

    override fun getMedicationLogs(medicationId: String, limit: Int): Flow<List<MedicationLog>> =
        medicationDao.getMedicationLogs(medicationId, limit).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getLogsBetweenDates(startDate: Instant, endDate: Instant): Flow<List<MedicationLog>> =
        medicationDao.getLogsBetweenDates(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun insertLog(log: MedicationLog) {
        medicationDao.insertLog(log.toEntity())
    }

    override suspend fun getAdherenceRate(medicationId: String, startDate: Instant): Double =
        medicationDao.getAdherenceRate(medicationId, startDate)
}