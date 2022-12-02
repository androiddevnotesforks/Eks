package ir.fallahpoor.eks.data.network

import ir.fallahpoor.eks.data.Constants.NOT_AVAILABLE
import ir.fallahpoor.eks.data.database.entity.LibraryEntity
import ir.fallahpoor.eks.data.database.entity.VersionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import javax.inject.Inject

internal class LibrariesFetcher @Inject constructor() {

    private companion object {
        const val TABLE_COLUMN_COUNT = 6
        const val LIBRARY_BASE_URL = "https://developer.android.com"
    }

    private object TableColumnIndex {
        const val INFO = 0
        const val RELEASE_DATE = 1
        const val STABLE_VERSION = 2
        const val RELEASE_CANDIDATE_VERSION = 3
        const val BETA_VERSION = 4
        const val ALPHA_VERSION = 5
    }

    private object HtmlTags {

        const val TR = "tr"
        const val TD = "td"

        object A {
            const val NAME = "a"
            const val ATTR_HREF = "href"
            const val ATTR_DATA_TITLE = "data-title"
        }

    }

    suspend fun fetchLibraries(): List<LibraryEntity> = withContext(Dispatchers.IO) {
        val url = "https://developer.android.com/jetpack/androidx/versions"
        val document: Document = Jsoup.connect(url).get()
        fetchLibraries(document)
    }

    private fun fetchLibraries(document: Document): List<LibraryEntity> {

        val tableRows: Elements = document.getElementsByTag(HtmlTags.TR)

        val libraries: List<LibraryEntity> =
            tableRows.mapNotNull { element: Element ->
                val rowData: Elements = element.getElementsByTag(HtmlTags.TD)
                if (rowData.size == TABLE_COLUMN_COUNT) {
                    extractLibraryInfo(rowData)
                } else {
                    null
                }
            }

        return libraries

    }

    private fun extractLibraryInfo(rowData: Elements): LibraryEntity {

        val generalInfoCell = rowData[TableColumnIndex.INFO]

        val name = getLibraryName(generalInfoCell)
        val description = getLibraryDescription(generalInfoCell)
        val url = getLibraryUrl(generalInfoCell)

        val releaseDateCell = rowData[TableColumnIndex.RELEASE_DATE]
        val releaseDate = getLibraryReleaseDate(releaseDateCell)

        val stableVersionCell = rowData[TableColumnIndex.STABLE_VERSION]
        val stableVersion = getLibraryVersion(stableVersionCell)

        val releaseCandidateVersionCell = rowData[TableColumnIndex.RELEASE_CANDIDATE_VERSION]
        val rcVersion = getLibraryVersion(releaseCandidateVersionCell)

        val betaVersionCell = rowData[TableColumnIndex.BETA_VERSION]
        val betaVersion = getLibraryVersion(betaVersionCell)

        val alphaVersionCell = rowData[TableColumnIndex.ALPHA_VERSION]
        val alphaVersion = getLibraryVersion(alphaVersionCell)

        return LibraryEntity(
            name = name,
            description = description,
            url = url,
            releaseDate = releaseDate,
            stableVersion = stableVersion,
            rcVersion = rcVersion,
            betaVersion = betaVersion,
            alphaVersion = alphaVersion
        )

    }

    private fun getLibraryName(tdTag: Element): String {
        return if (tdTag.tagName() != HtmlTags.TD) {
            NOT_AVAILABLE
        } else {
            val aTag = tdTag.getElementsByTag(HtmlTags.A.NAME)
            return if (aTag.isEmpty()) {
                NOT_AVAILABLE
            } else {
                // Some libraries may have a suffix of '(*)' in their name. So we remove it from
                // the library name
                aTag.text().removeSuffix(" (*)")
            }
        }
    }

    private fun getLibraryDescription(tdTag: Element): String {

        if (tdTag.tagName() != HtmlTags.TD) {
            return NOT_AVAILABLE
        }

        val aTag = tdTag.getElementsByTag(HtmlTags.A.NAME)
        if (aTag.isEmpty()) {
            return NOT_AVAILABLE
        }

        val libraryDescription = aTag.attr(HtmlTags.A.ATTR_DATA_TITLE)
        return libraryDescription.ifBlank {
            NOT_AVAILABLE
        }

    }

    private fun getLibraryUrl(tdTag: Element): String {

        if (tdTag.tagName() != HtmlTags.TD) {
            return NOT_AVAILABLE
        }

        val aTag = tdTag.getElementsByTag(HtmlTags.A.NAME)
        if (aTag.isEmpty()) {
            return NOT_AVAILABLE
        }

        val libraryUrlSuffix = aTag.attr(HtmlTags.A.ATTR_HREF)
        return if (libraryUrlSuffix.isNotBlank()) {
            "$LIBRARY_BASE_URL$libraryUrlSuffix"
        } else {
            NOT_AVAILABLE
        }

    }

    private fun getLibraryReleaseDate(tdTag: Element): String {
        return if (tdTag.tagName() == HtmlTags.TD) {
            tdTag.text()
        } else {
            NOT_AVAILABLE
        }
    }

    private fun getLibraryVersion(tdTag: Element): VersionEntity {

        if (tdTag.tagName() != HtmlTags.TD || tdTag.text() == "-") {
            return VersionEntity()
        }

        val aTag = tdTag.getElementsByTag(HtmlTags.A.NAME)
        if (aTag.isEmpty()) {
            return VersionEntity()
        }

        val releaseNotesUrlSuffix = aTag.attr(HtmlTags.A.ATTR_HREF)
        val releaseNotesUrl = if (releaseNotesUrlSuffix.isNotBlank()) {
            "$LIBRARY_BASE_URL$releaseNotesUrlSuffix"
        } else {
            NOT_AVAILABLE
        }
        val version = aTag.text()

        return VersionEntity(version, releaseNotesUrl)

    }

}