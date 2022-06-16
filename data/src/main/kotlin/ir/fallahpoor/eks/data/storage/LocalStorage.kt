package ir.fallahpoor.eks.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

// TODO: Remove the remaining "runBlocking" calls.

class LocalStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : Storage {

    companion object {
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_REFRESH_DATE = "refresh_date"
    }

    override suspend fun setSortOrder(sortOrder: SortOrder) {
        putString(KEY_SORT_ORDER, sortOrder.name)
    }

    override fun getSortOrder(): SortOrder {
        var sortOrderStr: String?
        runBlocking {
            sortOrderStr = getString(KEY_SORT_ORDER)
        }
        return SortOrder.valueOf(sortOrderStr ?: SortOrder.A_TO_Z.name)
    }

    override fun getSortOrderAsFlow(): Flow<SortOrder> {
        val prefKey = stringPreferencesKey(KEY_SORT_ORDER)
        return dataStore.data
            .map { preferences ->
                SortOrder.valueOf(preferences[prefKey] ?: SortOrder.A_TO_Z.name)
            }
    }

    override suspend fun setRefreshDate(date: String) {
        putString(KEY_REFRESH_DATE, date)
    }

    override fun getRefreshDateAsFlow(): Flow<String> {
        val prefKey = stringPreferencesKey(KEY_REFRESH_DATE)
        return dataStore.data
            .map { preferences ->
                preferences[prefKey] ?: Constants.NOT_AVAILABLE
            }
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