@ProvidedTypeConverter
class DatabaseConverters {
    
    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    // Instant conversions
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = 
        instant?.toEpochMilliseconds()
    
    @TypeConverter
    fun toInstant(timestamp: Long?): Instant? = 
        timestamp?.let { Instant.fromEpochMilliseconds(it) }
    
    // LocalDate conversions
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()
    
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = 
        dateString?.let { LocalDate.parse(it) }
    
    // LocalTime conversions
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.toString()
    
    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? = 
        timeString?.let { LocalTime.parse(it) }
    
    // Enum conversions
    @TypeConverter
    fun fromMedicationStatus(status: MedicationStatus): String = status.name
    
    @TypeConverter
    fun toMedicationStatus(statusString: String): MedicationStatus = 
        MedicationStatus.valueOf(statusString)
    
    // Set<DayOfWeek> conversions
    @TypeConverter
    fun fromDaysOfWeek(days: Set<DayOfWeek>): String = 
        json.encodeToString(days.map { it.name })
    
    @TypeConverter
    fun toDaysOfWeek(daysString: String): Set<DayOfWeek> = 
        try {
            json.decodeFromString<List<String>>(daysString)
                .map { DayOfWeek.valueOf(it) }
                .toSet()
        } catch (e: Exception) {
            emptySet()
        }
    
    // List<String> conversions
    @TypeConverter
    fun fromStringList(list: List<String>): String = 
        json.encodeToString(list)
    
    @TypeConverter
    fun toStringList(listString: String): List<String> = 
        try {
            json.decodeFromString(listString)
        } catch (e: Exception) {
            emptyList()
        }
}