package ir.fallahpoor.eks.data.network.dto

import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.database.entity.LibraryEntity

internal data class LibraryDto(
    val name: String = Constants.NOT_AVAILABLE,
    val description: String = Constants.NOT_AVAILABLE,
    val url: String = Constants.NOT_AVAILABLE,
    val releaseDate: String = Constants.NOT_AVAILABLE,
    val stableVersion: VersionDto = VersionDto(),
    val rcVersion: VersionDto = VersionDto(),
    val betaVersion: VersionDto = VersionDto(),
    val alphaVersion: VersionDto = VersionDto()
)

internal fun LibraryDto.toLibraryEntity(isPinned: Boolean = false) = LibraryEntity(
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