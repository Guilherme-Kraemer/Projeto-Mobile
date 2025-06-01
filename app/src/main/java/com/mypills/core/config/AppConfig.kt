package com.mypills.core.config

import com.mypills.BuildConfig

object AppConfig {
    const val APP_VERSION = BuildConfig.VERSION_NAME
    const val DATABASE_VERSION = 1
    const val PRIVACY_POLICY_URL = "https://mypills.app/privacy"
    const val TERMS_OF_SERVICE_URL = "https://mypills.app/terms"
    const val SUPPORT_EMAIL = "suporte@mypills.app"
    
    // Notification settings
    const val DEFAULT_SNOOZE_MINUTES = 10
    const val MEDICATION_REMINDER_ADVANCE_MINUTES = 5
    const val LOW_STOCK_THRESHOLD = 0.2 // 20%
    const val EXPIRY_WARNING_DAYS = 30
    
    // Financial settings
    const val DEFAULT_CURRENCY = "BRL"
    const val BUDGET_ALERT_THRESHOLD = 0.8 // 80%
    
    // Transport settings
    const val NEARBY_STOPS_RADIUS_KM = 1.0
    const val ARRIVAL_REFRESH_INTERVAL_SECONDS = 30
    
    // AI settings
    const val AI_CONFIDENCE_THRESHOLD = 0.7
    const val MAX_CONVERSATION_HISTORY = 50
    
    // Widget settings
    const val WIDGET_UPDATE_INTERVAL_MINUTES = 15
    const val MAX_WIDGET_ITEMS = 5
    
    // Development flags
    val isDebug: Boolean = BuildConfig.DEBUG
    val enableAnalytics: Boolean = !BuildConfig.DEBUG
    val enableCrashReporting: Boolean = !BuildConfig.DEBUG
    
    // Feature flags
    val enableOfflineAI: Boolean = true
    val enableBiometricAuth: Boolean = true
    val enableWidgets: Boolean = true
    val enableTransportFeature: Boolean = true
    val enableShoppingOptimization: Boolean = true
}