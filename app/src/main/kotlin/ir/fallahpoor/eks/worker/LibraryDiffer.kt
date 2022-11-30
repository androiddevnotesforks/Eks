package ir.fallahpoor.eks.worker

import ir.fallahpoor.eks.data.model.Library
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

        val newLibraries: List<Library> = getNewLibraries(oldLibraries, refreshedLibraries)
        val removedLibraries: List<Library> =
            getRemovedLibraries(oldLibraries, refreshedLibraries)
        val oldLibrariesMinusRemovedOnes: List<Library> =
            getOldLibrariesMinusRemoveOnes(oldLibraries, removedLibraries).sortedBy { it.name }
        val refreshedLibrariesMinusNewOnes: List<Library> =
            getRefreshedLibrariesMinusRemoveOnes(
                refreshedLibraries,
                removedLibraries
            ).sortedBy { it.name }
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

    private fun getNewLibraries(
        oldLibraries: List<Library>,
        refreshedLibraries: List<Library>
    ): List<Library> {
        val oldLibraryNamesSet = oldLibraries.map { it.name }.toSet()
        return refreshedLibraries.filter { it.name !in oldLibraryNamesSet }
    }

    private fun getRemovedLibraries(
        oldLibraries: List<Library>,
        refreshedLibraries: List<Library>
    ): List<Library> {
        val refreshedLibraryNamesSet = refreshedLibraries.map { it.name }.toSet()
        return oldLibraries.filter { it.name !in refreshedLibraryNamesSet }
    }

    private fun getOldLibrariesMinusRemoveOnes(
        oldLibraries: List<Library>,
        removeLibraries: List<Library>
    ): List<Library> {
        val removedLibraryNamesSet = removeLibraries.map { it.name }.toSet()
        return oldLibraries.filter { it.name !in removedLibraryNamesSet }
    }

    private fun getRefreshedLibrariesMinusRemoveOnes(
        refreshedLibraries: List<Library>,
        removeLibraries: List<Library>
    ): List<Library> {
        val removedLibraryNamesSet = removeLibraries.map { it.name }.toSet()
        return refreshedLibraries.filter { it.name !in removedLibraryNamesSet }
    }

}