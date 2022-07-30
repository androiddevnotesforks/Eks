package ir.fallahpoor.eks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.fallahpoor.eks.data.Constants.NOT_AVAILABLE
import ir.fallahpoor.eks.data.database.DatabaseContract

@Entity(tableName = DatabaseContract.TABLE_NAME)
data class Library(
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
    val stableVersion: Version = Version(),
    @Embedded(prefix = DatabaseContract.PREFIX_RC_VERSION)
    val rcVersion: Version = Version(),
    @Embedded(prefix = DatabaseContract.PREFIX_BETA_VERSION)
    val betaVersion: Version = Version(),
    @Embedded(prefix = DatabaseContract.PREFIX_ALPHA_VERSION)
    val alphaVersion: Version = Version(),
    @ColumnInfo(name = DatabaseContract.FIELD_PINNED)
    val pinned: Int = 0
) {

    fun isUpdatedComparedTo(library: Library) =
        isVersionUpdated(stableVersion.name, library.stableVersion.name) ||
                isVersionUpdated(rcVersion.name, library.rcVersion.name) ||
                isVersionUpdated(betaVersion.name, library.betaVersion.name) ||
                isVersionUpdated(alphaVersion.name, library.alphaVersion.name)

    private fun isVersionUpdated(oldVersionName: String, newVersionName: String): Boolean =
        newVersionName != NOT_AVAILABLE && oldVersionName != newVersionName

}
