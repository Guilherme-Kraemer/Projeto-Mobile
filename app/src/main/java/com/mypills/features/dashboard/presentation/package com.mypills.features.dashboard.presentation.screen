package com.mypills.features.dashboard.presentation.screen

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
import androidx.navigation.NavController
import com.mypills.core.theme.getModuleColor
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Olá! 👋",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Como está se sentindo hoje?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { /* Settings */ }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Configurações"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick Actions
            item {
                QuickActionsSection(navController = navController)
            }
            
            // Today's Medications
            item {
                TodayMedicationsSection(navController = navController)
            }
            
            // Finance Summary
            item {
                FinanceSummarySection(navController = navController)
            }
            
            // Upcoming Reminders
            item {
                UpcomingRemindersSection(navController = navController)
            }
            
            // Transport Info
            item {
                TransportInfoSection(navController = navController)
            }
        }
    }
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Column {
        Text(
            text = "Ações Rápidas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quickActions) { action ->
                QuickActionCard(
                    action = action,
                    onClick = { navController.navigate(action.route) }
                )
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = getModuleColor(action.module).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.title,
                tint = getModuleColor(action.module),
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = action.title,
                style = MaterialTheme.typography.labelMedium,
                color = getModuleColor(action.module)
            )
        }
    }
}

@Composable
private fun TodayMedicationsSection(navController: NavController) {
    ModuleCard(module = "medications") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Medicamentos de Hoje",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(
                    onClick = { navController.navigate("medications") }
                ) {
                    Text("Ver todos")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Lista de medicamentos - mock data
            repeat(3) { index ->
                MedicationItem(
                    name = "Medicamento ${index + 1}",
                    time = "${8 + index * 4}:00",
                    status = if (index == 0) "taken" else "pending"
                )
                
                if (index < 2) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MedicationItem(
    name: String,
    time: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        StatusChip(
            text = if (status == "taken") "Tomado" else "Pendente",
            status = if (status == "taken") "success" else "warning"
        )
    }
}

@Composable
private fun FinanceSummarySection(navController: NavController) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resumo Financeiro",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(
                    onClick = { navController.navigate("finances") }
                ) {
                    Text("Detalhes")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FinanceItem(
                    label = "Gastos com Medicamentos",
                    value = "R$ 245,80",
                    subtitle = "Este mês"
                )
                
                FinanceItem(
                    label = "Orçamento Restante",
                    value = "R$ 154,20",
                    subtitle = "Para medicamentos"
                )
            }
        }
    }
}

@Composable
private fun FinanceItem(
    label: String,
    value: String,
    subtitle: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UpcomingRemindersSection(navController: NavController) {
    ModuleCard(module = "reminders") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Próximos Lembretes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(
                    onClick = { navController.navigate("reminders") }
                ) {
                    Text("Ver todos")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Próximo lembrete em 45 minutos",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tomar Vitamina D - 14:00",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransportInfoSection(navController: NavController) {
    ModuleCard(module = "transport") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transporte",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(
                    onClick = { navController.navigate("transport") }
                ) {
                    Text("Rotas")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Próximo ônibus: Linha 101",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Chega em 8 minutos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data classes para ações rápidas
data class QuickAction(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    val module: String
)

private val quickActions = listOf(
    QuickAction("Adicionar Remédio", Icons.Filled.Add, "add_medication", "medications"),
    QuickAction("Ver Gastos", Icons.Filled.TrendingUp, "finances", "finances"),
    QuickAction("Planar Rota", Icons.Filled.Route, "route_planner", "transport"),
    QuickAction("Lista de Compras", Icons.Filled.ShoppingCart, "shopping", "shopping"),
    QuickAction("Assistente", Icons.Filled.SmartToy, "assistant", "assistant")
)