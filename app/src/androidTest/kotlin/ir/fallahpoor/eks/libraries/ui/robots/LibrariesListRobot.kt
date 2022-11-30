package ir.fallahpoor.eks.libraries.ui.robots

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.model.Library
import ir.fallahpoor.eks.data.model.Version
import ir.fallahpoor.eks.libraries.ui.LibrariesList
import ir.fallahpoor.eks.libraries.ui.LibrariesListTags
import ir.fallahpoor.eks.libraries.ui.LibraryItemTags
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithTag
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithText

class LibrariesListRobot(
    private val context: Context,
    private val composeTestRule: ComposeContentTestRule
) {

    fun composeLibrariesList(
        modifier: Modifier = Modifier,
        libraries: List<Library>,
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onLibraryPinClick: (Library, Boolean) -> Unit = { _, _ -> }
    ): LibrariesListRobot {
        composeTestRule.setContent {
            LibrariesList(
                modifier = modifier,
                libraries = libraries,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = onLibraryPinClick
            )
        }
        return this
    }

    fun scrollToTop() {
        composeTestRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST).performScrollToIndex(0)
    }

    fun scrollToBottom(libraries: List<Library>) {
        composeTestRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            .performScrollToIndex(libraries.lastIndex)
    }

    fun clickOnScrollToTopButton() {
        composeTestRule.clickOnNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)
    }

    fun clickOnLibrary(library: Library) {
        composeTestRule.clickOnNodeWithTag(LibraryItemTags.ITEM + library.name)
    }

    fun clickOnPin(library: Library) {
        composeTestRule.clickOnNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
    }

    fun clickOnLibraryStableVersion(library: Library) {
        composeTestRule.clickOnNodeWithText(
            context.getString(R.string.version_stable, library.stableVersion.name)
        )
    }

}