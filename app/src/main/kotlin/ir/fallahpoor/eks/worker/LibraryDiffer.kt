package ir.fallahpoor.eks.worker

import ir.fallahpoor.eks.data.entity.Library
import javax.inject.Inject

class LibraryDiffer @Inject constructor() {

    enum class Type {
        NEW,
        REMOVED,
        UPDATED,
        UNCHANGED
    }

    fun diffLibraries(
        oldLibraries: List<Library>,
        refreshedLibraries: List<Library>
    ): Map<Type, List<Library>> {

        val newLibraries: List<Library> = refreshedLibraries.minus(oldLibraries.toSet())
        val removedLibraries: List<Library> = oldLibraries.minus(refreshedLibraries.toSet())
        val oldLibrariesMinusRemovedOnes: List<Library> =
            oldLibraries.minus(removedLibraries.toSet()).sortedBy { it.name }
        val refreshedLibrariesMinusNewOnes: List<Library> =
            refreshedLibraries.minus(newLibraries.toSet()).sortedBy { it.name }
        val updatedLibraries = mutableListOf<Library>()
        val unchangedLibraries = mutableListOf<Library>()

        oldLibrariesMinusRemovedOnes.forEachIndexed { index: Int, library: Library ->
            if (library.isUpdatedComparedTo(refreshedLibrariesMinusNewOnes[index])) {
                updatedLibraries += library
            } else {
                unchangedLibraries += library
            }
        }

        return mapOf(
            Type.NEW to newLibraries,
            Type.REMOVED to removedLibraries,
            Type.UPDATED to updatedLibraries,
            Type.UNCHANGED to unchangedLibraries
        )

    }

}