package com.mypills.core.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    
    suspend fun saveFileFromUri(
        context: Context,
        uri: Uri,
        fileName: String,
        subdirectory: String = "backups"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                ?: return@withContext Result.failure(Exception("Cannot open input stream"))
            
            val directory = File(context.getExternalFilesDir(null), subdirectory)
            if (!directory.exists()) directory.mkdirs()
            
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            Result.success(file.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun readFileAsString(filePath: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                return@withContext Result.failure(Exception("File does not exist"))
            }
            Result.success(file.readText())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun writeStringToFile(content: String, filePath: String): Result<Unit> = 
        withContext(Dispatchers.IO) {
            try {
                val file = File(filePath)
                file.parentFile?.mkdirs()
                file.writeText(content)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    fun getFileSize(filePath: String): Long {
        return try {
            File(filePath).length()
        } catch (e: Exception) {
            0L
        }
    }
    
    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }
}
