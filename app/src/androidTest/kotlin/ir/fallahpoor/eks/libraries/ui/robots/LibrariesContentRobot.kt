package ir.fallahpoor.eks.libraries.ui.robots

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.libraries.ui.LibrariesContent
import ir.fallahpoor.eks.libraries.ui.LibrariesContentTags
import ir.fallahpoor.eks.libraries.ui.LibrariesState
import ir.fallahpoor.eks.libraries.ui.LibraryItemTags
import ir.fallahpoor.eks.libraries.ui.assertDoesNotExistNodeWithTag
import ir.fallahpoor.eks.libraries.ui.assertIsDisplayedNodeWithTag
import ir.fallahpoor.eks.libraries.ui.assertTextIsDisplayed
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithTag
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithText

class LibrariesContentRobot(
    private val context: Context,
    private val composeTestRule: ComposeContentTestRule
) {

    fun composeLibrariesContent(
        librariesState: LibrariesState = LibrariesState.Loading,
        refreshDate: String = "N/A",
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onLibraryPinClick: (Library, Boolean) -> Unit = { _, _ -> },
        onTryAgainClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            LibrariesContent(
                librariesState = librariesState,
                refreshDate = refreshDate,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = onLibraryPinClick,
                onTryAgainClick = onTryAgainClick
            )
        }
    }

    fun clickOnLibrary(library: Library) {
        composeTestRule.clickOnNodeWithTag(LibraryItemTags.ITEM + library.name)
    }

    fun clickOnLibraryStableVersion(library: Library) {
        composeTestRule.clickOnNodeWithText(
            context.getString(R.string.version_stable, library.stableVersion.name)
        )
    }

    fun clickOnPin(library: Library) {
        composeTestRule.clickOnNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
    }

    fun clickOnTryAgain() {
        composeTestRule.clickOnNodeWithTag(LibrariesContentTags.TRY_AGAIN_BUTTON)
    }

    fun progressIndicatorIsDisplayed() {
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
    }

    fun progressIndicatorIsNotDisplayed() {
        composeTestRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
    }

    fun refreshDateIsDisplayed() {
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.REFRESH_DATE)
    }

    fun refreshDateIsNotDisplayed() {
        composeTestRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.REFRESH_DATE)
    }

    fun librariesListIsDisplayed() {
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)
    }

    fun librariesListIsNotDisplayed() {
        composeTestRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)
    }

    fun tryAgainIsDisplayed() {
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.TRY_AGAIN_LAYOUT)
    }

    fun tryAgainIsNotDisplayed() {
        composeTestRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.TRY_AGAIN_LAYOUT)
    }

    fun errorMessageIsDisplayed(errorMessage: String) {
        composeTestRule.assertTextIsDisplayed(errorMessage)
    }

}