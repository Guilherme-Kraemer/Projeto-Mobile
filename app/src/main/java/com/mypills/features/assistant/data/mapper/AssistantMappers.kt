package com.mypills.features.assistant.data.mapper

import com.mypills.core.database.entity.*
import com.mypills.features.assistant.domain.model.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun ConversationEntity.toDomain(): Conversation = Conversation(
    id = id,
    title = title,
    createdAt = createdAt,
    lastMessageAt = lastMessageAt,
    isActive = isActive
)

fun Conversation.toEntity(): ConversationEntity = ConversationEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    lastMessageAt = lastMessageAt,
    isActive = isActive
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    conversationId = conversationId,
    content = content,
    isFromUser = isFromUser,
    timestamp = timestamp,
    messageType = messageType,
    metadata = metadata?.let { 
        try {
            Json.decodeFromString<MessageMetadata>(it)
        } catch (e: Exception) {
            null
        }
    }
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    conversationId = conversationId,
    content = content,
    isFromUser = isFromUser,
    timestamp = timestamp,
    messageType = messageType,
    metadata = metadata?.let { Json.encodeToString(it) }
)