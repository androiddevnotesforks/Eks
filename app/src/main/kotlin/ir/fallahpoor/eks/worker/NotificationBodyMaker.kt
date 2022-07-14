package ir.fallahpoor.eks.worker

import android.content.Context
import androidx.annotation.StringRes
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.entity.Library
import javax.inject.Inject

class NotificationBodyMaker
@Inject constructor(
    private val context: Context,
    private val libraryDiffer: LibraryDiffer
) {

    fun makeBody(oldLibraries: List<Library>, refreshedLibraries: List<Library>): String? {

        val libraryNames = mutableListOf<String>()
        val librariesMap = libraryDiffer.diffLibraries(oldLibraries, refreshedLibraries)

        val newLibraries: List<Library>? = librariesMap[LibraryDiffer.Type.NEW]
        if (!newLibraries.isNullOrEmpty()) {
            libraryNames += context.getString(R.string.new_libraries)
            libraryNames += newLibraries.map { it.name }
        }

        val removedLibraries: List<Library>? = librariesMap[LibraryDiffer.Type.REMOVED]
        if (!removedLibraries.isNullOrEmpty()) {
            libraryNames += context.getString(R.string.removed_libraries)
            libraryNames += removedLibraries.map { it.name }
        }

        val updatedLibraries: List<Library>? = librariesMap[LibraryDiffer.Type.UPDATED]

        if (!updatedLibraries.isNullOrEmpty()) {

            libraryNames += context.getString(R.string.updated_libraries)

            updatedLibraries.forEachIndexed { index, library ->

                val oldLibrary = oldLibraries.first { it.name == library.name }
                val refreshedLibrary = refreshedLibraries.first { it.name == library.name }

                val updatedVersions = mutableListOf<String>()

                var updateText: String? = getOldVersionNewVersionText(
                    oldVersionName = oldLibrary.stableVersion.name,
                    newVersionName = refreshedLibrary.stableVersion.name,
                    textResId = R.string.version_stable
                )
                updateText?.let {
                    updatedVersions += it
                }

                updateText = getOldVersionNewVersionText(
                    oldVersionName = oldLibrary.rcVersion.name,
                    newVersionName = refreshedLibrary.rcVersion.name,
                    textResId = R.string.version_rc
                )
                updateText?.let {
                    updatedVersions + it
                }

                updateText = getOldVersionNewVersionText(
                    oldVersionName = oldLibrary.betaVersion.name,
                    newVersionName = refreshedLibrary.betaVersion.name,
                    textResId = R.string.version_beta
                )
                updateText?.let {
                    updatedVersions + it
                }

                updateText = getOldVersionNewVersionText(
                    oldVersionName = oldLibrary.alphaVersion.name,
                    newVersionName = refreshedLibrary.alphaVersion.name,
                    textResId = R.string.version_alpha
                )
                updateText?.let {
                    updatedVersions += it
                }

                libraryNames += updatedVersions.joinToString(
                    prefix = library.name + "\n",
                    separator = "\n",
                    postfix = if (index != updatedLibraries.lastIndex) "\n" else ""
                )
            }

        }

        return if (libraryNames.isNotEmpty()) {
            libraryNames.joinToString(separator = "\n")
        } else {
            null
        }

    }

    private fun getOldVersionNewVersionText(
        oldVersionName: String,
        newVersionName: String,
        @StringRes textResId: Int
    ): String? =
        if (newVersionName != Constants.NOT_AVAILABLE && newVersionName != oldVersionName) {
            context.getString(textResId, "$oldVersionName -> $newVersionName")
        } else {
            null
        }

}