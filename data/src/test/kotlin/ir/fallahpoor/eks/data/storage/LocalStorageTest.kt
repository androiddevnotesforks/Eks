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
import ir.fallahpoor.eks.data.NightMode
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.storage.LocalStorage.Companion.KEY_NIGHT_MODE
import ir.fallahpoor.eks.data.storage.LocalStorage.Companion.KEY_REFRESH_DATE
import ir.fallahpoor.eks.data.storage.LocalStorage.Companion.KEY_SORT_ORDER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// TODO add tests for getSortOrderAsFlow(), getRefreshDateAsFlow(), and getNightModeAsFlow()

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class LocalStorageTest {

    private lateinit var localStorage: LocalStorage
    private lateinit var preferencesScope: CoroutineScope
    private lateinit var dataStore: DataStore<Preferences>
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun runBeforeEachTest() {
        val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        preferencesScope = CoroutineScope(testDispatcher + Job())
        dataStore = PreferenceDataStoreFactory.create(scope = preferencesScope) {
            context.preferencesDataStoreFile(
                "test-preferences-file"
            )
        }
        localStorage = LocalStorage(dataStore)
    }

    @After
    fun runAfterEachTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saved night mode is returned`() = runTest {

        // Given
        val expectedNightMode = NightMode.OFF
        putString(KEY_NIGHT_MODE, expectedNightMode.name)

        // When
        val actualNightMode: NightMode = localStorage.getNightMode()

        // Then
        Truth.assertThat(actualNightMode).isEqualTo(expectedNightMode)

    }

    @Test
    fun `default night mode is returned when there is no saved night mode`() {

        // Given there is no saved night mode

        // When
        val actualNightMode: NightMode = localStorage.getNightMode()

        // Then
        Truth.assertThat(actualNightMode).isEqualTo(NightMode.AUTO)

    }

    @Test
    fun `night mode is saved`() = runTest {

        // Given
        val expectedNightMode = NightMode.ON

        // When
        localStorage.setNightMode(expectedNightMode)

        // Then
        val actualNightMode = NightMode.valueOf(getString(KEY_NIGHT_MODE) ?: "")
        Truth.assertThat(actualNightMode).isEqualTo(expectedNightMode)

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

    private suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[prefKey] = value
        }
    }

    private suspend fun getString(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        val flow: Flow<String?> = dataStore.data
            .map { preferences ->
                preferences[prefKey]
            }
        return flow.first()
    }

}