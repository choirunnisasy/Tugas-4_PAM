package id.ac.itera.choirunnisasy.myprofile

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.data.NoteRepository
import id.ac.itera.choirunnisasy.myprofile.data.NoteUiState
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import id.ac.itera.choirunnisasy.myprofile.screen.AddNoteScreen
import id.ac.itera.choirunnisasy.myprofile.screen.NoteListScreen
import id.ac.itera.choirunnisasy.myprofile.viewmodel.NoteViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: NoteRepository = mockk(relaxed = true)
    private val settingsManager: SettingsManager = mockk(relaxed = true)

    @Test
    fun emptyState_isDisplayed_whenNotesAreEmpty() {
        // Arrange
        val emptyState = NoteUiState(notes = emptyList(), isLoading = false)
        val viewModel: NoteViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(emptyState)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        composeTestRule.setContent {
            NoteListScreen(
                isDark = false,
                onNoteClick = {},
                onAddClick = {},
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belum ada catatan").assertIsDisplayed()
    }

    @Test
    fun addNoteScreen_allowsInputAndClickSave() {
        // Arrange
        val viewModel: NoteViewModel = mockk(relaxed = true)
        
        composeTestRule.setContent {
            AddNoteScreen(
                isDark = false,
                onBack = {},
                viewModel = viewModel
            )
        }

        // Act
        composeTestRule.onNodeWithTag("title_field").performTextInput("Tugas Mingguan")
        composeTestRule.onNodeWithTag("content_field").performTextInput("Mengerjakan testing UI")
        composeTestRule.onNodeWithTag("save_button").performClick()

        // Assert
        // Verify via mockk if needed, or simply ensure it doesn't crash
    }

    @Test
    fun notesList_displaysAddedNote() {
        // Arrange
        val notes = listOf(
            Note(id = 1, title = "Testing Note", content = "This is a test", emoji = "📝")
        )
        val state = NoteUiState(notes = notes, isLoading = false)
        val viewModel: NoteViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(state)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        composeTestRule.setContent {
            NoteListScreen(
                isDark = false,
                onNoteClick = {},
                onAddClick = {},
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithTag("note_item_1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Testing Note").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test").assertIsDisplayed()
    }
}
