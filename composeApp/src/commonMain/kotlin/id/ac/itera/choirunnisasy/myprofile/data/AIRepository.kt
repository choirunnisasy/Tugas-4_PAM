package id.ac.itera.choirunnisasy.myprofile.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import io.ktor.client.plugins.*

sealed class AIError : Exception() {
    object NetworkError : AIError()
    object Unauthorized : AIError()
    object RateLimited : AIError()
    data class Unknown(val msg: String) : AIError()
}

interface AIRepository {
    val messages: StateFlow<List<ChatMessage>>
    suspend fun sendMessage(text: String, imageBase64: String? = null): Flow<String>
    fun clearChat()
}

data class ChatMessage(
    val role: MessageRole,
    val text: String,
    val imageBase64: String? = null,
    val isError: Boolean = false,
    val isStreaming: Boolean = false
)

enum class MessageRole {
    USER, MODEL, SYSTEM
}

class AIRepositoryImpl(private val geminiService: GeminiService) : AIRepository {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val systemPrompt = "Kamu adalah Matcha-chan, asisten virtual ceria. Gunakan bahasa Indonesia santai 🍓🍵."

    override suspend fun sendMessage(text: String, imageBase64: String?): Flow<String> = flow {
        val userMessage = ChatMessage(MessageRole.USER, text, imageBase64)
        _messages.value += userMessage

        val streamingMessage = ChatMessage(MessageRole.MODEL, "", isStreaming = true)
        _messages.value += streamingMessage

        retryWithBackoff(retries = 2) {
            val history = _messages.value.filter { !it.isStreaming }.map { msg ->
                Content(
                    role = if (msg.role == MessageRole.USER) "user" else "model",
                    parts = listOfNotNull(
                        msg.text.let { Part(text = it) },
                        msg.imageBase64?.let { Part(inlineData = InlineData("image/jpeg", it)) }
                    )
                )
            }

            val request = GeminiRequest(
                contents = history,
                systemInstruction = Content(parts = listOf(Part(text = systemPrompt))),
                generationConfig = GenerationConfig(temperature = 0.7)
            )

            var fullResponse = ""
            geminiService.streamGenerateContent(request).collect { response ->
                val chunk = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                fullResponse += chunk
                
                val currentList = _messages.value.toMutableList()
                if (currentList.isNotEmpty() && currentList.last().isStreaming) {
                    currentList[currentList.size - 1] = currentList.last().copy(text = fullResponse)
                    _messages.value = currentList
                }
                emit(fullResponse)
            }
            
            val finalList = _messages.value.toMutableList()
            if (finalList.isNotEmpty() && finalList.last().isStreaming) {
                finalList[finalList.size - 1] = finalList.last().copy(text = fullResponse, isStreaming = false)
                _messages.value = finalList
            }
        }
    }.onCompletion { cause ->
        if (cause != null && cause !is kotlinx.coroutines.CancellationException) {
            val currentList = _messages.value.toMutableList()
            if (currentList.isNotEmpty() && currentList.last().isStreaming) {
                currentList.removeAt(currentList.size - 1)
            }
            // Pesan error sangat singkat sesuai permintaan
            currentList.add(ChatMessage(MessageRole.MODEL, "Error! 🍓", isError = true))
            _messages.value = currentList
        }
    }

    private suspend fun <T> retryWithBackoff(
        retries: Int,
        initialDelay: Long = 1000,
        maxDelay: Long = 4000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(retries - 1) {
            try {
                return block()
            } catch (e: Exception) {
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
            }
        }
        return block()
    }

    override fun clearChat() {
        _messages.value = emptyList()
    }
}
