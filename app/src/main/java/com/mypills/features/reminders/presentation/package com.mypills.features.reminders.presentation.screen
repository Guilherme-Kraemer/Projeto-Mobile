package com.mypills.features.reminders.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.mypills.features.reminders.presentation.viewmodel.RemindersViewModel
import com.mypills.features.reminders.presentation.viewmodel.ReminderTab
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    navController: NavController,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with adherence rate
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Lembretes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Adesão: ${(uiState.adherenceRate * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (uiState.adherenceRate >= 0.8) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
                )
            }
            
            FloatingActionButton(
                onClick = { navController.navigate("add_reminder") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Lembrete")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick stats
        if (uiState.overdueReminders.isNotEmpty() || uiState.todayReminders.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (uiState.overdueReminders.isNotEmpty()) {
                    StatusChip(
                        text = "${uiState.overdueReminders.size} Atrasado(s)",
                        status = "error"
                    )
                }
                
                if (uiState.todayReminders.isNotEmpty()) {
                    StatusChip(
                        text = "${uiState.todayReminders.size} Hoje",
                        status = "info"
                    )
                }
                
                if (uiState.upcomingReminders.isNotEmpty()) {
                    StatusChip(
                        text = "${uiState.upcomingReminders.size} Próximo(s)",
                        status = "warning"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tabs
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            ReminderTab.values().forEach { tab ->
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.setSelectedTab(tab) },
                    text = {
                        Text(
                            text = when (tab) {
                                ReminderTab.TODAY -> "Hoje (${uiState.todayReminders.size})"
                                ReminderTab.UPCOMING -> "Próximos (${uiState.upcomingReminders.size})"
                                ReminderTab.OVERDUE -> "Atrasados (${uiState.overdueReminders.size})"
                                ReminderTab.ALL -> "Todos (${uiState.allReminders.size})"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val remindersToShow = when (uiState.selectedTab) {
                ReminderTab.TODAY -> uiState.todayReminders
                ReminderTab.UPCOMING -> uiState.upcomingReminders
                ReminderTab.OVERDUE -> uiState.overdueReminders
                ReminderTab.ALL -> uiState.allReminders.filter { !it.isCompleted }
            }

            if (remindersToShow.isEmpty()) {
                EmptyRemindersState(tab = uiState.selectedTab)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(remindersToShow) { reminder ->
                        ReminderCard(
                            reminder = reminder,
                            onComplete = { viewModel.completeReminder(reminder.id) },
                            onSnooze = { viewModel.snoozeReminder(reminder.id) },
                            onClick = { /* Navigate to reminder details */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: Reminder,
    onComplete: () -> Unit,
    onSnooze: () -> Unit,
    onClick: () -> Unit
) {
    ModuleCard(module = "reminders") {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            when (reminder.type) {
                                ReminderType.MEDICATION -> Icons.Filled.Medication
                                ReminderType.APPOINTMENT -> Icons.Filled.Event
                                ReminderType.TASK -> Icons.Filled.Task
                                ReminderType.SHOPPING -> Icons.Filled.ShoppingCart
                                ReminderType.FINANCE -> Icons.Filled.AttachMoney
                                ReminderType.EXERCISE -> Icons.Filled.FitnessCenter
                                ReminderType.CUSTOM -> Icons.Filled.NotificationsActive
                            },
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = reminder.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    reminder.description?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = reminder.timeUntil,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    StatusChip(
                        text = when (reminder.priority) {
                            ReminderPriority.LOW -> "Baixa"
                            ReminderPriority.MEDIUM -> "Média"
                            ReminderPriority.HIGH -> "Alta"
                            ReminderPriority.URGENT -> "Urgente"
                        },
                        status = when (reminder.priority) {
                            ReminderPriority.LOW -> "info"
                            ReminderPriority.MEDIUM -> "warning"
                            ReminderPriority.HIGH -> "error"
                            ReminderPriority.URGENT -> "error"
                        }
                    )
                    
                    if (reminder.isOverdue) {
                        Spacer(modifier = Modifier.height(4.dp))
                        StatusChip("Atrasado", "error")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSnooze,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.Snooze,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Soneca")
                }
                
                Button(
                    onClick = onComplete,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Concluir")
                }
            }
        }
    }
}

@Composable
private fun EmptyRemindersState(tab: ReminderTab) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.NotificationsOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = when (tab) {
                ReminderTab.TODAY -> "Nenhum lembrete para hoje"
                ReminderTab.UPCOMING -> "Nenhum lembrete próximo"
                ReminderTab.OVERDUE -> "Nenhum lembrete atrasado"
                ReminderTab.ALL -> "Nenhum lembrete criado"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (tab == ReminderTab.ALL) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Crie seu primeiro lembrete",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}