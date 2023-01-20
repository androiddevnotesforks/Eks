package ir.fallahpoor.eks.data.fakes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.storage.LocalStorage
import ir.fallahpoor.eks.data.storage.Storage
import kotlinx.coroutines.flow.Flow

class FakeStorage : Storage {

    private var refreshDateLiveData = MutableLiveData(LocalStorage.DEFAULT_REFRESH_DATE)
    private var sortOrderLiveData = MutableLiveData(LocalStorage.DEFAULT_SORT_ORDER)

    override suspend fun setSortOrder(sortOrder: SortOrder) {
        sortOrderLiveData.value = sortOrder
    }

    override fun getSortOrder() = sortOrderLiveData.value!!

    override fun getSortOrderAsFlow(): Flow<SortOrder> = sortOrderLiveData.asFlow()

    override suspend fun setRefreshDate(date: String) {
        refreshDateLiveData.value = date
    }

    override fun getRefreshDateAsFlow(): Flow<String> = refreshDateLiveData.asFlow()

}