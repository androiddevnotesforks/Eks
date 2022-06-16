package ir.fallahpoor.eks.fakes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.NightMode
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.storage.Storage
import kotlinx.coroutines.flow.Flow

class FakeStorage : Storage {

    private var refreshDateLiveData = MutableLiveData(Constants.NOT_AVAILABLE)
    private var nightModeLiveData = MutableLiveData(NightMode.AUTO)
    private var sortOrderLiveData = MutableLiveData(SortOrder.A_TO_Z)

    override suspend fun setSortOrder(sortOrder: SortOrder) {
        sortOrderLiveData.value = sortOrder
    }

    override fun getSortOrder() = sortOrderLiveData.value!!

    override fun getSortOrderAsFlow(): Flow<SortOrder> = sortOrderLiveData.asFlow()

    override suspend fun setRefreshDate(date: String) {
        refreshDateLiveData.value = date
    }

    override fun getRefreshDateAsFlow(): Flow<String> = refreshDateLiveData.asFlow()

    override suspend fun setNightMode(nightMode: NightMode) {
        nightModeLiveData.value = nightMode
    }

    override fun getNightMode() = nightModeLiveData.value!!

    override fun getNightModeAsFlow(): Flow<NightMode> = nightModeLiveData.asFlow()

}