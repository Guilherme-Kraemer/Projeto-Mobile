package com.mypills.features.assistant.data.ai

import android.content.Context
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import dagger.hilt.android.qualifiers.ApplicationContext
import com.mypills.features.assistant.domain.model.*
import com.mypills.features.assistant.domain.service.AIService

@Singleton
class TensorFlowLiteAIService @Inject constructor(
    @ApplicationContext private val context: Context
) : AIService {

    private var intentClassifier: Interpreter? = null
    private var entityExtractor: Interpreter? = null
    private var responseGenerator: Interpreter? = null
    
    private val intentLabels = mutableListOf<String>()
    private val entityLabels = mutableListOf<String>()
    
    private val vocabulary = mutableMapOf<String, Int>()
    private val responses = mutableMapOf<Intent, List<String>>()

    init {
        initializeModels()
        loadResponses()
    }

    private fun initializeModels() {
        try {
            // Load intent classification model
            val intentModelBuffer = FileUtil.loadMappedFile(context, "intent_classifier.tflite")
            intentClassifier = Interpreter(intentModelBuffer)
            
            // Load entity extraction model  
            val entityModelBuffer = FileUtil.loadMappedFile(context, "entity_extractor.tflite")
            entityExtractor = Interpreter(entityModelBuffer)
            
            // Load labels
            intentLabels.addAll(FileUtil.loadLabels(context, "intent_labels.txt"))
            entityLabels.addAll(FileUtil.loadLabels(context, "entity_labels.txt"))
            
            // Load vocabulary
            loadVocabulary()
            
        } catch (e: Exception) {
            // Fallback to rule-based system if models fail to load
            println("Failed to load AI models, using rule-based fallback: ${e.message}")
        }
    }
    
    private fun loadVocabulary() {
        try {
            val vocabLines = context.assets.open("vocabulary.txt").bufferedReader().readLines()
            vocabLines.forEachIndexed { index, word ->
                vocabulary[word.lowercase()] = index
            }
        } catch (e: Exception) {
            // Create basic vocabulary for fallback
            createBasicVocabulary()
        }
    }
    
    private fun createBasicVocabulary() {
        val basicWords = listOf(
            "oi", "olá", "bom", "dia", "tarde", "noite", "ajuda", "medicamento", "remédio",
            "tomar", "hora", "lembrete", "preço", "custo", "quanto", "ônibus", "rota",
            "como", "chegar", "comprar", "lista", "farmácia", "dor", "sintoma",
            "obrigado", "tchau", "adeus"
        )
        
        basicWords.forEachIndexed { index, word ->
            vocabulary[word] = index
        }
    }
    
    private fun loadResponses() {
        responses[Intent.GREETING] = listOf(
            "Olá! Como posso ajudá-lo hoje?",
            "Oi! Estou aqui para ajudar com seus medicamentos e saúde.",
            "Bom dia! No que posso ser útil?"
        )
        
        responses[Intent.MEDICATION_QUESTION] = listOf(
            "Vou ajudá-lo com informações sobre medicamentos. Qual é sua dúvida?",
            "Posso fornecer informações gerais sobre medicamentos. O que gostaria de saber?",
            "Lembre-se: sempre consulte um médico para questões específicas sobre saúde."
        )
        
        responses[Intent.MEDICATION_REMINDER] = listOf(
            "Posso ajudá-lo a configurar lembretes para seus medicamentos.",
            "Vamos configurar um lembrete para não esquecer de tomar seu medicamento.",
            "É importante manter a regularidade nos medicamentos. Vou ajudar com os lembretes."
        )
        
        responses[Intent.PRICE_INQUIRY] = listOf(
            "Vou verificar informações de preços para você.",
            "Posso ajudar a comparar preços de medicamentos.",
            "Vamos encontrar o melhor preço para seu medicamento."
        )
        
        responses[Intent.ROUTE_HELP] = listOf(
            "Posso ajudar com informações de transporte público.",
            "Vamos encontrar a melhor rota para seu destino.",
            "Vou verificar os horários de ônibus para você."
        )
        
        responses[Intent.SHOPPING_ASSISTANCE] = listOf(
            "Posso ajudar com sua lista de compras.",
            "Vamos organizar suas compras de forma inteligente.",
            "Vou sugerir formas de otimizar seus gastos."
        )
        
        responses[Intent.FINANCE_HELP] = listOf(
            "Posso ajudar com questões financeiras.",
            "Vamos analisar seus gastos com medicamentos.",
            "Posso sugerir formas de economizar."
        )
        
        responses[Intent.GENERAL_HEALTH] = listOf(
            "Lembre-se: sempre consulte um profissional de saúde para questões médicas.",
            "Posso fornecer informações gerais, mas um médico é sempre a melhor opção.",
            "Para questões de saúde, recomendo conversar com um profissional."
        )
        
        responses[Intent.GOODBYE] = listOf(
            "Até logo! Estarei aqui quando precisar.",
            "Tchau! Cuidem-se bem!",
            "Até mais! Lembre-se de tomar seus medicamentos."
        )
        
        responses[Intent.UNKNOWN] = listOf(
            "Desculpe, não entendi bem. Pode reformular sua pergunta?",
            "Não tenho certeza sobre isso. Pode ser mais específico?",
            "Posso ajudar com medicamentos, lembretes, preços ou rotas. O que precisa?"
        )
    }

    override suspend fun processMessage(
        message: String, 
        context: ConversationContext
    ): AssistantResponse = withContext(Dispatchers.Default) {
        try {
            val intent = classifyIntent(message)
            val entities = extractEntities(message)
            val responseText = generateResponse(intent, entities, context)
            val suggestions = getSuggestions(context)
            val actions = generateActions(intent, entities)
            
            AssistantResponse(
                text = responseText,
                confidence = 0.85, // Placeholder confidence
                intent = intent,
                entities = entities,
                suggestions = suggestions,
                actions = actions
            )
        } catch (e: Exception) {
            AssistantResponse(
                text = "Desculpe, ocorreu um erro. Tente novamente.",
                confidence = 0.0,
                intent = Intent.UNKNOWN,
                entities = emptyList()
            )
        }
    }

    override suspend fun classifyIntent(text: String): Intent = withContext(Dispatchers.Default) {
        if (intentClassifier != null) {
            classifyIntentWithModel(text)
        } else {
            classifyIntentWithRules(text)
        }
    }
    
    private fun classifyIntentWithModel(text: String): Intent {
        try {
            val input = preprocessTextForModel(text)
            val output = TensorBuffer.createFixedSize(intArrayOf(1, intentLabels.size), org.tensorflow.lite.DataType.FLOAT32)
            
            intentClassifier?.run(input, output.buffer.rewind())
            
            val probabilities = output.floatArray
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
            
            return Intent.values().getOrElse(maxIndex) { Intent.UNKNOWN }
        } catch (e: Exception) {
            return classifyIntentWithRules(text)
        }
    }
    
    private fun classifyIntentWithRules(text: String): Intent {
        val lowerText = text.lowercase()
        
        return when {
            lowerText.matches(Regex(".*(oi|olá|bom dia|boa tarde|boa noite).*")) -> Intent.GREETING
            lowerText.matches(Regex(".*(tchau|adeus|até|obrigad).*")) -> Intent.GOODBYE
            lowerText.matches(Regex(".*(medicamento|remédio|comprimido|dose|tomar).*")) -> Intent.MEDICATION_QUESTION
            lowerText.matches(Regex(".*(lembrete|lembrar|hora|horário|alarme).*")) -> Intent.MEDICATION_REMINDER
            lowerText.matches(Regex(".*(preço|custo|quanto|valor|caro|barato).*")) -> Intent.PRICE_INQUIRY
            lowerText.matches(Regex(".*(ônibus|rota|transporte|como chegar|ir para).*")) -> Intent.ROUTE_HELP
            lowerText.matches(Regex(".*(comprar|lista|mercado|farmácia).*")) -> Intent.SHOPPING_ASSISTANCE
            lowerText.matches(Regex(".*(dinheiro|gasto|economia|financeiro).*")) -> Intent.FINANCE_HELP
            lowerText.matches(Regex(".*(dor|sintoma|doente|mal|saúde).*")) -> Intent.GENERAL_HEALTH
            else -> Intent.UNKNOWN
        }
    }

    override suspend fun extractEntities(text: String): List<Entity> = withContext(Dispatchers.Default) {
        if (entityExtractor != null) {
            extractEntitiesWithModel(text)
        } else {
            extractEntitiesWithRules(text)
        }
    }
    
    private fun extractEntitiesWithRules(text: String): List<Entity> {
        val entities = mutableListOf<Entity>()
        val lowerText = text.lowercase()
        
        // Extract medication names (simple patterns)
        val medicationPatterns = listOf(
            "paracetamol", "ibuprofeno", "dipirona", "aspirina", "omeprazol",
            "losartana", "metformina", "sinvastatina", "atenolol"
        )
        
        medicationPatterns.forEach { med ->
            val index = lowerText.indexOf(med)
            if (index != -1) {
                entities.add(
                    Entity(
                        type = EntityType.MEDICATION_NAME,
                        value = med,
                        confidence = 0.9,
                        startIndex = index,
                        endIndex = index + med.length
                    )
                )
            }
        }
        
        // Extract time patterns
        val timeRegex = Regex("""(\d{1,2}):(\d{2})""")
        timeRegex.findAll(text).forEach { match ->
            entities.add(
                Entity(
                    type = EntityType.TIME,
                    value = match.value,
                    confidence = 0.95,
                    startIndex = match.range.first,
                    endIndex = match.range.last + 1
                )
            )
        }
        
        // Extract quantities
        val quantityRegex = Regex("""(\d+)\s*(comprimido|ml|mg|g)""")
        quantityRegex.findAll(lowerText).forEach { match ->
            entities.add(
                Entity(
                    type = EntityType.QUANTITY,
                    value = match.value,
                    confidence = 0.9,
                    startIndex = match.range.first,
                    endIndex = match.range.last + 1
                )
            )
        }
        
        return entities
    }
    
    private fun extractEntitiesWithModel(text: String): List<Entity> {
        // Placeholder for model-based entity extraction
        return extractEntitiesWithRules(text)
    }

    override suspend fun generateResponse(
        intent: Intent,
        entities: List<Entity>,
        context: ConversationContext
    ): String = withContext(Dispatchers.Default) {
        val baseResponses = responses[intent] ?: responses[Intent.UNKNOWN]!!
        var response = baseResponses.random()
        
        // Customize response based on entities
        if (entities.isNotEmpty()) {
            response = enhanceResponseWithEntities(response, entities, intent)
        }
        
        // Add context awareness
        if (context.recentMessages.size > 2) {
            response = addContextualTouch(response, context)
        }
        
        response
    }
    
    private fun enhanceResponseWithEntities(response: String, entities: List<Entity>, intent: Intent): String {
        val medicationEntity = entities.find { it.type == EntityType.MEDICATION_NAME }
        val timeEntity = entities.find { it.type == EntityType.TIME }
        
        return when (intent) {
            Intent.MEDICATION_QUESTION -> {
                medicationEntity?.let {
                    "Sobre ${it.value}: $response"
                } ?: response
            }
            Intent.MEDICATION_REMINDER -> {
                val med = medicationEntity?.value ?: "seu medicamento"
                val time = timeEntity?.value ?: "no horário definido"
                "Vou configurar um lembrete para tomar $med às $time."
            }
            else -> response
        }
    }
    
    private fun addContextualTouch(response: String, context: ConversationContext): String {
        val recentTopics = context.recentMessages.takeLast(3)
            .filter { !it.isFromUser }
            .map { it.content }
        
        return if (recentTopics.any { it.contains("medicamento") }) {
            "$response Vejo que você tem interesse em medicamentos."
        } else {
            response
        }
    }

    override suspend fun getSuggestions(context: ConversationContext): List<String> = withContext(Dispatchers.Default) {
        val baseSuggestions = listOf(
            "Como configurar um lembrete?",
            "Verificar preços de medicamentos",
            "Encontrar rota de ônibus",
            "Ajuda com lista de compras",
            "Dicas de saúde"
        )
        
        // Customize based on recent conversation
        val recentUserMessages = context.recentMessages.filter { it.isFromUser }.takeLast(3)
        
        if (recentUserMessages.any { it.content.contains("medicamento", ignoreCase = true) }) {
            return@withContext listOf(
                "Configurar lembrete de medicamento",
                "Buscar farmácias próximas",
                "Verificar interações medicamentosas",
                "Calcular próxima dose"
            )
        }
        
        baseSuggestions.shuffled().take(3)
    }
    
    private fun generateActions(intent: Intent, entities: List<Entity>): List<AssistantAction> {
        val actions = mutableListOf<AssistantAction>()
        
        when (intent) {
            Intent.MEDICATION_REMINDER -> {
                actions.add(
                    AssistantAction(
                        type = ActionType.CREATE_REMINDER,
                        title = "Criar Lembrete",
                        description = "Configurar lembrete de medicamento",
                        parameters = entities.associate { "${it.type.name.lowercase()}" to it.value }
                    )
                )
            }
            Intent.PRICE_INQUIRY -> {
                actions.add(
                    AssistantAction(
                        type = ActionType.CHECK_PRICE,
                        title = "Verificar Preços",
                        description = "Comparar preços em farmácias",
                        parameters = emptyMap()
                    )
                )
            }
            Intent.ROUTE_HELP -> {
                actions.add(
                    AssistantAction(
                        type = ActionType.PLAN_ROUTE,
                        title = "Planejar Rota",
                        description = "Encontrar melhor trajeto",
                        parameters = emptyMap()
                    )
                )
            }
            Intent.SHOPPING_ASSISTANCE -> {
                actions.add(
                    AssistantAction(
                        type = ActionType.ADD_TO_SHOPPING_LIST,
                        title = "Adicionar à Lista",
                        description = "Incluir na lista de compras",
                        parameters = emptyMap()
                    )
                )
            }
            else -> {}
        }
        
        return actions
    }
    
    private fun preprocessTextForModel(text: String): TensorBuffer {
        val maxLength = 128
        val tokens = text.lowercase().split(" ")
            .mapNotNull { vocabulary[it] }
            .take(maxLength)
            .toMutableList()
        
        // Pad or truncate to maxLength
        while (tokens.size < maxLength) {
            tokens.add(0) // Padding token
        }
        
        val inputArray = tokens.map { it.toFloat() }.toFloatArray()
        val input = TensorBuffer.createFixedSize(intArrayOf(1, maxLength), org.tensorflow.lite.DataType.FLOAT32)
        input.loadArray(inputArray)
        
        return input
    }
}
