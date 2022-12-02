package ir.fallahpoor.eks.data.fakes

import androidx.lifecycle.MutableLiveData
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.database.entity.LibraryEntity

internal class FakeLibraryDao : LibraryDao {

    private val librariesLiveData = MutableLiveData<List<LibraryEntity>>()
    private val libraries = mutableListOf<LibraryEntity>()

    override suspend fun getLibrariesCount(): Int = libraries.size

    override suspend fun getLibraries(searchQuery: String): List<LibraryEntity> =
        libraries.filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.name }

    override suspend fun insertLibraries(libraries: List<LibraryEntity>) {
        val libraryNames = libraries.map { it.name }
        removeElementsIf(this.libraries) { library -> library.name in libraryNames }
        this.libraries += libraries
        updatedLibrariesLiveData(this.libraries)
    }

    private fun removeElementsIf(
        libraries: MutableList<LibraryEntity>,
        filter: (LibraryEntity) -> Boolean
    ) {
        val iterator: MutableIterator<LibraryEntity> = libraries.iterator()
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

    override suspend fun updateLibrary(library: LibraryEntity) {
        val removed = libraries.remove(getLibrary(library.name))
        if (removed) {
            libraries += library
            updatedLibrariesLiveData(libraries)
        }
    }

    private fun getLibrary(libraryName: String): LibraryEntity? =
        libraries.find { it.name.contentEquals(libraryName, ignoreCase = true) }

    private fun updatedLibrariesLiveData(libraries: List<LibraryEntity>) {
        librariesLiveData.value = libraries
    }

}