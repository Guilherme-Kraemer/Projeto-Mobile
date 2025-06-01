package com.mypills.core.performance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Debounced search for better performance
@Composable
fun rememberDebouncedSearch(
    searchText: String,
    delayMillis: Long = 300L,
    onSearch: (String) -> Unit
) {
    LaunchedEffect(searchText) {
        delay(delayMillis)
        onSearch(searchText)
    }
}

// Memory-efficient image loading
@Singleton
class ImageCacheManager @Inject constructor() {
    // Implementation for image caching
    private val memoryCache = mutableMapOf<String, ByteArray>()
    private val maxCacheSize = 10 * 1024 * 1024 // 10MB

    fun cacheImage(url: String, data: ByteArray) {
        if (getCurrentCacheSize() + data.size <= maxCacheSize) {
            memoryCache[url] = data
        }
    }

    fun getImage(url: String): ByteArray? = memoryCache[url]

    private fun getCurrentCacheSize(): Int = memoryCache.values.sumOf { it.size }

    fun clearCache() {
        memoryCache.clear()
    }
}

// Database optimization
@Singleton
class DatabaseOptimizer @Inject constructor(
    private val database: com.mypills.core.database.AppDatabase
) {
    suspend fun optimizeDatabase() {
        // Run database maintenance
        database.clearAllTables() // Only for testing
        // Add other optimization queries here
    }

    suspend fun vacuum() {
        // VACUUM operation to reclaim space
        database.openHelper.writableDatabase.execSQL("VACUUM")
    }
}
