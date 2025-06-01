package com.mypills.features.medications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.medications.domain.model.*
import com.mypills.features.medications.domain.usecase.*

@HiltViewModel
class MedicationsViewModel @Inject constructor(
    private val getAllMedicationsUseCase: GetAllMedicationsUseCase,
    private val getLowStockMedicationsUseCase: GetLowStockMedicationsUseCase,
    private val getExpiringMedicationsUseCase: GetExpiringMedicationsUseCase,
    private val takeMedicationUseCase: TakeMedicationUseCase,
    private val addMedicationUseCase: AddMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicationsUiState())
    val uiState: StateFlow<MedicationsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadMedications()
        loadLowStockMedications()
        loadExpiringMedications()
    }

    private fun loadMedications() {
        viewModelScope.launch {
            getAllMedicationsUseCase()
                .combine(searchQuery) { medications, query ->
                    if (query.isBlank()) {
                        medications
                    } else {
                        medications.filter { 
                            it.name.contains(query, ignoreCase = true) ||
                            it.description?.contains(query, ignoreCase = true) == true
                        }
                    }
                }
                .collect { medications ->
                    _uiState.value = _uiState.value.copy(
                        medications = medications,
                        isLoading = false
                    )
                }
        }
    }

    private fun loadLowStockMedications() {
        viewModelScope.launch {
            getLowStockMedicationsUseCase().collect { medications ->
                _uiState.value = _uiState.value.copy(
                    lowStockMedications = medications
                )
            }
        }
    }

    private fun loadExpiringMedications() {
        viewModelScope.launch {
            getExpiringMedicationsUseCase().collect { medications ->
                _uiState.value = _uiState.value.copy(
                    expiringMedications = medications
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun takeMedication(medicationId: String, dosageAmount: Double) {
        viewModelScope.launch {
            try {
                takeMedicationUseCase(medicationId, dosageAmount)
                // Mostrar sucesso
                _uiState.value = _uiState.value.copy(
                    message = "Medicamento tomado com sucesso!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao registrar medicamento: ${e.message}"
                )
            }
        }
    }

    fun addMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                addMedicationUseCase(medication)
                _uiState.value = _uiState.value.copy(
                    message = "Medicamento adicionado com sucesso!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao adicionar medicamento: ${e.message}"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    fun setSelectedTab(tab: MedicationTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
}

data class MedicationsUiState(
    val medications: List<Medication> = emptyList(),
    val lowStockMedications: List<Medication> = emptyList(),
    val expiringMedications: List<Medication> = emptyList(),
    val selectedTab: MedicationTab = MedicationTab.ALL,
    val isLoading: Boolean = true,
    val error: String? = null,
    val message: String? = null
)

enum class MedicationTab {
    ALL, LOW_STOCK, EXPIRING
}
