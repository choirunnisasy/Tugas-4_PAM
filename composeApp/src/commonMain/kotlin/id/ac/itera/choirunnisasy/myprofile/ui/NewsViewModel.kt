package id.ac.itera.choirunnisasy.myprofile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.choirunnisasy.myprofile.data.Article
import id.ac.itera.choirunnisasy.myprofile.data.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    data object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

sealed class PostUiState {
    data object Idle : PostUiState()
    data object Loading : PostUiState()
    data object Success : PostUiState()
    data class Error(val message: String) : PostUiState()
}

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _detailArticle = MutableStateFlow<Article?>(null)
    val detailArticle: StateFlow<Article?> = _detailArticle.asStateFlow()

    private val _postState = MutableStateFlow<PostUiState>(PostUiState.Idle)
    val postState: StateFlow<PostUiState> = _postState.asStateFlow()

    init { loadArticles() }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getArticles()
                .onSuccess { _uiState.value = NewsUiState.Success(it) }
                .onFailure { _uiState.value = NewsUiState.Error(it.message ?: "Error") }
        }
    }

    fun loadDetail(id: Int) {
        viewModelScope.launch {
            repository.getArticleById(id).onSuccess { _detailArticle.value = it }
        }
    }

    fun clearDetail() { _detailArticle.value = null }

    fun createPost(title: String, body: String) {
        if (title.isBlank() || body.isBlank()) return

        viewModelScope.launch {
            _postState.value = PostUiState.Loading
            repository.createPost(title, body)
                .onSuccess {
                    _postState.value = PostUiState.Success
                    // Kembalikan ke Idle setelah beberapa detik agar dialog bisa ditutup otomatis/direset
                    delay(2000)
                    _postState.value = PostUiState.Idle
                }
                .onFailure {
                    _postState.value = PostUiState.Error(it.message ?: "Gagal mengirim berita")
                }
        }
    }

    fun resetPostState() { _postState.value = PostUiState.Idle }
}