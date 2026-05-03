package id.ac.itera.choirunnisasy.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.choirunnisasy.myprofile.data.AIRepository
import id.ac.itera.choirunnisasy.myprofile.data.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val inputText: String = "",
    val selectedImageBase64: String? = null
)

class ChatViewModel(private val repository: AIRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val messages = repository.messages.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun onImageSelected(base64: String?) {
        _uiState.value = _uiState.value.copy(selectedImageBase64 = base64)
    }

    fun sendMessage() {
        val text = _uiState.value.inputText
        val image = _uiState.value.selectedImageBase64
        if (text.isBlank() && image == null) return

        _uiState.value = _uiState.value.copy(inputText = "", selectedImageBase64 = null, isLoading = true)

        viewModelScope.launch {
            try {
                repository.sendMessage(text, image).collect {
                    // Streaming handled by repository updating its state
                }
            } catch (e: Exception) {
                // Error handled in repository by adding an error message
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearChat() {
        repository.clearChat()
    }
}
