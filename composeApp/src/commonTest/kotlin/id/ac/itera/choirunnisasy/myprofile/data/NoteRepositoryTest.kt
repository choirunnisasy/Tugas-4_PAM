package id.ac.itera.choirunnisasy.myprofile.data

import id.ac.itera.choirunnisasy.myprofile.db.NotesDatabase
import id.ac.itera.choirunnisasy.myprofile.db.NoteQueries
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class NoteRepositoryTest {

    private lateinit var repository: NoteRepository
    private val database: NotesDatabase = mockk()
    private val queries: NoteQueries = mockk()

    @BeforeTest
    fun setup() {
        every { database.noteQueries } returns queries
        repository = NoteRepositoryImpl(database)
    }

    // 1. Test Insert Note Success
    @Test
    fun insertNote_success() = runTest {
        every { queries.insertNote(any(), any(), any(), any(), any(), any()) } just runs
        
        repository.insertNote("Title", "Content", "📝")
        
        verify { queries.insertNote("Title", "Content", "📝", 0L, any(), any()) }
    }

    // 2. Test Insert Note Validation (Empty Title)
    @Test
    fun insertNote_emptyTitle_throwsException() = runTest {
        assertFailsWith<IllegalArgumentException> {
            repository.insertNote("", "Content", "📝")
        }
    }

    // 3. Test Get Note By ID - Found
    @Test
    fun getNoteById_found() = runTest {
        val id = 1L
        val mockNote = mockk<id.ac.itera.choirunnisasy.myprofile.db.Note> {
            every { this@mockk.id } returns 1L
            every { title } returns "Title"
            every { content } returns "Content"
            every { emoji } returns "📝"
            every { is_favorite } returns 0L
            every { created_at } returns 100L
            every { updated_at } returns 100L
        }
        val mockQuery = mockk<app.cash.sqldelight.Query<id.ac.itera.choirunnisasy.myprofile.db.Note>>()
        every { queries.selectNoteById(id) } returns mockQuery
        every { mockQuery.executeAsOneOrNull() } returns mockNote

        val result = repository.getNoteById(id)
        assertNotNull(result)
        assertEquals("Title", result.title)
    }

    // 4. Test Get Note By ID - Not Found
    @Test
    fun getNoteById_notFound() = runTest {
        val id = 99L
        val mockQuery = mockk<app.cash.sqldelight.Query<id.ac.itera.choirunnisasy.myprofile.db.Note>>()
        every { queries.selectNoteById(id) } returns mockQuery
        every { mockQuery.executeAsOneOrNull() } returns null

        val result = repository.getNoteById(id)
        assertNull(result)
    }

    // 5. Test Update Note
    @Test
    fun updateNote_callsQueries() = runTest {
        every { queries.updateNote(any(), any(), any(), any(), any(), any()) } just runs
        
        repository.updateNote(1L, "New Title", "New Content", "✅", true)
        
        verify { queries.updateNote("New Title", "New Content", "✅", 1L, any(), 1L) }
    }

    // 6. Test Delete Note
    @Test
    fun deleteNote_callsQueries() = runTest {
        every { queries.deleteNote(any()) } just runs
        
        repository.deleteNote(1L)
        
        verify { queries.deleteNote(1L) }
    }

    // 7. Test Toggle Favorite
    @Test
    fun toggleFavorite_callsQueries() = runTest {
        every { queries.toggleFavorite(any()) } just runs
        
        repository.toggleFavorite(1L)
        
        verify { queries.toggleFavorite(1L) }
    }

    // 8. Test Insert Note with Blank Title
    @Test
    fun insertNote_blankTitle_throwsException() = runTest {
        assertFailsWith<IllegalArgumentException> {
            repository.insertNote("   ", "Content", "📝")
        }
    }

    // 9. Test Update Note with Favorite False
    @Test
    fun updateNote_favoriteFalse_callsQueriesWithZero() = runTest {
        every { queries.updateNote(any(), any(), any(), any(), any(), any()) } just runs
        
        repository.updateNote(1L, "Title", "Content", "📝", false)
        
        verify { queries.updateNote(any(), any(), any(), 0L, any(), 1L) }
    }

    // 10. Test Database Returns Correct Mapped Note
    @Test
    fun getNoteById_mapsFieldsCorrectly() = runTest {
        val mockNote = mockk<id.ac.itera.choirunnisasy.myprofile.db.Note> {
            every { id } returns 5L
            every { title } returns "Mapped"
            every { content } returns "Body"
            every { emoji } returns "🚀"
            every { is_favorite } returns 1L
            every { created_at } returns 1000L
            every { updated_at } returns 2000L
        }
        val mockQuery = mockk<app.cash.sqldelight.Query<id.ac.itera.choirunnisasy.myprofile.db.Note>>()
        every { queries.selectNoteById(5L) } returns mockQuery
        every { mockQuery.executeAsOneOrNull() } returns mockNote

        val result = repository.getNoteById(5L)
        assertEquals(5, result?.id)
        assertEquals(true, result?.isFavorite)
        assertEquals(1000L, result?.createdAt)
    }

    // 11. Test Insert Note verifies Timestamp is generated
    @Test
    fun insertNote_verifiesTimestamp() = runTest {
        every { queries.insertNote(any(), any(), any(), any(), any(), any()) } just runs
        
        repository.insertNote("T", "C", "E")
        
        verify { queries.insertNote(any(), any(), any(), any(), match { it > 0 }, match { it > 0 }) }
    }

    // 12. Test Update Note verifies Target ID
    @Test
    fun updateNote_verifiesTargetId() = runTest {
        every { queries.updateNote(any(), any(), any(), any(), any(), any()) } just runs
        val targetId = 123L
        
        repository.updateNote(targetId, "T", "C", "E", false)

        verify { queries.updateNote(any(), any(), any(), any(), any(), eq(targetId)) }
    }
}
