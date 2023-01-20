package ir.fallahpoor.eks.data.repository

import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.repository.model.Library

interface LibraryRepository {

    suspend fun getLibraries(
        sortOrder: SortOrder = SortOrder.A_TO_Z,
        searchQuery: String = ""
    ): List<Library>

    suspend fun refreshLibraries()

    suspend fun pinLibrary(library: Library, pinned: Boolean)

}