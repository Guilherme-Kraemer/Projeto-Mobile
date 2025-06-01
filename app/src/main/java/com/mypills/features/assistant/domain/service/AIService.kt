package com.mypills.features.assistant.domain.service

interface AIService {
    suspend fun processMessage(message: String, context: ConversationContext): AssistantResponse
    suspend fun extractEntities(text: String): List<Entity>
    suspend fun classifyIntent(text: String): Intent
    suspend fun generateResponse(intent: Intent, entities: List<Entity>, context: ConversationContext): String
    suspend fun getSuggestions(context: ConversationContext): List<String>
}

data class ConversationContext(
    val conversationId: String,
    val recentMessages: List<Message>,
    val userMedications: List<String> = emptyList(),
    val userPreferences: Map<String, String> = emptyMap()
)
