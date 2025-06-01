package com.mypills.features.shopping.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mypills.features.shopping.presentation.viewmodel.ShoppingViewModel
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    navController: NavController,
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

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
                text = "Lista de Compras",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nova Lista")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.shoppingLists) { list ->
                    ShoppingListCard(
                        list = list,
                        onListClick = { viewModel.setSelectedList(list.id) },
                        onOptimize = { viewModel.optimizeList(list.id) }
                    )
                }
                
                if (uiState.shoppingLists.isEmpty()) {
                    item {
                        EmptyShoppingListsState()
                    }
                }
            }
        }
    }

    // Create List Dialog
    if (showCreateDialog) {
        CreateShoppingListDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, budget ->
                viewModel.createShoppingList(name, budget)
                showCreateDialog = false
            }
        )
    }

    // Budget Analysis Bottom Sheet
    uiState.budgetAnalysis?.let { analysis ->
        BudgetAnalysisBottomSheet(
            analysis = analysis,
            onDismiss = { 
                // Clear analysis from state
            }
        )
    }
}

@Composable
private fun ShoppingListCard(
    list: ShoppingList,
    onListClick: () -> Unit,
    onOptimize: () -> Unit
) {
    ModuleCard(module = "shopping") {
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
                        text = list.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Orçamento: ${list.formattedBudget}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Text(
                        text = "Estimado: ${list.formattedEstimatedCost}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (list.isOverBudget) MaterialTheme.colorScheme.error
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    if (list.isCompleted) {
                        StatusChip("Concluída", "success")
                    } else if (list.isOverBudget) {
                        StatusChip("Acima do Orçamento", "error")
                    } else {
                        StatusChip("Ativa", "info")
                    }
                    
                    list.budget?.let { budget ->
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = (list.budgetUsage).toFloat().coerceAtMost(1f),
                            modifier = Modifier.width(80.dp),
                            color = if (list.isOverBudget) MaterialTheme.colorScheme.error
                                   else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOptimize,
                    modifier = Modifier.weight(1f),
                    enabled = !list.isCompleted
                ) {
                    Icon(
                        Icons.Filled.Lightbulb,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Otimizar")
                }
                
                Button(
                    onClick = onListClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Abrir")
                }
            }
        }
    }
}

@Composable
private fun CreateShoppingListDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Double?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Lista de Compras") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Lista") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("Orçamento (R$) - Opcional") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val budgetValue = budget.toDoubleOrNull()
                        onCreate(name, budgetValue)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BudgetAnalysisBottomSheet(
    analysis: BudgetAnalysis,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Análise de Orçamento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Budget overview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BudgetMetric(
                    label = "Orçamento",
                    value = "R$ %.2f".format(analysis.totalBudget),
                    color = MaterialTheme.colorScheme.primary
                )
                
                BudgetMetric(
                    label = "Usado",
                    value = "R$ %.2f".format(analysis.usedBudget),
                    color = if (analysis.usedBudget > analysis.totalBudget) 
                        MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
                
                BudgetMetric(
                    label = "Restante",
                    value = "R$ %.2f".format(analysis.remainingBudget),
                    color = if (analysis.remainingBudget >= 0) 
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            if (analysis.recommendedReductions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Recomendações para Reduzir Custos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                analysis.recommendedReductions.take(3).forEach { recommendation ->
                    RecommendationCard(recommendation = recommendation)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (analysis.alternativeProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Produtos Alternativos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                analysis.alternativeProducts.take(3).forEach { alternative ->
                    AlternativeProductCard(alternative = alternative)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BudgetMetric(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun RecommendationCard(recommendation: BudgetRecommendation) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = recommendation.itemName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = recommendation.recommendedAction,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Economia: R$ %.2f".format(recommendation.potentialSavings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AlternativeProductCard(alternative: ProductAlternative) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "${alternative.originalItem} → ${alternative.alternativeProduct}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Em ${alternative.store}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Economia: R$ %.2f".format(alternative.priceDifference),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyShoppingListsState() {
    ModuleCard(module = "shopping") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Nenhuma lista de compras",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "Crie sua primeira lista inteligente",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}