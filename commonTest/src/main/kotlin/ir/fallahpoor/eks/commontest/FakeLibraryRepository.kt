package ir.fallahpoor.eks.commontest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeLibraryRepository : LibraryRepository {

    companion object {
        const val REFRESH_DATE = "N/A"
    }

    var throwException = false
    var exception = IOException()
    var updateIsAvailable = false

    private val initialLibraries = mutableListOf(
        TestData.activityOld,
        TestData.biometricOld,
        TestData.core,
        TestData.preference
    )
    private val refreshedLibraries = listOf(
        TestData.activityNew,
        TestData.biometricNew,
        TestData.core,
        TestData.room
    )
    private val sortOrderLiveData = MutableLiveData(SortOrder.A_TO_Z)
    private val librariesLiveData = MutableLiveData<List<Library>>(initialLibraries)

    override suspend fun getLibraries(sortOrder: SortOrder, searchQuery: String): List<Library> =
        throwExceptionOrRun {
            initialLibraries.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }.sort(sortOrder)
        }

    private fun List<Library>.sort(sortOrder: SortOrder): List<Library> = when (sortOrder) {
        SortOrder.A_TO_Z -> sortedBy { it.name }
        SortOrder.Z_TO_A -> sortedByDescending { it.name }
        SortOrder.PINNED_FIRST -> sortedByDescending { it.pinned }
    }

    override suspend fun refreshLibraries() = throwExceptionOrRun {
        if (updateIsAvailable) {
            updateLibrariesLiveData(refreshedLibraries)
        } else {
            updateLibrariesLiveData(initialLibraries)
        }
    }

    override suspend fun pinLibrary(library: Library, pinned: Boolean) = throwExceptionOrRun {
        updateLibrary(library.copy(pinned = if (pinned) 1 else 0))
    }

    private fun updateLibrary(library: Library) {
        val libraryToRemove: Library? =
            initialLibraries.find { it.name.equals(library.name, ignoreCase = true) }
        if (libraryToRemove != null) {
            initialLibraries -= libraryToRemove
            initialLibraries += library
            updateLibrariesLiveData(initialLibraries)
        }
    }

    private fun updateLibrariesLiveData(libraries: List<Library>) {
        librariesLiveData.value = libraries
    }

    override fun getRefreshDate(): Flow<String> = throwExceptionOrRun {
        flow {
            emit(REFRESH_DATE)
        }
    }

    private fun <T> throwExceptionOrRun(block: () -> T): T = if (throwException) {
        throw exception
    } else {
        block()
    }

    override fun getSortOrderAsFlow(): Flow<SortOrder> = sortOrderLiveData.asFlow()

    override fun getSortOrder(): SortOrder = sortOrderLiveData.value ?: SortOrder.A_TO_Z

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        sortOrderLiveData.value = sortOrder
    }

}