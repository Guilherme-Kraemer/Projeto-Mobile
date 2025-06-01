package com.mypills.core.error

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorReporter @Inject constructor() {
    
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
        // In production, send to crash reporting service like Firebase Crashlytics
    }

    fun logWarning(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }

    fun logInfo(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }
}

sealed class MyPillsError : Exception() {
    object NetworkError : MyPillsError()
    object DatabaseError : MyPillsError()
    object ValidationError : MyPillsError()
    data class UnknownError(override val message: String) : MyPillsError()
}
