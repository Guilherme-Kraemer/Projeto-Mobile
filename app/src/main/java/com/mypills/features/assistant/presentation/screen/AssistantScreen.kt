package com.mypills.features.assistant.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mypills.features.assistant.presentation.viewmodel.AssistantViewModel
import com.mypills.core.theme.ModuleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    navController: NavController,
    viewModel: AssistantViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messages by viewModel.currentMessages.collectAsState()
    val listState = rememberLazyListState()
    var messageText by remember { mutableStateOf("") }

    // Handle navigation events
    LaunchedEffect(uiState.navigationEvent) {
        uiState.navigationEvent?.let { event ->
            when (event) {
                "create_reminder" -> navController.navigate("add_reminder")
                "check_prices" -> navController.navigate("finances")
                "plan_route" -> navController.navigate("transport")
                else -> navController.navigate(event)
            }
            viewModel.clearNavigationEvent()
        }
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        TopAppBar(
            title = { 
                Column {
                    Text(
                        text = "Assistente IA",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (uiState.isTyping) {
                        Text(
                            text = "Digitando...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { viewModel.createNewConversation() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Nova Conversa")
                }
            }
        )

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    WelcomeMessage()
                }
            } else {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Suggested Action Card
        uiState.suggestedAction?.let { action ->
            SuggestedActionCard(
                action = action,
                onExecute = { viewModel.executeSuggestedAction(action) },
                onDismiss = { viewModel.dismissSuggestedAction() },
                modifier = Modifier.padding(16.dp)
            )
        }

        // Message Input
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Digite sua pergunta...") },
                    modifier = Modifier.weight(1f),
                    maxLines = 4,
                    enabled = !uiState.isTyping
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                FloatingActionButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(
                        if (uiState.isTyping) Icons.Filled.HourglassTop else Icons.Filled.Send,
                        contentDescription = "Enviar"
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeMessage() {
    ModuleCard(module = "assistant") {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.SmartToy,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Olá! Sou seu assistente virtual",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Text(
                text = "Posso ajudar com medicamentos, lembretes, preços e muito mais!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Algumas coisas que posso fazer:",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            
            val suggestions = listOf(
                "• Configurar lembretes de medicamentos",
                "• Verificar preços em farmácias",
                "• Encontrar rotas de ônibus",
                "• Organizar lista de compras",
                "• Tirar dúvidas sobre saúde"
            )
            
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            Icon(
                Icons.Filled.SmartToy,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(top = 4.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Surface(
            color = if (message.isFromUser) 
                MaterialTheme.colorScheme.primary 
            else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = if (message.isFromUser) 
                        MaterialTheme.colorScheme.onPrimary 
                    else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                // Show confidence if available
                message.metadata?.confidence?.let { confidence ->
                    if (confidence < 0.7 && !message.isFromUser) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Não tenho certeza sobre isso",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                
                // Show suggestions
                message.metadata?.suggestions?.takeIf { it.isNotEmpty() }?.let { suggestions ->
                    Spacer(modifier = Modifier.height(8.dp))
                    suggestions.take(2).forEach { suggestion ->
                        Text(
                            text = "• $suggestion",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (message.isFromUser) 
                                MaterialTheme.colorScheme.onPrimary 
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        if (message.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(top = 4.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun SuggestedActionCard(
    action: AssistantAction,
    onExecute: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = action.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Row {
                TextButton(onClick = onDismiss) {
                    Text("Ignorar")
                }
                Button(onClick = onExecute) {
                    Text("Executar")
                }
            }
        }
    }
}