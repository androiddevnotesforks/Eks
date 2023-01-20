package ir.fallahpoor.eks.libraries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.FakeLibraryRepository
import ir.fallahpoor.eks.commontest.FakeStorageRepository
import ir.fallahpoor.eks.commontest.MainDispatcherRule
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.ui.LibrariesScreenUiState
import ir.fallahpoor.eks.libraries.ui.LibrariesState
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.FakeExceptionParser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibrariesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var librariesViewModel: LibrariesViewModel
    private lateinit var libraryRepository: FakeLibraryRepository
    private lateinit var storageRepository: FakeStorageRepository

    @Before
    fun runBeforeEachTest() {
        libraryRepository = FakeLibraryRepository()
        storageRepository = FakeStorageRepository()
        librariesViewModel = LibrariesViewModel(
            libraryRepository = libraryRepository,
            storageRepository = storageRepository,
            exceptionParser = FakeExceptionParser()
        )
    }

    @Test
    fun `state is updated correctly when getting the list of libraries succeeds`() = runTest {

        // Given
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Success(libraryRepository.getLibraries()))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when getting the list of libraries fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the sort order succeeds`() = runTest {

        // Given
        val expectedSortOrder = SortOrder.Z_TO_A
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSortOrder(expectedSortOrder))

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(sortOrder = expectedSortOrder),
                LibrariesScreenUiState(
                    sortOrder = expectedSortOrder,
                    librariesState = LibrariesState.Success(libraryRepository.getLibraries(sortOrder = expectedSortOrder))
                )
            )
        )
        Truth.assertThat(storageRepository.getSortOrder()).isEqualTo(expectedSortOrder)

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the sort order fails`() = runTest {

        // Given
        val expectedSortOrder = SortOrder.Z_TO_A
        libraryRepository.throwException = true
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSortOrder(expectedSortOrder))

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(sortOrder = expectedSortOrder),
                LibrariesScreenUiState(
                    sortOrder = expectedSortOrder,
                    librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE)
                )
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when changing the search query succeeds`() = runTest {

        // Given
        val searchQuery = "ko"
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(searchQuery = searchQuery),
                LibrariesScreenUiState(
                    searchQuery = searchQuery, librariesState = LibrariesState.Success(
                        libraryRepository.getLibraries(searchQuery = searchQuery)
                    )
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
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(searchQuery = searchQuery),
                LibrariesScreenUiState(
                    searchQuery = searchQuery,
                    librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE)
                )
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when pinning a library succeeds`() = runTest {

        // Given
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.preference.name, ignoreCase = true)) {
                it.copy(isPinned = true)
            } else {
                it
            }
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(library = TestData.preference, pin = true)
        )

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when pinning a library fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(library = TestData.preference, pin = true)
        )

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when unpinning a library succeeds`() = runTest {

        // Given
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.core.name, ignoreCase = true)) {
                it.copy(isPinned = false)
            } else {
                it
            }
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(library = TestData.core, pin = false)
        )

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries))
            )
        )

        job.cancel()

    }

    @Test
    fun `state is updated correctly when unpinning a library fails`() = runTest {

        // Given
        libraryRepository.throwException = true
        val actualStateSequence = mutableListOf<LibrariesScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            librariesViewModel.librariesScreenUiState.toList(actualStateSequence)
        }

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.PinLibrary(library = TestData.core, pin = false)
        )

        // Then
        assertStateSequence(
            actualStateSequence = actualStateSequence,
            expectedStateSequence = listOf(
                LibrariesScreenUiState(),
                LibrariesScreenUiState(librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE))
            )
        )

        job.cancel()

    }

    private fun assertStateSequence(
        actualStateSequence: List<LibrariesScreenUiState>,
        expectedStateSequence: List<LibrariesScreenUiState>
    ) {
        Truth.assertThat(actualStateSequence.size).isEqualTo(expectedStateSequence.size)
        actualStateSequence.zip(expectedStateSequence) { actualState, expectedState ->
            Truth.assertThat(actualState).isEqualTo(expectedState)
        }
    }

}