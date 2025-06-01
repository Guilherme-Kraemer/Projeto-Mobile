package com.mypills.features.assistant.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.mypills.core.database.dao.AssistantDao
import com.mypills.features.assistant.domain.model.*
import com.mypills.features.assistant.domain.repository.AssistantRepository
import com.mypills.features.assistant.data.mapper.*

class AssistantRepositoryImpl @Inject constructor(
    private val assistantDao: AssistantDao
) : AssistantRepository {

    override fun getActiveConversations(): Flow<List<Conversation>> =
        assistantDao.getActiveConversations().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getConversationById(id: String): Conversation? =
        assistantDao.getConversationById(id)?.toDomain()

    override suspend fun insertConversation(conversation: Conversation) {
        assistantDao.insertConversation(conversation.toEntity())
    }

    override suspend fun updateConversation(conversation: Conversation) {
        assistantDao.updateConversation(conversation.toEntity())
    }

    override suspend fun deleteConversation(conversationId: String) {
        assistantDao.deleteConversation(conversationId)
    }

    override fun getMessagesForConversation(conversationId: String): Flow<List<Message>> =
        assistantDao.getMessagesForConversation(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getLastMessage(conversationId: String): Message? =
        assistantDao.getLastMessage(conversationId)?.toDomain()

    override suspend fun insertMessage(message: Message) {
        assistantDao.insertMessage(message.toEntity())
    }

    override suspend fun deleteMessage(message: Message) {
        assistantDao.deleteMessage(message.toEntity())
    }
}
