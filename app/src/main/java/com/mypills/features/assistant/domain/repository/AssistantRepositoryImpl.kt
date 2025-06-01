package com.mypills.features.assistant.domain.repository

import kotlinx.coroutines.flow.Flow
import com.mypills.features.assistant.domain.model.*

interface AssistantRepository {
    fun getActiveConversations(): Flow<List<Conversation>>
    suspend fun getConversationById(id: String): Conversation?
    suspend fun insertConversation(conversation: Conversation)
    suspend fun updateConversation(conversation: Conversation)
    suspend fun deleteConversation(conversationId: String)
    
    fun getMessagesForConversation(conversationId: String): Flow<List<Message>>
    suspend fun getLastMessage(conversationId: String): Message?
    suspend fun insertMessage(message: Message)
    suspend fun deleteMessage(message: Message)
}
