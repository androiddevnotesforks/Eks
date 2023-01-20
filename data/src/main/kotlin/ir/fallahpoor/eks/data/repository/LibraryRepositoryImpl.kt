package ir.fallahpoor.eks.data.repository

import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.database.entity.LibraryEntity
import ir.fallahpoor.eks.data.database.entity.toLibrary
import ir.fallahpoor.eks.data.network.LibrariesFetcher
import ir.fallahpoor.eks.data.network.dto.LibraryDto
import ir.fallahpoor.eks.data.network.dto.toLibraryEntity
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.toLibraryEntity
import ir.fallahpoor.eks.data.storage.Storage
import javax.inject.Inject

internal class LibraryRepositoryImpl
@Inject constructor(
    private val storage: Storage,
    private val libraryDao: LibraryDao,
    private val librariesFetcher: LibrariesFetcher,
    private val dateProvider: DateProvider
) : LibraryRepository {

    override suspend fun getLibraries(
        sortOrder: SortOrder,
        searchQuery: String
    ): List<Library> {
        fetchLibrariesIfCacheIsEmpty()
        return libraryDao.getLibraries(searchQuery)
            .map(LibraryEntity::toLibrary)
            .sort(sortOrder)
    }

    private suspend fun fetchLibrariesIfCacheIsEmpty() {
        val librariesCount: Int = libraryDao.getLibrariesCount()
        if (librariesCount == 0) {
            refreshLibraries()
        }
    }

    private fun List<Library>.sort(sortOrder: SortOrder): List<Library> = when (sortOrder) {
        SortOrder.A_TO_Z -> sortedBy { it.name }
        SortOrder.Z_TO_A -> sortedByDescending { it.name }
        SortOrder.PINNED_FIRST -> sortedByDescending { it.isPinned }
    }

    override suspend fun refreshLibraries() {
        val refreshedLibraries = getRefreshedLibraries()
        storage.setRefreshDate(dateProvider.getCurrentDate())
        libraryDao.deleteLibraries()
        libraryDao.insertLibraries(refreshedLibraries)
    }

    private suspend fun getRefreshedLibraries(): List<LibraryEntity> {
        val newLibraries: List<LibraryDto> = librariesFetcher.fetchLibraries()
        val pinnedOldLibraryNames = libraryDao.getLibraries()
            .filter { it.pinned == 1 }
            .map { it.name }
            .toSet()
        return newLibraries
            .map(LibraryDto::toLibraryEntity)
            .map {
                if (it.name in pinnedOldLibraryNames) {
                    it.copy(pinned = 1)
                } else {
                    it
                }
            }
    }

    override suspend fun pinLibrary(library: Library, pinned: Boolean) {
        val newLibrary = library.toLibraryEntity().copy(pinned = if (pinned) 1 else 0)
        libraryDao.updateLibrary(newLibrary)
    }

}
