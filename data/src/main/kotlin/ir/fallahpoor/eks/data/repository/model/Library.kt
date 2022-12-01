package ir.fallahpoor.eks.data.repository.model

import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.database.entity.LibraryEntity

data class Library(
    val name: String = Constants.NOT_AVAILABLE,
    val description: String = Constants.NOT_AVAILABLE,
    val url: String = Constants.NOT_AVAILABLE,
    val releaseDate: String = Constants.NOT_AVAILABLE,
    val stableVersion: Version = Version(),
    val rcVersion: Version = Version(),
    val betaVersion: Version = Version(),
    val alphaVersion: Version = Version(),
    val isPinned: Boolean = false
) {

    fun isUpdatedComparedTo(library: Library) =
        isVersionUpdated(stableVersion.name, library.stableVersion.name) ||
                isVersionUpdated(rcVersion.name, library.rcVersion.name) ||
                isVersionUpdated(betaVersion.name, library.betaVersion.name) ||
                isVersionUpdated(alphaVersion.name, library.alphaVersion.name)

    private fun isVersionUpdated(oldVersionName: String, newVersionName: String): Boolean =
        newVersionName != Constants.NOT_AVAILABLE && oldVersionName != newVersionName

}

internal fun Library.toLibraryEntity() = LibraryEntity(
    name = this.name,
    description = this.description,
    url = this.url,
    releaseDate = this.releaseDate,
    stableVersion = this.stableVersion.toVersionEntity(),
    rcVersion = this.rcVersion.toVersionEntity(),
    betaVersion = this.betaVersion.toVersionEntity(),
    alphaVersion = this.alphaVersion.toVersionEntity(),
    pinned = if (isPinned) 1 else 0
)