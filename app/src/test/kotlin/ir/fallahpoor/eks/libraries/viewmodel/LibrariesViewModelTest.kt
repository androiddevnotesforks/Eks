package ir.fallahpoor.eks.libraries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.FakeLibraryRepository
import ir.fallahpoor.eks.commontest.FakeStorage
import ir.fallahpoor.eks.commontest.MainDispatcherRule
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.ui.LibrariesScreenUiState
import ir.fallahpoor.eks.libraries.ui.LibrariesState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LibrariesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var librariesViewModel: LibrariesViewModel
    private lateinit var libraryRepository: FakeLibraryRepository
    private lateinit var storage: FakeStorage
    private val exceptionParser = ExceptionParser(ApplicationProvider.getApplicationContext())

    @Before
    fun runBeforeEachTest() {
        libraryRepository = FakeLibraryRepository()
        storage = FakeStorage()
        librariesViewModel = LibrariesViewModel(
            libraryRepository = libraryRepository, exceptionParser = exceptionParser
        )
    }

    @Test
    fun `state is updated correctly when getting the list of libraries succeeds`() = runTest {

        // Given
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1]).isEqualTo(
            LibrariesScreenUiState(
                librariesState = LibrariesState.Success(
                    libraryRepository.getLibraries()
                )
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when getting the list of libraries fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1])
            .isEqualTo(LibrariesScreenUiState(librariesState = LibrariesState.Error(exceptionParser.INTERNET_NOT_CONNECTED)))

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the sort order succeeds`() = runTest {

        // Given
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSortOrder(SortOrder.Z_TO_A))

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(3)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1])
            .isEqualTo(LibrariesScreenUiState(sortOrder = SortOrder.Z_TO_A))
        Truth.assertThat(uiStates[2]).isEqualTo(
            LibrariesScreenUiState(
                sortOrder = SortOrder.Z_TO_A,
                librariesState = LibrariesState.Success(libraryRepository.getLibraries(sortOrder = SortOrder.Z_TO_A))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the sort order fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSortOrder(SortOrder.Z_TO_A))

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(3)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1])
            .isEqualTo(LibrariesScreenUiState(sortOrder = SortOrder.Z_TO_A))
        Truth.assertThat(uiStates[2]).isEqualTo(
            LibrariesScreenUiState(
                sortOrder = SortOrder.Z_TO_A,
                librariesState = LibrariesState.Error(exceptionParser.INTERNET_NOT_CONNECTED)
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the search query succeeds`() = runTest {

        // Given
        val searchQuery = "ko"
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(3)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1]).isEqualTo(LibrariesScreenUiState(searchQuery = searchQuery))
        Truth.assertThat(uiStates[2]).isEqualTo(
            LibrariesScreenUiState(
                searchQuery = searchQuery, librariesState = LibrariesState.Success(
                    libraryRepository.getLibraries(searchQuery = searchQuery)
                )
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the search query fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val searchQuery = "ko"
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(3)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1]).isEqualTo(LibrariesScreenUiState(searchQuery = searchQuery))
        Truth.assertThat(uiStates[2]).isEqualTo(
            LibrariesScreenUiState(
                searchQuery = searchQuery,
                librariesState = LibrariesState.Error(exceptionParser.INTERNET_NOT_CONNECTED)
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when pinning a library succeeds`() = runTest {

        // Given
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.preference.name, ignoreCase = true)) {
                it.copy(pinned = 1)
            } else {
                it
            }
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(
                TestData.preference,
                pin = true
            )
        )

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1])
            .isEqualTo(LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries)))

        job.cancel()

    }

    @Test
    fun `state is updated correctly when pinning a library fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(
                TestData.preference, pin = true
            )
        )

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1]).isEqualTo(
            LibrariesScreenUiState(
                librariesState = LibrariesState.Error(exceptionParser.INTERNET_NOT_CONNECTED)
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when unpinning a library succeeds`() = runTest {

        // Given
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.core.name, ignoreCase = true)) {
                it.copy(pinned = 0)
            } else {
                it
            }
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(
                TestData.core,
                pin = false
            )
        )

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1])
            .isEqualTo(LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries)))

        job.cancel()

    }

    @Test
    fun `state is updated correctly when unpinning a library fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val uiStates = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(uiStates)
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(
                TestData.core, pin = false
            )
        )

        // Then
        Truth.assertThat(uiStates.size).isEqualTo(2)
        Truth.assertThat(uiStates[0]).isEqualTo(LibrariesScreenUiState())
        Truth.assertThat(uiStates[1]).isEqualTo(
            LibrariesScreenUiState(
                librariesState = LibrariesState.Error(exceptionParser.INTERNET_NOT_CONNECTED)
            )
        )

        job.cancel()

    }

}