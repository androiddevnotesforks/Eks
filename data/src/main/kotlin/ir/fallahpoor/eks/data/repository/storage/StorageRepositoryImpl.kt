package ir.fallahpoor.eks.data.repository.storage

import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.storage.Storage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class StorageRepositoryImpl
@Inject constructor(
    private val storage: Storage
) : StorageRepository {

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        storage.setSortOrder(sortOrder)
    }

    override fun getSortOrder(): SortOrder = storage.getSortOrder()

    override fun getSortOrderAsFlow(): Flow<SortOrder> = storage.getSortOrderAsFlow()

    override suspend fun saveRefreshDate(refreshDate: String) {
        storage.setRefreshDate(refreshDate)
    }

    override fun getRefreshDateAsFlow(): Flow<String> = storage.getRefreshDateAsFlow()

}