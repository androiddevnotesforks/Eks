package ir.fallahpoor.eks.data.entity

import androidx.room.ColumnInfo
import ir.fallahpoor.eks.data.Constants.NOT_AVAILABLE
import ir.fallahpoor.eks.data.database.DatabaseContract

data class Version(
    @ColumnInfo(name = DatabaseContract.FIELD_VERSION_NAME)
    val name: String = NOT_AVAILABLE,
    @ColumnInfo(name = DatabaseContract.FIELD_RELEASE_NOTES_URL)
    val releaseNotesUrl: String = NOT_AVAILABLE
)