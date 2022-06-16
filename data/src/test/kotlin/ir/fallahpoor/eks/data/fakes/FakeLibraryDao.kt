package ir.fallahpoor.eks.data.fakes

import androidx.lifecycle.MutableLiveData
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.entity.Library

class FakeLibraryDao : LibraryDao {

    private val librariesLiveData = MutableLiveData<List<Library>>()
    private val libraries = mutableListOf<Library>()

    override suspend fun getLibrariesCount(): Int = libraries.size

    override suspend fun getAllLibraries(searchQuery: String): List<Library> =
        libraries.filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.name }

    override suspend fun insertLibrary(libraries: List<Library>) {
        this.libraries += libraries
        updatedLibrariesLiveData()
    }

    override suspend fun deleteAllLibraries() {
        libraries.clear()
        updatedLibrariesLiveData()
    }

    override suspend fun updateLibrary(library: Library) {
        val removed = libraries.remove(getLibrary(library.name))
        if (removed) {
            libraries.add(library)
            updatedLibrariesLiveData()
        }
    }

    private fun getLibrary(libraryName: String): Library? =
        libraries.find { it.name.contentEquals(libraryName, ignoreCase = true) }

    private fun updatedLibrariesLiveData() {
        librariesLiveData.value = libraries
    }

}