package ir.fallahpoor.eks.commontest

import androidx.lifecycle.MutableLiveData
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.data.repository.model.Library
import java.io.IOException

class FakeLibraryRepository : LibraryRepository {
    var throwException = false
    var updateIsAvailable = false
    private val exception = IOException()
    private val initialLibraries = mutableListOf(
        TestData.Activity.old,
        TestData.Biometric.old,
        TestData.core,
        TestData.preference
    )
    private val refreshedLibraries = listOf(
        TestData.Activity.new,
        TestData.Biometric.new,
        TestData.core,
        TestData.room
    )
    private val librariesLiveData = MutableLiveData<List<Library>>(initialLibraries)

    override suspend fun getLibraries(
        sortOrder: SortOrder,
        searchQuery: String
    ): List<Library> =
        throwExceptionOrRun {
            initialLibraries.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }.sort(sortOrder)
        }

    private fun List<Library>.sort(sortOrder: SortOrder): List<Library> =
        when (sortOrder) {
            SortOrder.A_TO_Z -> sortedBy { it.name }
            SortOrder.Z_TO_A -> sortedByDescending { it.name }
            SortOrder.PINNED_FIRST -> sortedByDescending { it.isPinned }
        }

    override suspend fun refreshLibraries() = throwExceptionOrRun {
        if (updateIsAvailable) {
            updateLibrariesLiveData(refreshedLibraries)
        } else {
            updateLibrariesLiveData(initialLibraries)
        }
    }

    override suspend fun pinLibrary(library: Library, pinned: Boolean) = throwExceptionOrRun {
        updateLibrary(library.copy(isPinned = pinned))
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

    private fun <T> throwExceptionOrRun(block: () -> T): T = if (throwException) {
        throw exception
    } else {
        block()
    }
}