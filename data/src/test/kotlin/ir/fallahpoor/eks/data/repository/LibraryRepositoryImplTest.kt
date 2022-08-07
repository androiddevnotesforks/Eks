package ir.fallahpoor.eks.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import ir.fallahpoor.eks.data.MainDispatcherRule
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.TestData
import ir.fallahpoor.eks.data.any
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.fakes.FakeLibraryDao
import ir.fallahpoor.eks.data.fakes.FakeStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LibraryRepositoryImplTest {

    companion object {
        private const val REFRESH_DATE = "September 11, 09:10"
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var librariesFetcher: LibrariesFetcher

    @Mock
    private lateinit var dateProvider: DateProvider

    private lateinit var libraryRepository: LibraryRepositoryImpl
    private lateinit var fakeLibraryDao: FakeLibraryDao
    private lateinit var fakeStorage: FakeStorage

    @Before
    fun runBeforeEachTest() {
        fakeStorage = FakeStorage()
        fakeLibraryDao = FakeLibraryDao()
        libraryRepository = LibraryRepositoryImpl(
            storage = fakeStorage,
            libraryDao = fakeLibraryDao,
            librariesFetcher = librariesFetcher,
            dateProvider = dateProvider
        )
        Mockito.`when`(dateProvider.getCurrentDate(any()))
            .thenReturn(REFRESH_DATE)
    }

    @Test
    fun `list of libraries is returned sorted by given sort order`() = runTest {

        // Given
        Mockito.`when`(librariesFetcher.fetchLibraries()).thenReturn(
            listOf(
                TestData.activity, TestData.biometric, TestData.core, TestData.room
            )
        )

        // When
        val actualLibraries = libraryRepository.getLibraries(sortOrder = SortOrder.Z_TO_A)

        // Then
        Truth.assertThat(actualLibraries).isEqualTo(
            listOf(
                TestData.room, TestData.core, TestData.biometric, TestData.activity
            )
        )

    }

    @Test
    fun `list of libraries is filtered by given search query`() = runTest {

        // Given
        val expectedLibraries =
            listOf(TestData.activity, TestData.biometric, TestData.core, TestData.room)
        fakeLibraryDao.insertLibrary(expectedLibraries)

        // When
        val actualLibraries = libraryRepository.getLibraries(searchQuery = "ro")

        // Then
        Truth.assertThat(actualLibraries).isEqualTo(listOf(TestData.room))
    }

    @Test
    fun `list of libraries is fetched when cache is empty`() = runTest {

        // Given
        val expectedLibraries =
            listOf(TestData.activity, TestData.biometric, TestData.core, TestData.room)
        Mockito.`when`(librariesFetcher.fetchLibraries()).thenReturn(expectedLibraries)

        // When
        val actualLibraries = libraryRepository.getLibraries()

        // Then
        Truth.assertThat(actualLibraries).isEqualTo(expectedLibraries)

    }

    @Test
    fun `list of libraries is returned from cache when cache is not empty`() = runTest {

        // Given
        val expectedLibraries =
            listOf(TestData.activity, TestData.biometric, TestData.core, TestData.room)
        fakeLibraryDao.insertLibrary(expectedLibraries)

        // When
        val actualLibraries = libraryRepository.getLibraries()

        // Then
        Truth.assertThat(actualLibraries).isEqualTo(expectedLibraries)
        Mockito.verifyNoInteractions(librariesFetcher)

    }

    @Test
    fun `library is pinned`() = runTest {

        // Given
        val library: Library = TestData.activity
        fakeLibraryDao.insertLibrary(listOf(library))

        // When
        val libraryToPin: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        libraryRepository.pinLibrary(libraryToPin, true)

        // Then
        val pinnedLibrary = libraryRepository.getLibraries().first { it.name == library.name }
        Truth.assertThat(pinnedLibrary.pinned).isEqualTo(1)

    }

    @Test
    fun `library is unpinned`() = runTest {

        // Given
        val library: Library = TestData.core
        fakeLibraryDao.insertLibrary(listOf(library))

        // When
        val libraryToUnpin: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        libraryRepository.pinLibrary(libraryToUnpin, false)

        // Then
        val unpinnedLibrary: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        Truth.assertThat(unpinnedLibrary.pinned).isEqualTo(0)

    }

    @Test
    fun `refreshLibraries() replaces old libraries with latest libraries`() = runTest {

        // TODO Check that the pin state of libraries are preserved

        // Given
        fakeLibraryDao.insertLibrary(
            listOf(
                TestData.activity, TestData.biometric, TestData.core, TestData.room
            )
        )
        val expectedLibraries = listOf(TestData.core, TestData.preference, TestData.room)
        Mockito.`when`(librariesFetcher.fetchLibraries()).thenReturn(expectedLibraries)

        // When
        libraryRepository.refreshLibraries()

        // Then
        Truth.assertThat(fakeLibraryDao.getAllLibraries()).isEqualTo(expectedLibraries)

    }

    @Test
    fun `refresh date is saved when libraries are refreshed`() = runTest {

        // Given
        Mockito.`when`(librariesFetcher.fetchLibraries()).thenReturn(emptyList())

        // When
        libraryRepository.refreshLibraries()

        // Then
        Truth.assertThat(fakeStorage.getRefreshDateAsFlow().first()).isEqualTo(REFRESH_DATE)

    }

    @Test
    fun `correct refresh date is returned`() = runTest {

        // Given
        fakeStorage.setRefreshDate(REFRESH_DATE)

        // When
        val actualRefreshDate: String = libraryRepository.getRefreshDate().first()

        // Then
        Truth.assertThat(actualRefreshDate).isEqualTo(REFRESH_DATE)

    }

    @Test
    fun `save sort order`() = runTest {

        // Given

        // When
        libraryRepository.saveSortOrder(SortOrder.PINNED_FIRST)

        // Then
        Truth.assertThat(fakeStorage.getSortOrder()).isEqualTo(SortOrder.PINNED_FIRST)

    }

    @Test
    fun `get sort order`() = runTest {

        // Given
        fakeStorage.setSortOrder(SortOrder.Z_TO_A)

        // When
        val actualSortOrder: SortOrder = libraryRepository.getSortOrder()

        // Then
        Truth.assertThat(actualSortOrder).isEqualTo(SortOrder.Z_TO_A)

    }

    // TODO write a test to verify that libraryRepository.getSortOrderAsFlow() returns the collect sequence
    //  of values

}