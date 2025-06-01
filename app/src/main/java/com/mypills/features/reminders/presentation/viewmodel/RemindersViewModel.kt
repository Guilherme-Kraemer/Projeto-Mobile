package com.mypills.features.reminders.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject
import com.mypills.features.reminders.domain.model.*
import com.mypills.features.reminders.domain.usecase.*
import com.mypills.features.reminders.data.worker.ReminderScheduler

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val getActiveRemindersUseCase: GetActiveRemindersUseCase,
    private val getUpcomingRemindersUseCase: GetUpcomingRemindersUseCase,
    private val createReminderUseCase: CreateReminderUseCase,
    private val completeReminderUseCase: CompleteReminderUseCase,
    private val snoozeReminderUseCase: SnoozeReminderUseCase,
    private val getMedicationAdherenceUseCase: GetMedicationAdherenceUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(RemindersUiState())
    val uiState: StateFlow<RemindersUiState> = _uiState.asStateFlow()

    init {
        loadReminders()
        loadAdherence()
    }

    private fun loadReminders() {
        viewModelScope.launch {
            getActiveRemindersUseCase().collect { reminders ->
                val upcoming = reminders.filter { it.isUpcoming }
                val overdue = reminders.filter { it.isOverdue }
                val today = reminders.filter { 
                    val reminderDate = it.scheduledTime.toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    reminderDate == currentDate && !it.isCompleted
                }
                
                _uiState.value = _uiState.value.copy(
                    allReminders = reminders,
                    upcomingReminders = upcoming,
                    overdueReminders = overdue,
                    todayReminders = today,
                    isLoading = false
                )
            }
        }
    }

    private fun loadAdherence() {
        viewModelScope.launch {
            try {
                val adherence = getMedicationAdherenceUseCase(30)
                _uiState.value = _uiState.value.copy(adherenceRate = adherence)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun createReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                val createdReminders = createReminderUseCase(reminder)
                createdReminders.forEach { reminderScheduler.scheduleReminder(it) }
                
                _uiState.value = _uiState.value.copy(
                    message = "Lembrete criado com sucesso!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao criar lembrete: ${e.message}"
                )
            }
        }
    }

    fun completeReminder(reminderId: String) {
        viewModelScope.launch {
            try {
                completeReminderUseCase(reminderId)
                reminderScheduler.cancelReminder(reminderId)
                
                _uiState.value = _uiState.value.copy(
                    message = "Lembrete marcado como concluído!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao completar lembrete: ${e.message}"
                )
            }
        }
    }

    fun snoozeReminder(reminderId: String, minutes: Int = 10) {
        viewModelScope.launch {
            try {
                snoozeReminderUseCase(reminderId, minutes)
                // Reschedule the reminder
                _uiState.value.allReminders.find { it.id == reminderId }?.let { reminder ->
                    val snoozedReminder = reminder.copy(
                        scheduledTime = Clock.System.now().plus(DateTimePeriod(minutes = minutes))
                    )
                    reminderScheduler.scheduleReminder(snoozedReminder)
                }
                
                _uiState.value = _uiState.value.copy(
                    message = "Lembrete adiado por $minutes minutos"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao adiar lembrete: ${e.message}"
                )
            }
        }
    }

    fun setSelectedTab(tab: ReminderTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class RemindersUiState(
    val allReminders: List<Reminder> = emptyList(),
    val upcomingReminders: List<Reminder> = emptyList(),
    val overdueReminders: List<Reminder> = emptyList(),
    val todayReminders: List<Reminder> = emptyList(),
    val adherenceRate: Double = 0.0,
    val selectedTab: ReminderTab = ReminderTab.TODAY,
    val isLoading: Boolean = true,
    val error: String? = null,
    val message: String? = null
)

enum class ReminderTab {
    TODAY, UPCOMING, OVERDUE, ALL
}
