package ir.fallahpoor.eks.data.network.dto

import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.database.entity.VersionEntity

internal data class VersionDto(
    val name: String = Constants.NOT_AVAILABLE,
    val releaseNotesUrl: String = Constants.NOT_AVAILABLE
)

internal fun VersionDto.toVersionEntity() = VersionEntity(
    name = this.name,
    releaseNotesUrl = this.releaseNotesUrl
)