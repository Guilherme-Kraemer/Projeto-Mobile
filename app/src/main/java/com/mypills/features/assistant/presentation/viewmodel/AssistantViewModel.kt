package com.mypills.features.assistant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.assistant.domain.model.*
import com.mypills.features.assistant.domain.usecase.*

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val getActiveConversationsUseCase: GetActiveConversationsUseCase,
    private val createConversationUseCase: CreateConversationUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssistantUiState())
    val uiState: StateFlow<AssistantUiState> = _uiState.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<Message>>(emptyList())
    val currentMessages: StateFlow<List<Message>> = _currentMessages.asStateFlow()

    init {
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            getActiveConversationsUseCase().collect { conversations ->
                _uiState.value = _uiState.value.copy(
                    conversations = conversations,
                    isLoading = false
                )
                
                // Auto-select first conversation or create new one
                if (conversations.isNotEmpty() && _uiState.value.currentConversationId == null) {
                    selectConversation(conversations.first().id)
                } else if (conversations.isEmpty()) {
                    createNewConversation()
                }
            }
        }
    }

    fun createNewConversation() {
        viewModelScope.launch {
            try {
                val conversation = createConversationUseCase()
                selectConversation(conversation.id)
                _uiState.value = _uiState.value.copy(
                    message = "Nova conversa iniciada!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao criar conversa: ${e.message}"
                )
            }
        }
    }

    fun selectConversation(conversationId: String) {
        _uiState.value = _uiState.value.copy(currentConversationId = conversationId)
        
        viewModelScope.launch {
            getMessagesUseCase(conversationId).collect { messages ->
                _currentMessages.value = messages
            }
        }
    }

    fun sendMessage(messageContent: String) {
        val currentConversationId = _uiState.value.currentConversationId
        if (currentConversationId == null || messageContent.isBlank()) return

        _uiState.value = _uiState.value.copy(isTyping = true)

        viewModelScope.launch {
            try {
                val (userMessage, assistantMessage) = sendMessageUseCase(currentConversationId, messageContent)
                
                // Handle actions from assistant response
                assistantMessage.metadata?.actionType?.let { actionType ->
                    handleAssistantAction(actionType, assistantMessage.metadata)
                }
                
                _uiState.value = _uiState.value.copy(
                    isTyping = false,
                    lastAssistantResponse = assistantMessage
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao enviar mensagem: ${e.message}",
                    isTyping = false
                )
            }
        }
    }

    private fun handleAssistantAction(actionType: String, metadata: MessageMetadata?) {
        // Handle different types of actions
        when (actionType) {
            "CREATE_REMINDER" -> {
                _uiState.value = _uiState.value.copy(
                    suggestedAction = AssistantAction(
                        type = ActionType.CREATE_REMINDER,
                        title = "Criar Lembrete",
                        description = "Configurar novo lembrete de medicamento",
                        parameters = metadata?.parameters ?: emptyMap()
                    )
                )
            }
            "CHECK_PRICE" -> {
                _uiState.value = _uiState.value.copy(
                    suggestedAction = AssistantAction(
                        type = ActionType.CHECK_PRICE,
                        title = "Verificar Preços",
                        description = "Comparar preços de medicamentos",
                        parameters = metadata?.parameters ?: emptyMap()
                    )
                )
            }
            // Add other action types as needed
        }
    }

    fun executeSuggestedAction(action: AssistantAction) {
        when (action.type) {
            ActionType.CREATE_REMINDER -> {
                // Navigate to reminder creation
                _uiState.value = _uiState.value.copy(
                    navigationEvent = "create_reminder"
                )
            }
            ActionType.CHECK_PRICE -> {
                // Navigate to price comparison
                _uiState.value = _uiState.value.copy(
                    navigationEvent = "check_prices"
                )
            }
            ActionType.PLAN_ROUTE -> {
                // Navigate to route planner
                _uiState.value = _uiState.value.copy(
                    navigationEvent = "plan_route"
                )
            }
            ActionType.OPEN_MODULE -> {
                val module = action.parameters["module"] as? String ?: "dashboard"
                _uiState.value = _uiState.value.copy(
                    navigationEvent = module
                )
            }
            else -> {}
        }
        
        // Clear suggested action after execution
        _uiState.value = _uiState.value.copy(suggestedAction = null)
    }

    fun dismissSuggestedAction() {
        _uiState.value = _uiState.value.copy(suggestedAction = null)
    }

    fun clearNavigationEvent() {
        _uiState.value = _uiState.value.copy(navigationEvent = null)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class AssistantUiState(
    val conversations: List<Conversation> = emptyList(),
    val currentConversationId: String? = null,
    val lastAssistantResponse: Message? = null,
    val suggestedAction: AssistantAction? = null,
    val isLoading: Boolean = true,
    val isTyping: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val navigationEvent: String? = null
)
