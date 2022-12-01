package ir.fallahpoor.eks.data.database.entity

import androidx.room.ColumnInfo
import ir.fallahpoor.eks.data.Constants.NOT_AVAILABLE
import ir.fallahpoor.eks.data.database.DatabaseContract
import ir.fallahpoor.eks.data.repository.model.Version

data class VersionEntity(
    @ColumnInfo(name = DatabaseContract.FIELD_VERSION_NAME)
    val name: String = NOT_AVAILABLE,
    @ColumnInfo(name = DatabaseContract.FIELD_RELEASE_NOTES_URL)
    val releaseNotesUrl: String = NOT_AVAILABLE
)

fun VersionEntity.toVersion() = Version(
    name = this.name,
    releaseNotesUrl = this.releaseNotesUrl
)