package id.ac.itera.choirunnisasy.myprofile.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

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

        try {
            val chatHistory = _messages.value.filter { !it.isStreaming && !it.isError }
            
            val contents = chatHistory.mapIndexed { index, msg ->
                val parts = mutableListOf<Part>()
                
                // Masukkan instruksi Matcha-chan ke pesan pertama agar aman dari error "Unknown name"
                val processedText = if (index == 0) {
                    "$systemPrompt\n\nUser: ${msg.text}"
                } else {
                    msg.text
                }
                
                if (processedText.isNotBlank()) {
                    parts.add(Part(text = processedText))
                }
                
                msg.imageBase64?.let { 
                    parts.add(Part(inlineData = InlineData("image/jpeg", it)))
                }

                Content(
                    role = if (msg.role == MessageRole.USER) "user" else "model",
                    parts = parts
                )
            }

            val request = GeminiRequest(
                contents = contents,
                generationConfig = GenerationConfig(temperature = 0.7)
            )

            var fullResponse = ""
            geminiService.streamGenerateContent(request).collect { response ->
                val chunk = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                fullResponse += chunk
                
                updateStreamingMessage(fullResponse)
                emit(fullResponse)
            }
            
            finalizeStreamingMessage(fullResponse)
        } catch (e: Exception) {
            handleError(e)
            throw e
        }
    }

    private fun updateStreamingMessage(text: String) {
        val currentList = _messages.value.toMutableList()
        if (currentList.isNotEmpty() && currentList.last().isStreaming) {
            currentList[currentList.size - 1] = currentList.last().copy(text = text)
            _messages.value = currentList
        }
    }

    private fun finalizeStreamingMessage(text: String) {
        val currentList = _messages.value.toMutableList()
        if (currentList.isNotEmpty() && currentList.last().isStreaming) {
            currentList[currentList.size - 1] = currentList.last().copy(text = text, isStreaming = false)
            _messages.value = currentList
        }
    }

    private fun handleError(e: Exception) {
        val currentList = _messages.value.toMutableList()
        if (currentList.isNotEmpty() && currentList.last().isStreaming) {
            currentList.removeAt(currentList.size - 1)
        }
        val errorMsg = e.message ?: "Koneksi terputus"
        currentList.add(ChatMessage(MessageRole.MODEL, "Gagal: $errorMsg 🍓", isError = true))
        _messages.value = currentList
    }

    private suspend fun <T> retryWithBackoff(
        retries: Int,
        initialDelay: Long = 1000,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(retries) { attempt ->
            try { return block() } catch (e: Exception) {
                if (attempt == retries - 1) throw e
                delay(currentDelay)
                currentDelay *= 2
            }
        }
        return block()
    }

    override fun clearChat() {
        _messages.value = emptyList()
    }
}
