// app/src/main/java/com/mypills/features/medications/presentation/screen/MedicationsScreen.kt
package com.mypills.features.medications.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mypills.features.medications.presentation.viewmodel.MedicationsViewModel
import com.mypills.features.medications.presentation.viewmodel.MedicationTab
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip
import com.mypills.core.performance.rememberDebouncedSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsScreen(
    navController: NavController,
    viewModel: MedicationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    // Debounced search for better performance
    rememberDebouncedSearch(
        searchText = searchQuery,
        delayMillis = 300L,
        onSearch = { /* Search is handled in ViewModel */ }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Medicamentos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = { navController.navigate("add_medication") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Medicamento")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Buscar medicamentos") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Alerts row
        if (uiState.lowStockMedications.isNotEmpty() || uiState.expiringMedications.isNotEmpty()) {
            AlertsSection(
                lowStockCount = uiState.lowStockMedications.size,
                expiringCount = uiState.expiringMedications.size,
                onLowStockClick = { viewModel.setSelectedTab(MedicationTab.LOW_STOCK) },
                onExpiringClick = { viewModel.setSelectedTab(MedicationTab.EXPIRING) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tabs
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            MedicationTab.values().forEach { tab ->
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.setSelectedTab(tab) },
                    text = {
                        Text(
                            text = when (tab) {
                                MedicationTab.ALL -> "Todos (${uiState.medications.size})"
                                MedicationTab.LOW_STOCK -> "Estoque Baixo (${uiState.lowStockMedications.size})"
                                MedicationTab.EXPIRING -> "Vencendo (${uiState.expiringMedications.size})"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val medicationsToShow = when (uiState.selectedTab) {
                MedicationTab.ALL -> uiState.medications
                MedicationTab.LOW_STOCK -> uiState.lowStockMedications
                MedicationTab.EXPIRING -> uiState.expiringMedications
            }

            if (medicationsToShow.isEmpty()) {
                EmptyMedicationsState(
                    tab = uiState.selectedTab,
                    hasSearchQuery = searchQuery.isNotBlank()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(medicationsToShow) { medication ->
                        MedicationCard(
                            medication = medication,
                            onTakeMedication = { 
                                viewModel.takeMedication(medication.id, 1.0) 
                            },
                            onClick = { 
                                navController.navigate("medication_detail/${medication.id}") 
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Handle messages
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            viewModel.clearMessage()
        }
    }
    
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show error snackbar or toast
            viewModel.clearMessage()
        }
    }
}

@Composable
private fun AlertsSection(
    lowStockCount: Int,
    expiringCount: Int,
    onLowStockClick: () -> Unit,
    onExpiringClick: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (lowStockCount > 0) {
            item {
                Card(
                    onClick = onLowStockClick,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$lowStockCount medicamento(s) com estoque baixo",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        if (expiringCount > 0) {
            item {
                Card(
                    onClick = onExpiringClick,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$expiringCount medicamento(s) vencendo",
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicationCard(
    medication: Medication,
    onTakeMedication: () -> Unit,
    onClick: () -> Unit
) {
    ModuleCard(module = "medications") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    medication.description?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${medication.dosage} ${medication.unit}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        medication.pharmacy?.let { pharmacy ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• $pharmacy",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    if (medication.isLowStock) {
                        StatusChip("Estoque Baixo", "error")
                    } else if (medication.isExpiringSoon) {
                        StatusChip("Vencendo", "warning")
                    } else if (medication.isExpired) {
                        StatusChip("Vencido", "error")
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = medication.formattedPrice,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stock indicator
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Estoque",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${medication.currentQuantity}/${medication.totalQuantity}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                LinearProgressIndicator(
                    progress = medication.stockPercentage,
                    modifier = Modifier.fillMaxWidth(),
                    color = when {
                        medication.stockPercentage <= 0.2f -> MaterialTheme.colorScheme.error
                        medication.stockPercentage <= 0.5f -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expiry date
            medication.expirationDate?.let { expiryDate ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Vence em: $expiryDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    medication.daysUntilExpiry?.let { days ->
                        if (days >= 0) {
                            Text(
                                text = " ($days dias)",
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    days <= 7 -> MaterialTheme.colorScheme.error
                                    days <= 30 -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Detalhes")
                }
                
                Button(
                    onClick = onTakeMedication,
                    modifier = Modifier.weight(1f),
                    enabled = medication.currentQuantity > 0
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tomar")
                }
            }
        }
    }
}

@Composable
private fun EmptyMedicationsState(
    tab: MedicationTab,
    hasSearchQuery: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            if (hasSearchQuery) Icons.Filled.SearchOff else Icons.Filled.MedicationLiquid,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = when {
                hasSearchQuery -> "Nenhum medicamento encontrado"
                tab == MedicationTab.ALL -> "Nenhum medicamento cadastrado"
                tab == MedicationTab.LOW_STOCK -> "Nenhum medicamento com estoque baixo"
                tab == MedicationTab.EXPIRING -> "Nenhum medicamento vencendo"
                else -> "Lista vazia"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (!hasSearchQuery && tab == MedicationTab.ALL) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Adicione seu primeiro medicamento",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}