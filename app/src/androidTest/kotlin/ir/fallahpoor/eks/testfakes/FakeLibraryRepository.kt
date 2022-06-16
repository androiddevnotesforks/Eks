package ir.fallahpoor.eks.testfakes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ir.fallahpoor.eks.TestData
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeLibraryRepository : LibraryRepository {

    var throwException = false
    var updatesAvailable = false

    companion object {
        const val REFRESH_DATE = "N/A"
    }

    private var sortOrderLiveData = MutableLiveData(SortOrder.A_TO_Z)
    private val libraries = TestData.libraries
    private val refreshedLibraries = TestData.refreshedLibraries
    private val librariesLiveData = MutableLiveData<List<Library>>(libraries)

    private fun <T> throwExceptionOrRunBlock(block: () -> T): T =
        if (throwException) {
            throw IOException()
        } else {
            block()
        }

    override suspend fun getLibraries(sortOrder: SortOrder, searchQuery: String): List<Library> =
        libraries.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }.sort(sortOrder)

    private fun List<Library>.sort(sortOrder: SortOrder): List<Library> = when (sortOrder) {
        SortOrder.A_TO_Z -> sortedBy { it.name }
        SortOrder.Z_TO_A -> sortedByDescending { it.name }
        SortOrder.PINNED_FIRST -> sortedByDescending { it.pinned }
    }

    override suspend fun refreshLibraries() {
        throwExceptionOrRunBlock {
            if (updatesAvailable) {
                updateLibrariesLiveData(refreshedLibraries)
            } else {
                updateLibrariesLiveData(libraries)
            }
        }
    }

    override suspend fun pinLibrary(library: Library, pinned: Boolean) {
        throwExceptionOrRunBlock {
            updateLibrary(library.copy(pinned = if (pinned) 1 else 0))
        }
    }

    private fun updateLibrary(library: Library) {
        val removed: Boolean = libraries.removeIf {
            it.name.equals(library.name, ignoreCase = true)
        }
        if (removed) {
            libraries += library
            updateLibrariesLiveData(libraries)
        }
    }

    private fun updateLibrariesLiveData(libraries: List<Library>) {
        librariesLiveData.value = libraries
    }

    override fun getRefreshDate(): Flow<String> = throwExceptionOrRunBlock {
        flow {
            emit(REFRESH_DATE)
        }
    }

    override fun getSortOrderAsFlow(): Flow<SortOrder> = sortOrderLiveData.asFlow()

    override fun getSortOrder(): SortOrder = sortOrderLiveData.value!!

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        sortOrderLiveData.value = sortOrder
    }

}