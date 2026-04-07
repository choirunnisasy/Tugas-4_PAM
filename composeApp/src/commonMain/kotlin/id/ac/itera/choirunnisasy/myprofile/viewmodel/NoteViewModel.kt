package id.ac.itera.choirunnisasy.myprofile.viewmodel

// NoteViewModel.kt — viewmodel/
// ViewModel untuk Notes feature

import androidx.lifecycle.ViewModel
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.data.NoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    // ── Get note by ID ────────────────────────────────────────────
    fun getNoteById(id: Int): Note? {
        return _uiState.value.notes.find { it.id == id }
    }

    // ── Add new note ──────────────────────────────────────────────
    fun addNote(title: String, content: String, emoji: String = "📝") {
        val newNote = Note(
            id      = (_uiState.value.notes.maxOfOrNull { it.id } ?: 0) + 1,
            title   = title,
            content = content,
            emoji   = emoji
        )
        _uiState.update { it.copy(notes = it.notes + newNote) }
    }

    // ── Delete note ───────────────────────────────────────────────
    fun deleteNote(id: Int) {
        _uiState.update { it.copy(notes = it.notes.filter { note -> note.id != id }) }
    }
    // ── Update note ───────────────────────────────────────────────
    fun updateNote(id: Int, title: String, content: String, emoji: String) {
        _uiState.update {
            it.copy(
                notes = it.notes.map { note ->
                    if (note.id == id) note.copy(
                        title   = title,
                        content = content,
                        emoji   = emoji
                    )
                    else note
                }
            )
        }
    }

    // ── Toggle favorite ───────────────────────────────────────────
    fun toggleFavorite(id: Int) {
        _uiState.update {
            it.copy(
                notes = it.notes.map { note ->
                    if (note.id == id) note.copy(isFavorite = !note.isFavorite)
                    else note
                }
            )
        }
    }

    // ── Search ────────────────────────────────────────────────────
    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // ── Filtered notes (untuk search) ────────────────────────────
    fun getFilteredNotes(): List<Note> {
        val query = _uiState.value.searchQuery
        return if (query.isEmpty()) {
            _uiState.value.notes
        } else {
            _uiState.value.notes.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }
        }
    }
}