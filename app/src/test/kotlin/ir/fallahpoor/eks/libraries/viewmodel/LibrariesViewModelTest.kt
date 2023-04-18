package ir.fallahpoor.eks.libraries.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
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
    fun `Sequence of states is correct when getting the list of libraries succeeds`() = runTest {
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                librariesState = LibrariesState.Success(libraryRepository.getLibraries())
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when getting the list of libraries fails`() = runTest {
        libraryRepository.throwException = true
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE)
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when changing the sort order succeeds`() = runTest {
        val expectedSortOrder = SortOrder.Z_TO_A
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.ChangeSortOrder(
                    sortOrder = expectedSortOrder
                )
            )

            val actualIntermediateState = awaitItem()
            val expectedIntermediateState = LibrariesScreenUiState(sortOrder = expectedSortOrder)
            Truth.assertThat(actualIntermediateState).isEqualTo(expectedIntermediateState)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                sortOrder = expectedSortOrder,
                librariesState = LibrariesState.Success(libraryRepository.getLibraries(sortOrder = expectedSortOrder))
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when changing the sort order fails`() = runTest {
        val expectedSortOrder = SortOrder.Z_TO_A
        libraryRepository.throwException = true
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.ChangeSortOrder(
                    sortOrder = expectedSortOrder
                )
            )

            val actualIntermediateState = awaitItem()
            val expectedIntermediateState = LibrariesScreenUiState(sortOrder = expectedSortOrder)
            Truth.assertThat(actualIntermediateState).isEqualTo(expectedIntermediateState)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                sortOrder = expectedSortOrder, librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE)
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
            Truth.assertThat(storageRepository.getSortOrder()).isEqualTo(expectedSortOrder)
        }
        // TODO assert that sort order is not persisted
    }

    @Test
    fun `Sort order is persisted when changing the sort order succeeds`() {

        // Given
        val expectedSortOrder = SortOrder.Z_TO_A

        // When
        librariesViewModel.handleEvent(
            LibrariesViewModel.Event.ChangeSortOrder(
                sortOrder = expectedSortOrder
            )
        )

        // Then
        Truth.assertThat(storageRepository.getSortOrder()).isEqualTo(expectedSortOrder)

    }

    @Test
    fun `Sequence of states is correct when changing the search query succeeds`() = runTest {
        val searchQuery = "ko"
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

            val actualIntermediateState = awaitItem()
            val expectedIntermediateState = LibrariesScreenUiState(searchQuery = searchQuery)
            Truth.assertThat(actualIntermediateState).isEqualTo(expectedIntermediateState)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                searchQuery = searchQuery, librariesState = LibrariesState.Success(
                    libraryRepository.getLibraries(searchQuery = searchQuery)
                )
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when changing the search query fails`() = runTest {
        libraryRepository.throwException = true
        val searchQuery = "ko"
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(LibrariesViewModel.Event.ChangeSearchQuery(searchQuery))

            val actualIntermediateState = awaitItem()
            val expectedIntermediateState = LibrariesScreenUiState(searchQuery = searchQuery)
            Truth.assertThat(actualIntermediateState).isEqualTo(expectedIntermediateState)

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                searchQuery = searchQuery, librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE)
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when pinning a library succeeds`() = runTest {
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.preference.name, ignoreCase = true)) {
                it.copy(isPinned = true)
            } else {
                it
            }
        }
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.PinLibrary(library = TestData.preference, pin = true)
            )

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries))
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when pinning a library fails`() = runTest {
        libraryRepository.throwException = true
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.PinLibrary(library = TestData.preference, pin = true)
            )

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(
                librariesState = LibrariesState.Error(
                    FakeExceptionParser.ERROR_MESSAGE
                )
            )
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when unpinning a library succeeds`() = runTest {
        val libraries = libraryRepository.getLibraries().map {
            if (it.name.equals(TestData.core.name, ignoreCase = true)) {
                it.copy(isPinned = false)
            } else {
                it
            }
        }
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.PinLibrary(library = TestData.core, pin = false)
            )

            val actualFinalState = awaitItem()
            val expectedFinalState = LibrariesScreenUiState(librariesState = LibrariesState.Success(libraries))
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

    @Test
    fun `Sequence of states is correct when unpinning a library fails`() = runTest {
        libraryRepository.throwException = true
        librariesViewModel.librariesScreenUiState.test {
            val actualInitialState = awaitItem()
            val expectedInitialState = LibrariesScreenUiState()
            Truth.assertThat(actualInitialState).isEqualTo(expectedInitialState)

            // When
            librariesViewModel.handleEvent(
                LibrariesViewModel.Event.PinLibrary(library = TestData.core, pin = false)
            )

            val actualFinalState = awaitItem()
            val expectedFinalState =
                LibrariesScreenUiState(librariesState = LibrariesState.Error(FakeExceptionParser.ERROR_MESSAGE))
            Truth.assertThat(actualFinalState).isEqualTo(expectedFinalState)
        }
    }

}