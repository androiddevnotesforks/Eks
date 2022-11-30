package ir.fallahpoor.eks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.fallahpoor.eks.data.Constants.NOT_AVAILABLE
import ir.fallahpoor.eks.data.database.DatabaseContract
import ir.fallahpoor.eks.data.model.Library

@Entity(tableName = DatabaseContract.TABLE_NAME)
data class LibraryEntity(
    @PrimaryKey
    @ColumnInfo(name = DatabaseContract.FIELD_NAME, collate = ColumnInfo.NOCASE)
    val name: String = NOT_AVAILABLE,
    @ColumnInfo(name = DatabaseContract.FIELD_DESCRIPTION)
    val description: String = NOT_AVAILABLE,
    @ColumnInfo(name = DatabaseContract.FIELD_URL)
    val url: String = NOT_AVAILABLE,
    @ColumnInfo(name = DatabaseContract.FIELD_RELEASE_DATE)
    val releaseDate: String = NOT_AVAILABLE,
    @Embedded(prefix = DatabaseContract.PREFIX_STABLE_VERSION)
    val stableVersion: VersionEntity = VersionEntity(),
    @Embedded(prefix = DatabaseContract.PREFIX_RC_VERSION)
    val rcVersion: VersionEntity = VersionEntity(),
    @Embedded(prefix = DatabaseContract.PREFIX_BETA_VERSION)
    val betaVersion: VersionEntity = VersionEntity(),
    @Embedded(prefix = DatabaseContract.PREFIX_ALPHA_VERSION)
    val alphaVersion: VersionEntity = VersionEntity(),
    @ColumnInfo(name = DatabaseContract.FIELD_PINNED)
    val pinned: Int = 0
)

fun LibraryEntity.toLibrary() = Library(
    name = this.name,
    description = this.description,
    url = this.url,
    releaseDate = this.releaseDate,
    stableVersion = this.stableVersion.toVersion(),
    rcVersion = this.rcVersion.toVersion(),
    betaVersion = this.betaVersion.toVersion(),
    alphaVersion = this.alphaVersion.toVersion(),
    isPinned = pinned == 1
)
