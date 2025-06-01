package com.mypills.features.assistant.domain.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import com.mypills.features.assistant.domain.model.*
import com.mypills.features.assistant.domain.repository.AssistantRepository
import com.mypills.features.assistant.domain.service.AIService

class GetActiveConversationsUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    operator fun invoke(): Flow<List<Conversation>> = repository.getActiveConversations()
}

class CreateConversationUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(title: String = "Nova Conversa"): Conversation {
        val now = Clock.System.now()
        val conversation = Conversation(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            createdAt = now,
            lastMessageAt = now,
            isActive = true
        )
        
        repository.insertConversation(conversation)
        return conversation
    }
}

class SendMessageUseCase @Inject constructor(
    private val repository: AssistantRepository,
    private val aiService: AIService
) {
    suspend operator fun invoke(conversationId: String, messageContent: String): Pair<Message, Message> {
        val now = Clock.System.now()
        
        // Create user message
        val userMessage = Message(
            id = java.util.UUID.randomUUID().toString(),
            conversationId = conversationId,
            content = messageContent,
            isFromUser = true,
            timestamp = now,
            messageType = MessageType.TEXT
        )
        
        // Get conversation context
        val recentMessages = repository.getMessagesForConversation(conversationId)
        val messagesList = mutableListOf<Message>()
        recentMessages.collect { messagesList.addAll(it.takeLast(10)) } // Last 10 messages for context
        
        val context = ConversationContext(
            conversationId = conversationId,
            recentMessages = messagesList
        )
        
        // Process with AI
        val aiResponse = aiService.processMessage(messageContent, context)
        
        // Create assistant message
        val assistantMessage = Message(
            id = java.util.UUID.randomUUID().toString(),
            conversationId = conversationId,
            content = aiResponse.text,
            isFromUser = false,
            timestamp = now.plusMilliseconds(100), // Slight delay for ordering
            messageType = getMessageTypeFromIntent(aiResponse.intent),
            metadata = MessageMetadata(
                confidence = aiResponse.confidence,
                suggestions = aiResponse.suggestions,
                actionType = aiResponse.actions.firstOrNull()?.type?.name
            )
        )
        
        // Save messages
        repository.insertMessage(userMessage)
        repository.insertMessage(assistantMessage)
        
        // Update conversation
        repository.getConversationById(conversationId)?.let { conversation ->
            repository.updateConversation(
                conversation.copy(
                    lastMessageAt = now,
                    title = if (conversation.title == "Nova Conversa") {
                        generateConversationTitle(messageContent)
                    } else conversation.title
                )
            )
        }
        
        return Pair(userMessage, assistantMessage)
    }
    
    private fun getMessageTypeFromIntent(intent: Intent): MessageType {
        return when (intent) {
            Intent.MEDICATION_QUESTION, Intent.MEDICATION_REMINDER -> MessageType.MEDICATION_INFO
            Intent.PRICE_INQUIRY -> MessageType.PRICE_SUGGESTION
            Intent.ROUTE_HELP -> MessageType.ROUTE_SUGGESTION
            else -> MessageType.TEXT
        }
    }
    
    private fun generateConversationTitle(firstMessage: String): String {
        return when {
            firstMessage.contains("medicamento", ignoreCase = true) -> "Dúvidas sobre Medicamentos"
            firstMessage.contains("lembrete", ignoreCase = true) -> "Configurar Lembretes"
            firstMessage.contains("preço", ignoreCase = true) -> "Consulta de Preços"
            firstMessage.contains("ônibus", ignoreCase = true) || 
            firstMessage.contains("rota", ignoreCase = true) -> "Ajuda com Transporte"
            else -> firstMessage.take(30) + if (firstMessage.length > 30) "..." else ""
        }
    }
}

class GetMessagesUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    operator fun invoke(conversationId: String): Flow<List<Message>> = 
        repository.getMessagesForConversation(conversationId)
}
