package com.mypills.core.utils

object Constants {
    
    // Database
    const val DATABASE_NAME = "mypills_database"
    
    // Preferences
    const val PREFERENCES_NAME = "app_settings"
    
    // WorkManager
    const val MEDICATION_REMINDER_WORK = "medication_reminder_work"
    const val WIDGET_UPDATE_WORK = "widget_update_work"
    const val DATA_BACKUP_WORK = "data_backup_work"
    
    // Notification IDs
    const val MEDICATION_NOTIFICATION_ID = 1001
    const val FINANCE_NOTIFICATION_ID = 1002
    const val TRANSPORT_NOTIFICATION_ID = 1003
    const val SHOPPING_NOTIFICATION_ID = 1004
    
    // Intent extras
    const val EXTRA_MEDICATION_ID = "medication_id"
    const val EXTRA_REMINDER_ID = "reminder_id"
    const val EXTRA_NAVIGATION_DESTINATION = "navigate_to"
    
    // Default values
    const val DEFAULT_MEDICATION_QUANTITY = 30
    const val DEFAULT_REMINDER_SNOOZE_MINUTES = 10
    const val DEFAULT_BUDGET_ALERT_THRESHOLD = 0.8
    
    // API endpoints (for when you implement external APIs)
    const val PRODUCT_API_BASE_URL = "https://world.openfoodfacts.org/api/v0/"
    const val TRANSPORT_API_BASE_URL = "https://api.transport.local/"
    const val PRICE_API_BASE_URL = "https://api.prices.local/"
    
    // File paths
    const val BACKUP_DIRECTORY = "backups"
    const val CACHE_DIRECTORY = "cache"
    const val IMAGES_DIRECTORY = "images"
    
    // Regex patterns
    const val PATTERN_BARCODE = "^\\d{8,14}$"
    const val PATTERN_TIME_24H = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
    const val PATTERN_DOSAGE = "^\\d+(\\.\\d+)?$"
}
