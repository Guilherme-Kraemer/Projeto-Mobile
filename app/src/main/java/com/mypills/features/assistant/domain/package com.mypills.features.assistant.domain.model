package com.mypills.features.assistant.domain.model

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

data class Conversation(
    val id: String,
    val title: String,
    val createdAt: Instant,
    val lastMessageAt: Instant,
    val isActive: Boolean = true
)

data class Message(
    val id: String,
    val conversationId: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Instant,
    val messageType: MessageType = MessageType.TEXT,
    val metadata: MessageMetadata? = null
)

enum class MessageType {
    TEXT, MEDICATION_INFO, PRICE_SUGGESTION, ROUTE_SUGGESTION, REMINDER_CREATED, ERROR
}

@Serializable
data class MessageMetadata(
    val medicationId: String? = null,
    val confidence: Double? = null,
    val suggestions: List<String> = emptyList(),
    val actionType: String? = null,
    val parameters: Map<String, String> = emptyMap()
)

data class AssistantResponse(
    val text: String,
    val confidence: Double,
    val intent: Intent,
    val entities: List<Entity>,
    val suggestions: List<String> = emptyList(),
    val actions: List<AssistantAction> = emptyList()
)

enum class Intent {
    GREETING,
    MEDICATION_QUESTION,
    MEDICATION_REMINDER,
    PRICE_INQUIRY,
    ROUTE_HELP,
    SHOPPING_ASSISTANCE,
    FINANCE_HELP,
    GENERAL_HEALTH,
    GOODBYE,
    UNKNOWN
}

data class Entity(
    val type: EntityType,
    val value: String,
    val confidence: Double,
    val startIndex: Int,
    val endIndex: Int
)

enum class EntityType {
    MEDICATION_NAME,
    TIME,
    DATE,
    LOCATION,
    PRICE,
    QUANTITY,
    SYMPTOM,
    BODY_PART
}

data class AssistantAction(
    val type: ActionType,
    val title: String,
    val description: String,
    val parameters: Map<String, Any> = emptyMap()
)

enum class ActionType {
    CREATE_REMINDER,
    SEARCH_MEDICATION,
    CHECK_PRICE,
    PLAN_ROUTE,
    ADD_TO_SHOPPING_LIST,
    OPEN_MODULE
}
