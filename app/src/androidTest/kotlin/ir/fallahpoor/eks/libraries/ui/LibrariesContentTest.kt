package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LibrariesContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun when_state_is_LOADING_progress_indicator_is_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Loading)

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)

    }

    @Test
    fun when_state_is_LOADING_refresh_date_is_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Loading)

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.REFRESH_DATE)
    }

    @Test
    fun when_state_is_LOADING_libraries_are_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Loading)

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)

    }

    @Test
    fun when_state_is_LOADING_try_again_is_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Loading)

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.TRY_AGAIN_LAYOUT)

    }

    @Test
    fun when_state_is_SUCCESS_libraries_are_displayed() {

        // Given
        val libraries = listOf(
            TestData.Activity.old,
            TestData.Biometric.old,
            TestData.core, TestData.preference
        )
        composeLibrariesContent(librariesState = LibrariesState.Success(libraries))

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)

    }

    @Test
    fun when_state_is_SUCCESS_refresh_date_is_displayed() {

        // Given
        val libraries = listOf(
            TestData.Activity.old,
            TestData.Biometric.old,
            TestData.core,
            TestData.preference
        )
        composeLibrariesContent(librariesState = LibrariesState.Success(libraries))

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.REFRESH_DATE)

    }

    @Test
    fun when_state_is_SUCCESS_progress_indicator_is_not_displayed() {

        // Given
        val libraries = listOf(
            TestData.Activity.old,
            TestData.Biometric.old,
            TestData.core,
            TestData.preference
        )
        composeLibrariesContent(librariesState = LibrariesState.Success(libraries))

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)

    }

    @Test
    fun when_state_is_SUCCESS_try_again_is_not_displayed() {

        // Given
        val libraries = listOf(
            TestData.Activity.old,
            TestData.Biometric.old,
            TestData.core,
            TestData.preference
        )
        composeLibrariesContent(librariesState = LibrariesState.Success(libraries))

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.TRY_AGAIN_LAYOUT)

    }

    @Test
    fun when_state_is_ERROR_try_again_is_displayed() {

        // Given
        val errorMessage = "An error occurred."
        composeLibrariesContent(librariesState = LibrariesState.Error(errorMessage))

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibrariesContentTags.TRY_AGAIN_LAYOUT)
        composeRule.assertTextIsDisplayed(errorMessage)

    }

    @Test
    fun when_state_is_ERROR_libraries_are_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Error("An error occurred."))

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.LIBRARIES_LIST)

    }

    @Test
    fun when_state_is_ERROR_refresh_date_is_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Error("An error occurred."))

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.REFRESH_DATE)

    }

    @Test
    fun when_state_is_ERROR_progress_indicator_is_not_displayed() {

        // Given
        composeLibrariesContent(librariesState = LibrariesState.Error("An error occurred."))

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)

    }

    @Test
    fun when_a_library_is_clicked_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(listOf(library)),
            onLibraryClick = onLibraryClick
        )

        // When
        composeRule.clickOnNodeWithTag(LibraryItemTags.ITEM + library.name)

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun when_a_library_version_is_clicked_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(listOf(library)),
            onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        composeRule.clickOnNodeWithText(
            context.getString(R.string.version_stable, library.stableVersion.name)
        )

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.stableVersion)

    }

    @Test
    fun when_a_library_is_pinned_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(listOf(library)),
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.clickOnNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    @Test
    fun when_a_library_is_unpinned_correct_callback_is_called() {

        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Success(listOf(library)),
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.clickOnNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, false)

    }

    @Test
    fun when_try_again_button_is_clicked_correct_callback_is_called() {

        // Given
        val onTryAgainClick: () -> Unit = mock()
        composeLibrariesContent(
            librariesState = LibrariesState.Error("something went wrong"),
            onTryAgainClick = onTryAgainClick
        )

        // When
        composeRule.clickOnNodeWithTag(LibrariesContentTags.TRY_AGAIN_BUTTON)

        // Then
        Mockito.verify(onTryAgainClick).invoke()

    }

    private fun composeLibrariesContent(
        librariesState: LibrariesState = LibrariesState.Loading,
        refreshDate: String = "N/A",
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onLibraryPinClick: (Library, Boolean) -> Unit = { _, _ -> },
        onTryAgainClick: () -> Unit = {}
    ) {
        composeRule.setContent {
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

}