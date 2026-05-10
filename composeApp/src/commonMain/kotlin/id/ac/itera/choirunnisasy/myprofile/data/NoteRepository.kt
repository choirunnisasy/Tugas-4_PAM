package id.ac.itera.choirunnisasy.myprofile.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import id.ac.itera.choirunnisasy.myprofile.db.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(title: String, content: String, emoji: String)
    suspend fun updateNote(id: Long, title: String, content: String, emoji: String, isFavorite: Boolean)
    suspend fun deleteNote(id: Long)
    suspend fun toggleFavorite(id: Long)
}

class NoteRepositoryImpl(database: NotesDatabase) : NoteRepository {
    private val queries = database.noteQueries

    override fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAllNotes()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { item ->
                    Note(
                        id = item.id.toInt(),
                        title = item.title,
                        content = item.content,
                        emoji = item.emoji,
                        isFavorite = item.is_favorite == 1L,
                        createdAt = item.created_at,
                        updatedAt = item.updated_at
                    )
                }
            }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries.selectNoteById(id).executeAsOneOrNull()?.let { item ->
            Note(
                id = item.id.toInt(),
                title = item.title,
                content = item.content,
                emoji = item.emoji,
                isFavorite = item.is_favorite == 1L,
                createdAt = item.created_at,
                updatedAt = item.updated_at
            )
        }
    }

    override suspend fun insertNote(title: String, content: String, emoji: String) {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title cannot be empty")
        }
        val now = Clock.System.now().toEpochMilliseconds()
        queries.insertNote(title, content, emoji, 0, now, now)
    }

    override suspend fun updateNote(id: Long, title: String, content: String, emoji: String, isFavorite: Boolean) {
        val now = Clock.System.now().toEpochMilliseconds()
        val favInt = if (isFavorite) 1L else 0L
        queries.updateNote(title, content, emoji, favInt, now, id)
    }

    override suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }

    override suspend fun toggleFavorite(id: Long) {
        queries.toggleFavorite(id)
    }
}
