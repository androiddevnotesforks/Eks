package ir.fallahpoor.eks.data.storage

import ir.fallahpoor.eks.data.NightMode
import ir.fallahpoor.eks.data.SortOrder
import kotlinx.coroutines.flow.Flow

// TODO all functions except those returning Flow should be suspend functions

interface Storage {

    suspend fun setSortOrder(sortOrder: SortOrder)

    fun getSortOrder(): SortOrder

    fun getSortOrderAsFlow(): Flow<SortOrder>

    suspend fun setRefreshDate(date: String)

    fun getRefreshDateAsFlow(): Flow<String>

    suspend fun setNightMode(nightMode: NightMode)

    fun getNightMode(): NightMode

    fun getNightModeAsFlow(): Flow<NightMode>

}