package ir.fallahpoor.eks.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import ir.fallahpoor.eks.commontest.FakeStorageRepository
import ir.fallahpoor.eks.commontest.MainDispatcherRule
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.TestData
import ir.fallahpoor.eks.data.fakes.FakeLibraryDao
import ir.fallahpoor.eks.data.network.LibrariesFetcher
import ir.fallahpoor.eks.data.network.dto.LibraryDto
import ir.fallahpoor.eks.data.network.dto.toLibraryEntity
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.toLibraryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LibraryRepositoryImplTest {

    companion object {
        private const val REFRESH_DATE = "September 11, 09:10"
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val librariesFetcher: LibrariesFetcher = mockk()

    private val dateProvider: DateProvider = mockk()

    private lateinit var libraryRepository: LibraryRepositoryImpl
    private lateinit var fakeLibraryDao: FakeLibraryDao
    private lateinit var fakeStorageRepository: FakeStorageRepository

    @Before
    fun runBeforeEachTest() {
        fakeStorageRepository = FakeStorageRepository()
        fakeLibraryDao = FakeLibraryDao()
        libraryRepository = LibraryRepositoryImpl(
            storageRepository = fakeStorageRepository,
            libraryDao = fakeLibraryDao,
            librariesFetcher = librariesFetcher,
            dateProvider = dateProvider
        )
        every { dateProvider.getCurrentDate() } returns REFRESH_DATE
    }

    @Test
    fun `list of libraries is fetched from the Internet when cache is empty`() = runTest {
        // Given
        val expectedLibraries = getOldLibraries()
        coEvery { librariesFetcher.fetchLibraries() } returns expectedLibraries

        // When
        val actualLibraries = libraryRepository.getLibraries()

        // Then
        Truth.assertThat(actualLibraries.map(Library::toLibraryEntity))
            .isEqualTo(expectedLibraries.map(LibraryDto::toLibraryEntity))
        coVerify { librariesFetcher.fetchLibraries() }
    }

    private fun getOldLibraries() = listOf(
        TestData.activity, TestData.biometric, TestData.core, TestData.room
    )

    @Test
    fun `list of libraries is returned from cache when cache is not empty`() = runTest {
        // Given
        val expectedLibraries = getOldLibraries().map(LibraryDto::toLibraryEntity)
        fakeLibraryDao.insertLibraries(expectedLibraries)

        // When
        val actualLibraries = libraryRepository.getLibraries()

        // Then
        Truth.assertThat(actualLibraries.map(Library::toLibraryEntity)).isEqualTo(expectedLibraries)
        coVerify(exactly = 0) { librariesFetcher.fetchLibraries() }
    }

    @Test
    fun `list of libraries is returned sorted by given sort order`() = runTest {
        // Given
        coEvery { librariesFetcher.fetchLibraries() } returns getOldLibraries()

        // When
        val actualLibraries = libraryRepository.getLibraries(sortOrder = SortOrder.Z_TO_A)

        // Then
        val expectedLibraries = listOf(
            TestData.room, TestData.core, TestData.biometric, TestData.activity
        )
        Truth.assertThat(actualLibraries.map(Library::toLibraryEntity))
            .isEqualTo(expectedLibraries.map(LibraryDto::toLibraryEntity))
        coVerify { librariesFetcher.fetchLibraries() }
    }

    @Test
    fun `list of libraries is filtered by given search query`() = runTest {
        // Given
        fakeLibraryDao.insertLibraries(getOldLibraries().map(LibraryDto::toLibraryEntity))

        // When
        val actualLibraries = libraryRepository.getLibraries(searchQuery = "ro")

        // Then
        Truth.assertThat(actualLibraries.map(Library::toLibraryEntity))
            .isEqualTo(listOf(TestData.room).map(LibraryDto::toLibraryEntity))
    }

    @Test
    fun `library is pinned`() = runTest {
        // Given
        val library = TestData.activity
        fakeLibraryDao.insertLibraries(listOf(library).map(LibraryDto::toLibraryEntity))

        // When
        val libraryToPin: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        libraryRepository.pinLibrary(libraryToPin, true)

        // Then
        val pinnedLibrary = libraryRepository.getLibraries().first { it.name == library.name }
        Truth.assertThat(pinnedLibrary.isPinned).isTrue()
    }

    @Test
    fun `library is unpinned`() = runTest {
        // Given
        val library = TestData.core
        fakeLibraryDao.insertLibraries(listOf(library).map { it.toLibraryEntity(true) })

        // When
        val libraryToUnpin: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        libraryRepository.pinLibrary(libraryToUnpin, false)

        // Then
        val unpinnedLibrary: Library =
            libraryRepository.getLibraries().first { it.name == library.name }
        Truth.assertThat(unpinnedLibrary.isPinned).isFalse()
    }

    @Test
    fun `refreshing libraries replaces old libraries with the latest ones`() = runTest {
        // Given
        fakeLibraryDao.insertLibraries(getOldLibraries().map(LibraryDto::toLibraryEntity))
        val expectedLibraries = listOf(
            TestData.core, TestData.preference, TestData.room
        )
        coEvery { librariesFetcher.fetchLibraries() } returns expectedLibraries

        // When
        libraryRepository.refreshLibraries()

        // Then
        Truth.assertThat(fakeLibraryDao.getLibraries())
            .isEqualTo(expectedLibraries.map(LibraryDto::toLibraryEntity))
    }

    @Test
    fun `refreshing libraries preserves the pin state of libraries`() = runTest {
        // Given
        val libraryToPin = TestData.core
        fakeLibraryDao.insertLibraries(getOldLibraries().map {
            it.toLibraryEntity(it.name.equals(libraryToPin.name, ignoreCase = true))
        })
        val refreshedLibraries = listOf(
            TestData.core, TestData.preference, TestData.room
        )
        coEvery { librariesFetcher.fetchLibraries() } returns refreshedLibraries

        // When
        libraryRepository.refreshLibraries()

        // Then
        val actualPinStates = fakeLibraryDao.getLibraries().map { it.pinned == 1 }
        Truth.assertThat(actualPinStates).isEqualTo(listOf(true, false, false))
    }

    @Test
    fun `refresh date is saved when libraries are refreshed`() = runTest {
        // Given
        coEvery { librariesFetcher.fetchLibraries() } returns emptyList()

        // When
        libraryRepository.refreshLibraries()

        // Then
        Truth.assertThat(fakeStorageRepository.getRefreshDateAsFlow().first())
            .isEqualTo(REFRESH_DATE)
    }

}