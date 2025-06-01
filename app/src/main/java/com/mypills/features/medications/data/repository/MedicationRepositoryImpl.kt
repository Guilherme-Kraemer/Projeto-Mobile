@Singleton
class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao,
    private val productApiService: ProductApiService
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
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val limitDate = today.plus(DatePeriod(days = withinDays))
        
        return medicationDao.getExpiringMedications(today, limitDate).map { entities ->
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
        val now = Clock.System.now()
        medicationDao.decreaseQuantity(medicationId, amount, now)
    }

    // Schedule operations
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

    // Log operations
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
        medicationDao.getAdherenceRate(medicationId, startDate, Clock.System.now()) ?: 0.0

    // Barcode scanning integration
    suspend fun searchMedicationByBarcode(barcode: String): Result<Medication> {
        return try {
            val response = productApiService.getProduct(barcode)
            if (response.isSuccessful && response.body()?.product != null) {
                val product = response.body()!!.product!!
                val medication = Medication(
                    id = java.util.UUID.randomUUID().toString(),
                    name = product.product_name ?: "Produto Desconhecido",
                    description = product.generic_name,
                    dosage = product.serving_size ?: "",
                    unit = "un", // Default unit
                    totalQuantity = 0,
                    currentQuantity = 0,
                    expirationDate = null,
                    barcode = barcode,
                    imageUrl = product.image_url,
                    price = null,
                    pharmacy = null,
                    prescription = false,
                    notes = null,
                    createdAt = Clock.System.now(),
                    updatedAt = Clock.System.now()
                )
                Result.success(medication)
            } else {
                Result.failure(Exception("Produto não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}