package ir.fallahpoor.eks.libraries.ui.robots

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performTextInput
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.data.repository.storage.StorageRepository
import ir.fallahpoor.eks.libraries.ui.LibrariesListTags
import ir.fallahpoor.eks.libraries.ui.LibrariesScreen
import ir.fallahpoor.eks.libraries.ui.LibraryItemTags
import ir.fallahpoor.eks.libraries.ui.SearchBarTags
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithContentDescription
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithTag
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithText
import ir.fallahpoor.eks.libraries.viewmodel.LibrariesViewModel
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.ExceptionParserImpl

class LibrariesScreenRobot(
    private val context: Context,
    private val composeTestRule: ComposeContentTestRule
) {

    fun composeLibrariesScreen(
        libraryRepository: LibraryRepository,
        storageRepository: StorageRepository,
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {}
    ): LibrariesScreenRobot {
        val librariesViewModel = LibrariesViewModel(
            libraryRepository = libraryRepository,
            storageRepository = storageRepository,
            exceptionParser = ExceptionParserImpl(context)
        )
        composeTestRule.setContent {
            LibrariesScreen(
                librariesViewModel = librariesViewModel,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick
            )
        }
        return this
    }

    fun enterSearchQuery(searchQuery: String) {
        with(composeTestRule) {
            clickOnNodeWithContentDescription(context.getString(R.string.search))
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .performTextInput(searchQuery)
        }
    }

    fun closeSearchBar() {
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLOSE_BUTTON)
    }

    fun clearSearchBar() {
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLEAR_BUTTON)
    }

    fun scrollToLibrary(library: Library) {
        composeTestRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            .performScrollToKey(library.name)
    }

    fun clickOnLibrary(library: Library) {
        composeTestRule.clickOnNodeWithTag(LibraryItemTags.ITEM + library.name)
    }

    fun clickOnLibraryBetaVersion(library: Library) {
        composeTestRule.clickOnNodeWithText(
            context.getString(R.string.version_beta, library.betaVersion.name)
        )
    }

}