package id.ac.itera.choirunnisasy.myprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.data.NoteRepository
import id.ac.itera.choirunnisasy.myprofile.data.NoteUiState
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val uiState: StateFlow<NoteUiState> = combine(
        repository.getAllNotes(),
        _searchQuery,
        settingsManager.sortOrder
    ) { notes, query, sortOrder ->
        val filtered = notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }

        val sorted = when (sortOrder) {
            SortOrder.NEWEST -> filtered.sortedByDescending { it.createdAt }
            SortOrder.OLDEST -> filtered.sortedBy { it.createdAt }
            SortOrder.AZ -> filtered.sortedBy { it.title.lowercase() }
        }

        NoteUiState(
            notes = sorted,
            searchQuery = query,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteUiState(isLoading = true))

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, content: String, emoji: String = "📝") {
        viewModelScope.launch {
            repository.insertNote(title, content, emoji)
        }
    }

    fun updateNote(id: Int, title: String, content: String, emoji: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateNote(id.toLong(), title, content, emoji, isFavorite)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id.toLong())
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(id.toLong())
        }
    }
    
    fun getNoteById(id: Int): Flow<Note?> = flow {
        emit(repository.getNoteById(id.toLong()))
    }
}
