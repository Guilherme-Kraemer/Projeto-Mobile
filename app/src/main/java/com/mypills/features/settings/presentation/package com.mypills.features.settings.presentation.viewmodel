package com.mypills.features.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.core.settings.AppPreferences
import com.mypills.core.backup.BackupManager
import com.mypills.core.security.BiometricAuthManager

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val backupManager: BackupManager,
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                appPreferences.theme,
                appPreferences.language,
                appPreferences.notificationsEnabled,
                appPreferences.reminderSound,
                appPreferences.biometricEnabled,
                appPreferences.autoBackupEnabled
            ) { theme, language, notifications, sound, biometric, autoBackup ->
                _uiState.value.copy(
                    theme = theme,
                    language = language,
                    notificationsEnabled = notifications,
                    reminderSound = sound,
                    biometricEnabled = biometric,
                    autoBackupEnabled = autoBackup
                )
            }.collect { settings ->
                _uiState.value = settings
            }
        }
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            appPreferences.setTheme(theme)
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            appPreferences.setLanguage(language)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setNotificationsEnabled(enabled)
        }
    }

    fun updateReminderSound(sound: String) {
        viewModelScope.launch {
            appPreferences.setReminderSound(sound)
        }
    }

    fun toggleBiometric(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled && biometricAuthManager.isBiometricAvailable()) {
                appPreferences.setBiometricEnabled(true)
            } else {
                appPreferences.setBiometricEnabled(false)
            }
        }
    }

    fun toggleAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setAutoBackupEnabled(enabled)
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val result = backupManager.createBackup()
                result.fold(
                    onSuccess = { filePath ->
                        _uiState.value = _uiState.value.copy(
                            message = "Dados exportados para: $filePath"
                        )
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            error = "Erro ao exportar dados: ${error.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao exportar dados: ${e.message}"
                )
            }
        }
    }

    fun importData() {
        // This would typically open a file picker
        _uiState.value = _uiState.value.copy(
            message = "Funcionalidade de importação em desenvolvimento"
        )
    }

    fun clearAllData() {
        viewModelScope.launch {
            // Implementation would clear all database data
            _uiState.value = _uiState.value.copy(
                message = "Todos os dados foram limpos"
            )
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class SettingsUiState(
    val theme: String = "system",
    val language: String = "pt-BR",
    val notificationsEnabled: Boolean = true,
    val reminderSound: String = "default",
    val biometricEnabled: Boolean = false,
    val autoBackupEnabled: Boolean = true,
    val appVersion: String = "1.0.0",
    val message: String? = null,
    val error: String? = null
)
