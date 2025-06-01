package com.mypills.core.database.entities

import androidx.room.*
import kotlinx.datetime.*

/**
 * Entidade do banco de dados para medicamentos
 * Representa um medicamento cadastrado pelo usuário
 */
@Entity(
    tableName = "medications",
    indices = [
        Index(value = ["name"]),
        Index(value = ["barcode"]),
        Index(value = ["expirationDate"])
    ]
)
data class MedicationEntity(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String?,
    
    @ColumnInfo(name = "dosage")
    val dosage: String, // Ex: "500mg", "1ml"
    
    @ColumnInfo(name = "unit")
    val unit: String, // mg, ml, comprimidos, etc
    
    @ColumnInfo(name = "total_quantity")
    val totalQuantity: Int, // Quantidade total comprada
    
    @ColumnInfo(name = "current_quantity")
    val currentQuantity: Int, // Quantidade atual restante
    
    @ColumnInfo(name = "expiration_date")
    val expirationDate: LocalDate?,
    
    @ColumnInfo(name = "barcode")
    val barcode: String?, // Código de barras escaneado
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String?, // URL da imagem (local ou remota)
    
    @ColumnInfo(name = "price")
    val price: Double?, // Preço pago
    
    @ColumnInfo(name = "pharmacy")
    val pharmacy: String?, // Farmácia onde foi comprado
    
    @ColumnInfo(name = "prescription")
    val prescription: Boolean = false, // Requer receita
    
    @ColumnInfo(name = "notes")
    val notes: String?, // Observações do usuário
    
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant
) {
    /**
     * Calcula se o estoque está baixo
     * Considera baixo quando resta menos de 20% ou menos de 5 unidades
     */
    val isLowStock: Boolean
        get() = currentQuantity <= 5 || 
                (currentQuantity.toDouble() / totalQuantity) <= 0.2
    
    /**
     * Calcula se está próximo do vencimento
     * Considera próximo quando faltam menos de 30 dias
     */
    val isNearExpiration: Boolean
        get() = expirationDate?.let { expDate ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val daysUntilExpiration = expDate.toEpochDays() - today.toEpochDays()
            daysUntilExpiration in 1..30
        } ?: false
    
    /**
     * Verifica se o medicamento está vencido
     */
    val isExpired: Boolean
        get() = expirationDate?.let { expDate ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            expDate < today
        } ?: false
}

/**
 * Entidade para agendamentos de medicamentos
 * Define horários regulares para tomar medicamentos
 */
@Entity(
    tableName = "medication_schedules",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("medication_id"),
        Index("time_of_day"),
        Index("is_active")
    ]
)
data class MedicationScheduleEntity(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "medication_id")
    val medicationId: String,
    
    @ColumnInfo(name = "time_of_day")
    val timeOfDay: LocalTime, // Horário para tomar (ex: 08:00)
    
    @ColumnInfo(name = "dosage_amount")
    val dosageAmount: Double, // Quantidade por dose (ex: 1.0 para 1 comprimido)
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: Set<DayOfWeek>, // Dias da semana para tomar
    
    @ColumnInfo(name = "instructions")
    val instructions: String?, // Ex: "Tomar com água", "Após refeição"
    
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
)

/**
 * Entidade para log de medicamentos tomados
 * Registra quando o usuário tomou cada medicamento
 */
@Entity(
    tableName = "medication_logs",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medication_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("medication_id"),
        Index("scheduled_time"),
        Index("taken_at"),
        Index("status")
    ]
)
data class MedicationLogEntity(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "medication_id")
    val medicationId: String,
    
    @ColumnInfo(name = "scheduled_time")
    val scheduledTime: Instant, // Quando era para tomar
    
    @ColumnInfo(name = "taken_at")
    val takenAt: Instant?, // Quando realmente tomou (null se não tomou)
    
    @ColumnInfo(name = "dosage_amount")
    val dosageAmount: Double, // Quantidade tomada
    
    @ColumnInfo(name = "status")
    val status: MedicationStatus,
    
    @ColumnInfo(name = "notes")
    val notes: String?, // Observações do usuário
    
    @ColumnInfo(name = "side_effects")
    val sideEffects: String?, // Efeitos colaterais observados
    
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
)

/**
 * Status de um medicamento agendado
 */
enum class MedicationStatus {
    SCHEDULED,  // Agendado mas ainda não é hora
    PENDING,    // É hora de tomar
    TAKEN,      // Tomado no horário
    MISSED,     // Não tomado no horário
    SKIPPED,    // Pulado intencionalmente
    DELAYED     // Tomado com atraso
}