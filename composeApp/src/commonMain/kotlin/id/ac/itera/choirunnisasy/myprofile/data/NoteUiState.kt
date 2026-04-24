package id.ac.itera.choirunnisasy.myprofile.data

data class Note(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
    val emoji: String = "📝"
)

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
