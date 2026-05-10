package id.ac.itera.choirunnisasy.myprofile.viewmodel

import app.cash.turbine.test
import id.ac.itera.choirunnisasy.myprofile.data.Note
import id.ac.itera.choirunnisasy.myprofile.data.NoteRepository
import id.ac.itera.choirunnisasy.myprofile.data.SettingsManager
import id.ac.itera.choirunnisasy.myprofile.data.SortOrder
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    private val repository: NoteRepository = mockk()
    private val settingsManager: SettingsManager = mockk()
    private lateinit var viewModel: NoteViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { settingsManager.sortOrder } returns flowOf(SortOrder.NEWEST)
        every { repository.getAllNotes() } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addNote_callsRepositoryInsert() = runTest {
        coEvery { repository.insertNote(any(), any(), any()) } just runs

        viewModel = NoteViewModel(repository, settingsManager)
        viewModel.addNote("Title", "Content", "📝")

        advanceUntilIdle()
        coVerify { repository.insertNote("Title", "Content", "📝") }
    }
}