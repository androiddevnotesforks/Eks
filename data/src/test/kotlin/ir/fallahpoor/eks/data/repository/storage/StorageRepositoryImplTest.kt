package ir.fallahpoor.eks.data.repository.storage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.MainDispatcherRule
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.fakes.FakeStorage
import ir.fallahpoor.eks.data.storage.LocalStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StorageRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var storageRepository: StorageRepositoryImpl
    private lateinit var fakeStorage: FakeStorage

    @Before
    fun runBeforeEachTest() {
        fakeStorage = FakeStorage()
        storageRepository = StorageRepositoryImpl(storage = fakeStorage)
    }

    @Test
    fun `sort order is saved`() = runTest {
        // Given
        val expectedSortOrder = SortOrder.PINNED_FIRST

        // When
        storageRepository.saveSortOrder(expectedSortOrder)

        // Then
        Truth.assertThat(fakeStorage.getSortOrder()).isEqualTo(expectedSortOrder)
    }

    @Test
    fun `saved sort order is returned`() = runTest {
        // Given
        val expectedSortOrder = SortOrder.Z_TO_A
        fakeStorage.setSortOrder(expectedSortOrder)

        // When
        val actualSortOrder: SortOrder = storageRepository.getSortOrder()

        // Then
        Truth.assertThat(actualSortOrder).isEqualTo(expectedSortOrder)
    }

    @Test
    fun `sort order flow emits a new value when sort order is updated`() = runTest {
        storageRepository.getSortOrderAsFlow().test {
            // When
            fakeStorage.setSortOrder(SortOrder.PINNED_FIRST)
            fakeStorage.setSortOrder(SortOrder.Z_TO_A)

            // Then
            Truth.assertThat(awaitItem()).isEqualTo(LocalStorage.DEFAULT_SORT_ORDER)
            Truth.assertThat(awaitItem()).isEqualTo(SortOrder.PINNED_FIRST)
            Truth.assertThat(awaitItem()).isEqualTo(SortOrder.Z_TO_A)
        }
    }

    @Test
    fun `refresh date is saved`() = runTest {
        // Given
        val expectedRefreshDate = "March 1st, 2022"

        // When
        storageRepository.saveRefreshDate(expectedRefreshDate)

        // Then
        val actualRefreshDate = fakeStorage.getRefreshDateAsFlow().first()
        Truth.assertThat(actualRefreshDate).isEqualTo(expectedRefreshDate)
    }

    @Test
    fun `refresh date is returned`() = runTest {
        // Given
        val expectedRefreshDate = "September 11, 2001"
        fakeStorage.setRefreshDate(expectedRefreshDate)

        // When
        val actualRefreshDate: String = storageRepository.getRefreshDateAsFlow().first()

        // Then
        Truth.assertThat(actualRefreshDate).isEqualTo(expectedRefreshDate)
    }

}