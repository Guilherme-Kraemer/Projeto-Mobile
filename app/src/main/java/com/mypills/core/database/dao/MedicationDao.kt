package com.mypills.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.*
import com.mypills.core.database.entities.*

/**
 * DAO para operações de medicamentos no banco de dados
 * 
 * Responsabilidades:
 * - CRUD de medicamentos
 * - Consultas específicas (estoque baixo, vencimento, etc.)
 * - Operações relacionadas a agendamentos e logs
 */
@Dao
interface MedicationDao {
    
    // ===== OPERAÇÕES BÁSICAS DE MEDICAMENTOS =====
    
    /**
     * Busca todos os medicamentos do usuário
     * Retorna Flow para observar mudanças em tempo real
     */
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<MedicationEntity>>
    
    /**
     * Busca medicamento por ID
     */
    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: String): MedicationEntity?
    
    /**
     * Busca medicamentos por nome (para busca/filtro)
     */
    @Query("""
        SELECT * FROM medications 
        WHERE name LIKE '%' || :searchQuery || '%' 
           OR description LIKE '%' || :searchQuery || '%'
        ORDER BY name ASC
    """)
    fun searchMedications(searchQuery: String): Flow<List<MedicationEntity>>
    
    /**
     * Insere novo medicamento
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)
    
    /**
     * Atualiza medicamento existente
     */
    @Update
    suspend fun updateMedication(medication: MedicationEntity)
    
    /**
     * Remove medicamento
     */
    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)
    
    // ===== CONSULTAS ESPECÍFICAS =====
    
    /**
     * Busca medicamentos com estoque baixo
     * Considera baixo: <= 5 unidades OU <= 20% do total
     */
    @Query("""
        SELECT * FROM medications 
        WHERE current_quantity <= 5 
           OR (current_quantity * 1.0 / total_quantity) <= 0.2
        ORDER BY current_quantity ASC
    """)
    fun getLowStockMedications(): Flow<List<MedicationEntity>>
    
    /**
     * Busca medicamentos próximos do vencimento
     * Considera próximo: vence nos próximos 30 dias
     */
    @Query("""
        SELECT * FROM medications 
        WHERE expiration_date IS NOT NULL 
          AND expiration_date <= :limitDate
          AND expiration_date > :today
        ORDER BY expiration_date ASC
    """)
    fun getExpiringMedications(
        today: LocalDate, 
        limitDate: LocalDate
    ): Flow<List<MedicationEntity>>
    
    /**
     * Busca medicamentos vencidos
     */
    @Query("""
        SELECT * FROM medications 
        WHERE expiration_date IS NOT NULL 
          AND expiration_date < :today
        ORDER BY expiration_date ASC
    """)
    fun getExpiredMedications(today: LocalDate): Flow<List<MedicationEntity>>
    
    /**
     * Busca medicamentos por código de barras
     */
    @Query("SELECT * FROM medications WHERE barcode = :barcode")
    suspend fun getMedicationByBarcode(barcode: String): MedicationEntity?
    
    // ===== OPERAÇÕES DE QUANTIDADE =====
    
    /**
     * Diminui quantidade atual do medicamento
     * Usado quando o usuário toma o medicamento
     */
    @Query("""
        UPDATE medications 
        SET current_quantity = current_quantity - :amount,
            updated_at = :timestamp
        WHERE id = :medicationId AND current_quantity >= :amount
    """)
    suspend fun decreaseQuantity(
        medicationId: String, 
        amount: Int, 
        timestamp: Instant
    ): Int // Retorna número de rows afetadas
    
    /**
     * Aumenta quantidade atual do medicamento
     * Usado quando o usuário compra mais do medicamento
     */
    @Query("""
        UPDATE medications 
        SET current_quantity = current_quantity + :amount,
            updated_at = :timestamp
        WHERE id = :medicationId
    """)
    suspend fun increaseQuantity(
        medicationId: String, 
        amount: Int, 
        timestamp: Instant
    )
    
    // ===== OPERAÇÕES DE AGENDAMENTO =====
    
    /**
     * Busca agendamentos ativos de um medicamento
     */
    @Query("""
        SELECT * FROM medication_schedules 
        WHERE medication_id = :medicationId AND is_active = 1
        ORDER BY time_of_day ASC
    """)
    fun getActiveSchedulesForMedication(medicationId: String): Flow<List<MedicationScheduleEntity>>
    
    /**
     * Busca todos os agendamentos ativos
     */
    @Query("""
        SELECT * FROM medication_schedules 
        WHERE is_active = 1 
        ORDER BY time_of_day ASC
    """)
    fun getAllActiveSchedules(): Flow<List<MedicationScheduleEntity>>
    
    /**
     * Insere novo agendamento
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: MedicationScheduleEntity)
    
    /**
     * Atualiza agendamento
     */
    @Update
    suspend fun updateSchedule(schedule: MedicationScheduleEntity)
    
    /**
     * Remove agendamento
     */
    @Delete
    suspend fun deleteSchedule(schedule: MedicationScheduleEntity)
    
    // ===== OPERAÇÕES DE LOG =====
    
    /**
     * Busca logs de medicamento (histórico de uso)
     */
    @Query("""
        SELECT * FROM medication_logs 
        WHERE medication_id = :medicationId 
        ORDER BY scheduled_time DESC 
        LIMIT :limit
    """)
    fun getMedicationLogs(
        medicationId: String, 
        limit: Int = 50
    ): Flow<List<MedicationLogEntity>>
    
    /**
     * Busca logs em um período específico
     */
    @Query("""
        SELECT * FROM medication_logs 
        WHERE scheduled_time >= :startDate 
          AND scheduled_time <= :endDate 
        ORDER BY scheduled_time DESC
    """)
    fun getLogsBetweenDates(
        startDate: Instant, 
        endDate: Instant
    ): Flow<List<MedicationLogEntity>>
    
    /**
     * Insere log de medicamento tomado
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedicationLogEntity)
    
    /**
     * Atualiza log existente
     */
    @Update
    suspend fun updateLog(log: MedicationLogEntity)
    
    // ===== ESTATÍSTICAS E ANÁLISES =====
    
    /**
     * Calcula taxa de adesão de um medicamento
     * Retorna valor entre 0.0 e 1.0
     */
    @Query("""
        SELECT 
            CAST(COUNT(CASE WHEN status = 'TAKEN' THEN 1 END) AS REAL) / 
            CAST(COUNT(*) AS REAL) as adherence_rate
        FROM medication_logs 
        WHERE medication_id = :medicationId 
          AND scheduled_time >= :startDate
          AND scheduled_time <= :endDate
    """)
    suspend fun getAdherenceRate(
        medicationId: String, 
        startDate: Instant, 
        endDate: Instant
    ): Double?
    
    /**
     * Conta total de medicamentos cadastrados
     */
    @Query("SELECT COUNT(*) FROM medications")
    suspend fun getTotalMedicationCount(): Int
    
    /**
     * Conta medicamentos ativos (com quantidade > 0)
     */
    @Query("SELECT COUNT(*) FROM medications WHERE current_quantity > 0")
    suspend fun getActiveMedicationCount(): Int
    
    // ===== TRANSAÇÕES COMPLEXAS =====
    
    /**
     * Registra que o usuário tomou um medicamento
     * Atualiza tanto o log quanto a quantidade
     */
    @Transaction
    suspend fun takeMedication(
        medicationId: String,
        dosageAmount: Double,
        scheduledTime: Instant,
        takenAt: Instant,
        notes: String? = null
    ) {
        // Insere log
        val log = MedicationLogEntity(
            id = java.util.UUID.randomUUID().toString(),
            medicationId = medicationId,
            scheduledTime = scheduledTime,
            takenAt = takenAt,
            dosageAmount = dosageAmount,
            status = MedicationStatus.TAKEN,
            notes = notes,
            sideEffects = null,
            createdAt = takenAt
        )
        insertLog(log)
        
        // Diminui quantidade se for comprimido/cápsula
        if (dosageAmount >= 1.0) {
            decreaseQuantity(medicationId, dosageAmount.toInt(), takenAt)
        }
    }
}