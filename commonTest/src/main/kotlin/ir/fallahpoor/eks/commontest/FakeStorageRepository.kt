package ir.fallahpoor.eks.commontest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.repository.storage.StorageRepository
import kotlinx.coroutines.flow.Flow

class FakeStorageRepository : StorageRepository {
    private val sortOrderLiveData = MutableLiveData(SortOrder.A_TO_Z)
    private val refreshDateLiveData = MutableLiveData(Constants.NOT_AVAILABLE)

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        sortOrderLiveData.value = sortOrder
    }

    override fun getSortOrder(): SortOrder = sortOrderLiveData.value ?: SortOrder.A_TO_Z

    override fun getSortOrderAsFlow(): Flow<SortOrder> = sortOrderLiveData.asFlow()

    override suspend fun saveRefreshDate(refreshDate: String) {
        refreshDateLiveData.value = refreshDate
    }

    override fun getRefreshDateAsFlow(): Flow<String> = refreshDateLiveData.asFlow()
}