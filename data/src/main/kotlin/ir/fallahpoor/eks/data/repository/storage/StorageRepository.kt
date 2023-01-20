package ir.fallahpoor.eks.data.repository.storage

import ir.fallahpoor.eks.data.SortOrder
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    suspend fun saveSortOrder(sortOrder: SortOrder)

    fun getSortOrder(): SortOrder

    fun getSortOrderAsFlow(): Flow<SortOrder>

    suspend fun saveRefreshDate(refreshDate: String)

    fun getRefreshDateAsFlow(): Flow<String>

}