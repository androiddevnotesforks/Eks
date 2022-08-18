package ir.fallahpoor.eks.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

// TODO: Remove the remaining "runBlocking" calls.

class LocalStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : Storage {

    companion object {
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_REFRESH_DATE = "refresh_date"
        val DEFAULT_SORT_ORDER = SortOrder.A_TO_Z
    }

    override suspend fun setSortOrder(sortOrder: SortOrder) {
        putString(KEY_SORT_ORDER, sortOrder.name)
    }

    override fun getSortOrder(): SortOrder {
        var sortOrderStr: String?
        runBlocking {
            sortOrderStr = getString(KEY_SORT_ORDER)
        }
        return SortOrder.valueOf(sortOrderStr ?: DEFAULT_SORT_ORDER.name)
    }

    override fun getSortOrderAsFlow(): Flow<SortOrder> {
        val key = stringPreferencesKey(KEY_SORT_ORDER)
        return getPreferencesFlow { preferences ->
            SortOrder.valueOf(preferences[key] ?: DEFAULT_SORT_ORDER.name)
        }
    }

    override suspend fun setRefreshDate(date: String) {
        putString(KEY_REFRESH_DATE, date)
    }

    override fun getRefreshDateAsFlow(): Flow<String> {
        val key = stringPreferencesKey(KEY_REFRESH_DATE)
        return getPreferencesFlow { preferences ->
            preferences[key] ?: Constants.NOT_AVAILABLE
        }
    }

    private suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        try {
            dataStore.edit { preferences ->
                preferences[prefKey] = value
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private suspend fun getString(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        val flow: Flow<String?> = getPreferencesFlow { preferences ->
            preferences[prefKey]
        }
        return flow.first()
    }

    private fun <T> getPreferencesFlow(action: suspend (Preferences) -> T): Flow<T> =
        dataStore.data.catch { exception ->
            Timber.e(exception)
            emit(emptyPreferences())
        }.map { preferences ->
            action(preferences)
        }

}