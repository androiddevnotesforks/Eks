package ir.fallahpoor.eks.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.MainDispatcherRule
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.storage.LocalStorage.Companion.KEY_REFRESH_DATE
import ir.fallahpoor.eks.data.storage.LocalStorage.Companion.KEY_SORT_ORDER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocalStorageTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var localStorage: LocalStorage
    private lateinit var preferencesScope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun runBeforeEachTest() {
        preferencesScope = CoroutineScope(mainDispatcherRule.testDispatcher + Job())
        dataStore = PreferenceDataStoreFactory.create(scope = preferencesScope) {
            context.preferencesDataStoreFile(
                "test-preferences-file"
            )
        }
        localStorage = LocalStorage(dataStore)
    }

    @Test
    fun `sort order is saved`() = runTest {

        // Given
        val expectedSortOrder = SortOrder.Z_TO_A

        // When
        localStorage.setSortOrder(expectedSortOrder)

        // Then
        val actualSortOrder = SortOrder.valueOf(getString(KEY_SORT_ORDER) ?: "")
        Truth.assertThat(actualSortOrder).isEqualTo(expectedSortOrder)

    }

    @Test
    fun `saved sort order is returned`() = runTest {

        // Given
        val expectedSortOrder = SortOrder.PINNED_FIRST
        putString(KEY_SORT_ORDER, expectedSortOrder.name)

        // When
        val actualSortOrder = localStorage.getSortOrder()

        // Then
        Truth.assertThat(actualSortOrder).isEqualTo(expectedSortOrder)

    }

    @Test
    fun `default sort order is returned when there is no saved sort order`() = runTest {

        // Given there is no saved sort order

        // When
        val actualSortOrder = localStorage.getSortOrder()

        // Then
        Truth.assertThat(actualSortOrder).isEqualTo(SortOrder.A_TO_Z)

    }

    @Test
    fun `refresh date is saved`() = runTest {

        // Given
        val expectedRefreshDate = "15:30, March"

        // When
        localStorage.setRefreshDate(expectedRefreshDate)

        // Then
        val actualLastUpdateCheckDate = getString(KEY_REFRESH_DATE)
        Truth.assertThat(actualLastUpdateCheckDate).isEqualTo(expectedRefreshDate)

    }

    @Test
    fun `refresh date flow emits a new value when refresh date is updated`() = runTest {

        // Given
        val refreshDate = "March 1st 2022"
        val actualRefreshDates = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher()) {
            localStorage.getRefreshDateAsFlow().toList(actualRefreshDates)
        }

        // When
        putString(KEY_REFRESH_DATE, refreshDate)

        // Then
        Truth.assertThat(actualRefreshDates)
            .isEqualTo(listOf(Constants.NOT_AVAILABLE, refreshDate))

        job.cancel()

    }

    @Test
    fun `sort order flow emits a new value when sort order is updated`() = runTest {

        // Given
        val sortOrder = SortOrder.PINNED_FIRST
        val actualSortOrders = mutableListOf<SortOrder>()
        val job = launch(UnconfinedTestDispatcher()) {
            localStorage.getSortOrderAsFlow().toList(actualSortOrders)
        }

        // When
        putString(KEY_SORT_ORDER, sortOrder.name)

        // Then
        Truth.assertThat(actualSortOrders)
            .isEqualTo(listOf(LocalStorage.DEFAULT_SORT_ORDER, sortOrder))

        job.cancel()

    }

    private suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[prefKey] = value
        }
    }

    private suspend fun getString(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        val flow: Flow<String?> = dataStore.data.map { preferences ->
            preferences[prefKey]
        }
        return flow.first()
    }

}