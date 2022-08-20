package ir.fallahpoor.eks.commontest

import androidx.lifecycle.MutableLiveData
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.entity.Library

class FakeLibraryDao : LibraryDao {

    private val librariesLiveData = MutableLiveData<List<Library>>()
    private val libraries = mutableListOf<Library>()

    override suspend fun getLibrariesCount(): Int = libraries.size

    override suspend fun getLibraries(searchQuery: String): List<Library> =
        libraries.filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.name }

    override suspend fun insertLibraries(libraries: List<Library>) {
        val libraryNames = libraries.map { it.name }
        removeElementsIf(this.libraries) { library -> library.name in libraryNames }
        this.libraries += libraries
        updatedLibrariesLiveData(this.libraries)
    }

    private fun removeElementsIf(libraries: MutableList<Library>, filter: (Library) -> Boolean) {
        val iterator: MutableIterator<Library> = libraries.iterator()
        while (iterator.hasNext()) {
            if (filter(iterator.next())) {
                iterator.remove()
            }
        }
    }

    override suspend fun deleteLibraries() {
        libraries.clear()
        updatedLibrariesLiveData(libraries)
    }

    override suspend fun updateLibrary(library: Library) {
        val removed = libraries.remove(getLibrary(library.name))
        if (removed) {
            libraries += library
            updatedLibrariesLiveData(libraries)
        }
    }

    private fun getLibrary(libraryName: String): Library? =
        libraries.find { it.name.contentEquals(libraryName, ignoreCase = true) }

    private fun updatedLibrariesLiveData(libraries: List<Library>) {
        librariesLiveData.value = libraries
    }

}