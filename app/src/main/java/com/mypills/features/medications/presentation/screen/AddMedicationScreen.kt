package com.mypills.features.medications.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mypills.features.medications.presentation.viewmodel.AddMedicationViewModel
import com.mypills.features.medications.presentation.component.BarcodeScannerScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddMedicationScreen(
    navController: NavController,
    viewModel: AddMedicationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showScanner by remember { mutableStateOf(false) }
    
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    
    if (showScanner && cameraPermissionState.status.isGranted) {
        BarcodeScannerScreen(
            onBarcodeScanned = { barcode ->
                viewModel.searchProductByBarcode(barcode)
                showScanner = false
            },
            onClose = { showScanner = false }
        )
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Adicionar Medicamento",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Scanner Button
        Card(
            onClick = {
                if (cameraPermissionState.status.isGranted) {
                    showScanner = true
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.QrCodeScanner,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Escanear Código de Barras",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::updateName,
            label = { Text("Nome do Medicamento*") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.nameError != null,
            supportingText = uiState.nameError?.let { { Text(it) } }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::updateDescription,
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.dosage,
                onValueChange = viewModel::updateDosage,
                label = { Text("Dosagem*") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.dosageError != null,
                supportingText = uiState.dosageError?.let { { Text(it) } }
            )

            OutlinedTextField(
                value = uiState.unit,
                onValueChange = viewModel::updateUnit,
                label = { Text("Unidade*") },
                modifier = Modifier.weight(1f),
                placeholder = { Text("mg, ml, g...") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.totalQuantity,
                onValueChange = viewModel::updateTotalQuantity,
                label = { Text("Quantidade Total*") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.quantityError != null,
                supportingText = uiState.quantityError?.let { { Text(it) } }
            )

            OutlinedTextField(
                value = uiState.currentQuantity,
                onValueChange = viewModel::updateCurrentQuantity,
                label = { Text("Quantidade Atual*") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.pharmacy,
            onValueChange = viewModel::updatePharmacy,
            label = { Text("Farmácia") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.price,
            onValueChange = viewModel::updatePrice,
            label = { Text("Preço (R$)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            leadingIcon = { Text("R$") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.prescription,
                onCheckedChange = viewModel::updatePrescription
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Medicamento sob prescrição médica")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = viewModel::saveMedication,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Salvar Medicamento")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
    
    // Handle navigation after successful save
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navController.popBackStack()
        }
    }
}

// ViewModel para AddMedicationScreen
@dagger.hilt.android.lifecycle.HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase,
    private val getProductByBarcodeUseCase: GetProductByBarcodeUseCase
) : androidx.lifecycle.ViewModel() {

    private val _uiState = MutableStateFlow(AddMedicationUiState())
    val uiState: StateFlow<AddMedicationUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name, nameError = null)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateDosage(dosage: String) {
        _uiState.value = _uiState.value.copy(dosage = dosage, dosageError = null)
    }

    fun updateUnit(unit: String) {
        _uiState.value = _uiState.value.copy(unit = unit)
    }

    fun updateTotalQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(totalQuantity = quantity, quantityError = null)
    }

    fun updateCurrentQuantity(quantity: String) {
        _uiState.value = _uiState.value.copy(currentQuantity = quantity)
    }

    fun updatePharmacy(pharmacy: String) {
        _uiState.value = _uiState.value.copy(pharmacy = pharmacy)
    }

    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    fun updatePrescription(prescription: Boolean) {
        _uiState.value = _uiState.value.copy(prescription = prescription)
    }

    fun searchProductByBarcode(barcode: String) {
        androidx.lifecycle.viewModelScope.launch {
            try {
                val result = getProductByBarcodeUseCase(barcode)
                result.getOrNull()?.let { medication ->
                    _uiState.value = _uiState.value.copy(
                        name = medication.name,
                        description = medication.description ?: "",
                        dosage = medication.dosage,
                        unit = medication.unit,
                        barcode = barcode
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun saveMedication() {
        if (!validateFields()) return

        androidx.lifecycle.viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val medication = createMedicationFromState()
                addMedicationUseCase(medication)
                _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao salvar medicamento: ${e.message}"
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val state = _uiState.value
        var hasErrors = false

        if (state.name.isBlank()) {
            _uiState.value = _uiState.value.copy(nameError = "Nome é obrigatório")
            hasErrors = true
        }

        if (state.dosage.isBlank()) {
            _uiState.value = _uiState.value.copy(dosageError = "Dosagem é obrigatória")
            hasErrors = true
        }

        if (state.totalQuantity.isBlank() || state.totalQuantity.toIntOrNull() == null) {
            _uiState.value = _uiState.value.copy(quantityError = "Quantidade deve ser um número")
            hasErrors = true
        }

        return !hasErrors
    }

    private fun createMedicationFromState(): Medication {
        val state = _uiState.value
        val now = kotlinx.datetime.Clock.System.now()
        
        return Medication(
            id = java.util.UUID.randomUUID().toString(),
            name = state.name.trim(),
            description = state.description.trim().takeIf { it.isNotBlank() },
            dosage = state.dosage.trim(),
            unit = state.unit.trim(),
            totalQuantity = state.totalQuantity.toInt(),
            currentQuantity = state.currentQuantity.toIntOrNull() ?: state.totalQuantity.toInt(),
            expirationDate = null, // TODO: Add expiration date picker
            barcode = state.barcode,
            imageUrl = null,
            price = state.price.toDoubleOrNull(),
            pharmacy = state.pharmacy.trim().takeIf { it.isNotBlank() },
            prescription = state.prescription,
            notes = null,
            createdAt = now,
            updatedAt = now
        )
    }
}

data class AddMedicationUiState(
    val name: String = "",
    val description: String = "",
    val dosage: String = "",
    val unit: String = "mg",
    val totalQuantity: String = "",
    val currentQuantity: String = "",
    val pharmacy: String = "",
    val price: String = "",
    val prescription: Boolean = false,
    val barcode: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val nameError: String? = null,
    val dosageError: String? = null,
    val quantityError: String? = null
)