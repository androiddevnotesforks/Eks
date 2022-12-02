package ir.fallahpoor.eks.data.database

internal object DatabaseContract {
    const val DATABASE_NAME = "Eks.db"
    const val TABLE_NAME = "library"

    const val FIELD_NAME = "name"
    const val FIELD_DESCRIPTION = "description"
    const val FIELD_URL = "url"
    const val FIELD_RELEASE_DATE = "release_date"
    const val FIELD_VERSION_NAME = "version_name"
    const val FIELD_RELEASE_NOTES_URL = "release_notes_url"
    const val FIELD_PINNED = "pinned"

    const val PREFIX_STABLE_VERSION = "stable_version_"
    const val PREFIX_RC_VERSION = "rc_version_"
    const val PREFIX_BETA_VERSION = "beta_version_"
    const val PREFIX_ALPHA_VERSION = "alpha_version_"
}