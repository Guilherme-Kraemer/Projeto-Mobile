package com.mypills.core.database

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mypills.core.database.dao.*
import com.mypills.core.database.entities.*

/**
 * Banco de dados principal da aplicação My Pills
 * 
 * Utiliza Room para persistência local com SQLite
 * Inclui todas as entidades e DAOs da aplicação
 */
@Database(
    entities = [
        // Medicamentos
        MedicationEntity::class,
        MedicationScheduleEntity::class,
        MedicationLogEntity::class,
        
        // Lembretes
        ReminderEntity::class,
        ReminderSettingsEntity::class,
        
        // Finanças (serão implementadas depois)
        // FinancialAccountEntity::class,
        // FinancialTransactionEntity::class,
        
        // Transporte (serão implementadas depois)
        // BusRouteEntity::class,
        // BusStopEntity::class,
        
        // Compras (serão implementadas depois)
        // ShoppingListEntity::class,
        // ShoppingItemEntity::class,
        
        // Analytics Local
        // AppUsageEntity::class
    ],
    version = 1,
    exportSchema = true, // Para versionamento e migrações
    autoMigrations = [
        // Migrações automáticas futuras
    ]
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    
    // DAOs para acesso aos dados
    abstract fun medicationDao(): MedicationDao
    abstract fun reminderDao(): ReminderDao
    
    // DAOs que serão implementados depois
    // abstract fun financeDao(): FinanceDao
    // abstract fun transportDao(): TransportDao
    // abstract fun shoppingDao(): ShoppingDao
    // abstract fun analyticsDao(): AnalyticsDao
    
    companion object {
        const val DATABASE_NAME = "mypills_database"
    }
}

/**
 * Conversores de tipos para Room
 * Converte tipos complexos para tipos que o SQLite pode armazenar
 */
class DatabaseConverters {
    
    // ===== Converters para kotlinx.datetime =====
    
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilliseconds()
    
    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? = 
        timestamp?.let { Instant.fromEpochMilliseconds(it) }
    
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()
    
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = 
        dateString?.let { LocalDate.parse(it) }
    
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()
    
    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? = 
        timeString?.let { LocalTime.parse(it) }
    
    // ===== Converters para Enums =====
    
    @TypeConverter
    fun fromMedicationStatus(status: MedicationStatus): String = status.name
    
    @TypeConverter
    fun toMedicationStatus(statusString: String): MedicationStatus = 
        MedicationStatus.valueOf(statusString)
    
    @TypeConverter
    fun fromReminderType(type: ReminderType): String = type.name
    
    @TypeConverter
    fun toReminderType(typeString: String): ReminderType = 
        ReminderType.valueOf(typeString)
    
    @TypeConverter
    fun fromReminderPriority(priority: ReminderPriority): String = priority.name
    
    @TypeConverter
    fun toReminderPriority(priorityString: String): ReminderPriority = 
        ReminderPriority.valueOf(priorityString)
    
    // ===== Converters para Collections =====
    
    @TypeConverter
    fun fromDaysOfWeek(days: Set<DayOfWeek>): String = 
        days.joinToString(",") { it.name }
    
    @TypeConverter
    fun toDaysOfWeek(daysString: String): Set<DayOfWeek> = 
        if (daysString.isBlank()) emptySet()
        else daysString.split(",").map { DayOfWeek.valueOf(it) }.toSet()
    
    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString(",")
    
    @TypeConverter
    fun toStringList(listString: String): List<String> = 
        if (listString.isBlank()) emptyList()
        else listString.split(",")
}

/**
 * Migrations para versionamento do banco de dados
 * Cada mudança na estrutura precisa de uma migration
 */
object DatabaseMigrations {
    
    // Exemplo de migration da versão 1 para 2 (quando necessário)
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Exemplo: adicionar nova coluna
            // database.execSQL("ALTER TABLE medications ADD COLUMN new_field TEXT")
        }
    }
    
    // Lista de todas as migrations
    val ALL_MIGRATIONS = arrayOf<Migration>(
        // MIGRATION_1_2 // Será adicionado quando necessário
    )
}

/**
 * Callback para operações no banco de dados
 * Útil para inicializar dados ou fazer limpezas
 */
class AppDatabaseCallback : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Banco criado pela primeira vez
        // Aqui podemos inserir dados iniciais se necessário
    }
    
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        // Banco aberto (toda vez que a aplicação inicia)
        // Útil para limpezas ou verificações
    }
}