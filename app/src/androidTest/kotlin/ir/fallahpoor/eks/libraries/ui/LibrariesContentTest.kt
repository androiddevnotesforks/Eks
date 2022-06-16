package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ir.fallahpoor.eks.TestData
import ir.fallahpoor.eks.TestData.libraries
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LibrariesContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun test_loading_state() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Loading)

        // Then
        with(composeRule) {
            composeRule.onNodeWithTag(LibrariesContentTags.LAST_UPDATE_CHECK_DATE)
                .assertIsDisplayed()
            onNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
                .assertIsDisplayed()
            onNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)
                .assertDoesNotExist()
        }

    }

    @Test
    fun list_of_libraries_is_displayed() {

        // Given
        composeLibrariesContent(
            librariesState = LibrariesState.Success(
                libraries
            )
        )

        // Then
        with(composeRule) {
            onNodeWithTag(LibrariesContentTags.LAST_UPDATE_CHECK_DATE)
                .assertIsDisplayed()
            onNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)
                .assertIsDisplayed()
            onNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
                .assertDoesNotExist()
        }

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(libraries),
            onLibraryClick = onLibraryClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.ITEM + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun correct_callback_is_called_when_a_library_version_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(libraries),
            onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.VERSION_STABLE + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.stableVersion)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_pinned() {

        // Given
        val onPinLibrary: (Library, Boolean) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(libraries),
            onPinLibraryClick = onPinLibrary
        )

        // When
        composeRule.onNodeWithTag(
            LibraryItemTags.PIN_BUTTON + TestData.preference.name
        ).performClick()

        // Then
        Mockito.verify(onPinLibrary).invoke(TestData.preference, true)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_unpinned() {

        // Given
        val onPinLibrary: (Library, Boolean) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(libraries),
            onPinLibraryClick = onPinLibrary
        )

        // When
        composeRule.onNodeWithTag(
            LibraryItemTags.PIN_BUTTON + TestData.core.name
        ).performClick()

        // Then
        Mockito.verify(onPinLibrary).invoke(TestData.core, false)

    }

    @Test
    fun correct_callback_is_called_when_try_again_is_clicked() {

        // Given
        val onTryAgainClick: () -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Error("something went wrong"),
            onTryAgainClick = onTryAgainClick
        )

        // When
        composeRule.onNodeWithTag(LibrariesContentTags.TRY_AGAIN)
            .performClick()

        // Then
        Mockito.verify(onTryAgainClick).invoke()

    }

    private fun composeLibrariesContent(
        librariesState: LibrariesState = LibrariesState.Loading,
        refreshDate: String = "N/A",
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onPinLibraryClick: (Library, Boolean) -> Unit = { _, _ -> },
        onTryAgainClick: () -> Unit = {}
    ) {
        composeRule.setContent {
            LibrariesContent(
                librariesState = librariesState,
                refreshDate = refreshDate,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = onPinLibraryClick,
                onTryAgainClick = onTryAgainClick
            )
        }
    }

}