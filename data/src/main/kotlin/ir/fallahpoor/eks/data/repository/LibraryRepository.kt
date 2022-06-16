package ir.fallahpoor.eks.data.repository

import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    suspend fun getLibraries(
        sortOrder: SortOrder = SortOrder.A_TO_Z,
        searchQuery: String = ""
    ): List<Library>

    suspend fun refreshLibraries()

    suspend fun pinLibrary(library: Library, pinned: Boolean)

    fun getRefreshDate(): Flow<String>

    fun getSortOrderAsFlow(): Flow<SortOrder>

    fun getSortOrder(): SortOrder

    suspend fun saveSortOrder(sortOrder: SortOrder)

}