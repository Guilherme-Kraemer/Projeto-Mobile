package com.mypills.core.database.entities

import androidx.room.*
import kotlinx.datetime.*

/**
 * Entidade para lembretes gerais da aplicação
 * Pode ser lembrete de medicamento, consulta, tarefa, etc.
 */
@Entity(
    tableName = "reminders",
    indices = [
        Index("scheduled_time"),
        Index("type"),
        Index("is_completed"),
        Index("medication_id"),
        Index("priority")
    ]
)
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "description")
    val description: String?,
    
    @ColumnInfo(name = "type")
    val type: ReminderType,
    
    @ColumnInfo(name = "scheduled_time")
    val scheduledTime: Instant,
    
    @ColumnInfo(name = "is_recurring")
    val isRecurring: Boolean = false,
    
    @ColumnInfo(name = "recurring_pattern")
    val recurringPattern: String?, // JSON com padrão de recorrência
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    
    @ColumnInfo(name = "completed_at")
    val completedAt: Instant?,
    
    @ColumnInfo(name = "priority")
    val priority: ReminderPriority = ReminderPriority.MEDIUM,
    
    @ColumnInfo(name = "notification_enabled")
    val notificationEnabled: Boolean = true,
    
    @ColumnInfo(name = "medication_id")
    val medicationId: String?, // Se for lembrete de medicamento
    
    @ColumnInfo(name = "snooze_count")
    val snoozeCount: Int = 0, // Quantas vezes foi adiado
    
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant
)

/**
 * Tipos de lembrete disponíveis
 */
enum class ReminderType {
    MEDICATION,     // Lembrete de medicamento
    APPOINTMENT,    // Consulta médica
    TASK,          // Tarefa geral
    SHOPPING,      // Lista de compras
    FINANCE,       // Lembrete financeiro
    EXERCISE,      // Exercício/atividade física
    CUSTOM         // Lembrete personalizado
}

/**
 * Prioridades dos lembretes
 * Afeta a cor, som e persistência da notificação
 */
enum class ReminderPriority {
    LOW,      // Baixa prioridade - notificação simples
    MEDIUM,   // Média prioridade - notificação padrão
    HIGH,     // Alta prioridade - notificação insistente
    URGENT    // Urgente - notificação que não pode ser cancelada facilmente
}

/**
 * Entidade para configurações de notificação personalizadas
 * Permite customizar como cada lembrete é notificado
 */
@Entity(
    tableName = "reminder_settings",
    foreignKeys = [
        ForeignKey(
            entity = ReminderEntity::class,
            parentColumns = ["id"],
            childColumns = ["reminder_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("reminder_id")]
)
data class ReminderSettingsEntity(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "reminder_id")
    val reminderId: String,
    
    @ColumnInfo(name = "sound_enabled")
    val soundEnabled: Boolean = true,
    
    @ColumnInfo(name = "vibration_enabled")
    val vibrationEnabled: Boolean = true,
    
    @ColumnInfo(name = "led_enabled")
    val ledEnabled: Boolean = true,
    
    @ColumnInfo(name = "custom_sound")
    val customSound: String?, // URI do som personalizado
    
    @ColumnInfo(name = "snooze_enabled")
    val snoozeEnabled: Boolean = true,
    
    @ColumnInfo(name = "snooze_duration_minutes")
    val snoozeDurationMinutes: Int = 10,
    
    @ColumnInfo(name = "auto_mark_complete")
    val autoMarkComplete: Boolean = false, // Marcar como completo automaticamente
    
    @ColumnInfo(name = "persistent_notification")
    val persistentNotification: Boolean = false, // Notificação que não sai até ser marcada
    
    @ColumnInfo(name = "created_at")
    val createdAt: Instant
)