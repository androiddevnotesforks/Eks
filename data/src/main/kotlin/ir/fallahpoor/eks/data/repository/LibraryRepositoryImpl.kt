package ir.fallahpoor.eks.data.repository

import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.storage.Storage
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LibraryRepositoryImpl
@Inject constructor(
    private val storage: Storage,
    private val libraryDao: LibraryDao,
    private val librariesFetcher: LibrariesFetcher,
    private val dateProvider: DateProvider
) : LibraryRepository {

    override suspend fun getLibraries(sortOrder: SortOrder, searchQuery: String): List<Library> {
        fetchLibrariesIfCacheIsEmpty()
        return libraryDao.getAllLibraries(searchQuery).sort(sortOrder)
    }

    private fun List<Library>.sort(sortOrder: SortOrder): List<Library> =
        when (sortOrder) {
            SortOrder.A_TO_Z -> sortedBy { it.name }
            SortOrder.Z_TO_A -> sortedByDescending { it.name }
            SortOrder.PINNED_FIRST -> sortedByDescending { it.pinned }
        }

    private suspend fun fetchLibrariesIfCacheIsEmpty() {
        val librariesCount: Int = libraryDao.getLibrariesCount()
        if (librariesCount == 0) {
            refreshLibraries()
        }
    }

    override suspend fun refreshLibraries() {
        var newLibraries: List<Library> = librariesFetcher.fetchLibraries()
        storage.setRefreshDate(
            dateProvider.getCurrentDate(
                SimpleDateFormat(
                    "MMM dd HH:mm",
                    Locale.US
                )
            )
        )
        val pinnedOldLibraryNames = libraryDao.getAllLibraries()
            .filter { it.pinned == 1 }
            .map { it.name }
        newLibraries = newLibraries.map {
            if (it.name in pinnedOldLibraryNames) {
                it.copy(pinned = 1)
            } else {
                it
            }
        }
        libraryDao.deleteAllLibraries()
        libraryDao.insertLibrary(newLibraries)
    }

    override suspend fun pinLibrary(library: Library, pinned: Boolean) {
        val newLibrary = library.copy(pinned = if (pinned) 1 else 0)
        libraryDao.updateLibrary(newLibrary)
    }

    override fun getRefreshDate(): Flow<String> = storage.getRefreshDateAsFlow()

    override fun getSortOrderAsFlow(): Flow<SortOrder> = storage.getSortOrderAsFlow()

    override fun getSortOrder(): SortOrder = storage.getSortOrder()

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        storage.setSortOrder(sortOrder)
    }

}
