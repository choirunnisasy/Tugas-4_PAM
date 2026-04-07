package id.ac.itera.choirunnisasy.myprofile.data

// NoteUiState.kt — data/
// Data class untuk Notes feature

data class Note(
    val id      : Int,
    val title   : String,
    val content : String,
    val emoji   : String  = "📝",
    val isFavorite : Boolean = false
)

data class NoteUiState(
    val notes       : List<Note> = listOf(
        Note(1, "Belajar Compose", "Hari ini belajar Navigation Component di KMP", "🍵"),
        Note(2, "To-Do List", "Kerjain tugas PAM sebelum deadline!", "📋"),
        Note(3, "Ide Project", "Bikin app catatan dengan tema matcha & strawberry", "💡"),
    ),
    val searchQuery    : String  = "",
    val isLoading      : Boolean = false,
    val errorMessage   : String? = null
)