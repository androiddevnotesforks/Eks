package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.TestData
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LibraryItemTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun library_information_is_displayed_correctly() {

        // Given
        val library: Library = TestData.core
        composeLibraryItem(library = library)

        // Then
        with(composeTestRule) {
            onNodeWithText(library.name, useUnmergedTree = true)
                .assertIsDisplayed()
            onNodeWithText(library.description, useUnmergedTree = true)
                .assertIsDisplayed()
            onNodeWithText(
                context.getString(R.string.version_stable, library.stableVersion.name),
                useUnmergedTree = true
            )
                .assertIsDisplayed()
            onNodeWithText(
                context.getString(R.string.version_rc, library.rcVersion.name),
                useUnmergedTree = true
            )
                .assertIsDisplayed()
            onNodeWithText(
                context.getString(R.string.version_beta, library.betaVersion.name),
                useUnmergedTree = true
            )
                .assertIsDisplayed()
            onNodeWithText(
                context.getString(R.string.version_alpha, library.alphaVersion.name),
                useUnmergedTree = true
            )
                .assertIsDisplayed()
            onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
                .assertIsOn()
        }

    }

    @Test
    fun click_callback_is_called_when_library_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryClick = onLibraryClick)

        // When
        composeTestRule.onNodeWithText(library.name, useUnmergedTree = true)
            .performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun version_callback_is_called_when_library_version_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        composeTestRule.onNodeWithText(
            context.getString(R.string.version_stable, library.stableVersion.name),
            useUnmergedTree = true
        )
            .performClick()

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.stableVersion)

    }

    @Test
    fun version_callback_is_not_called_when_library_version_is_not_available_and_library_version_is_clicked() {

        // Given
        val library: Library = TestData.room

        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        composeTestRule.onNodeWithText(
            context.getString(R.string.version_rc, library.rcVersion.name),
            useUnmergedTree = true
        )
            .performClick()

        // Then
        Mockito.verifyNoInteractions(onLibraryVersionClick)

    }

    @Test
    fun pin_callback_is_called_when_library_is_unpinned() {

        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryPinClick = onLibraryPinClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, false)

    }

    @Test
    fun pin_callback_is_called_when_library_is_pinned() {

        // Given
        val library: Library = TestData.room
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(
            library = library,
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    private fun composeLibraryItem(
        library: Library,
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onLibraryPinClick: (Library, Boolean) -> Unit = { _, _ -> }
    ) {
        composeTestRule.setContent {
            LibraryItem(
                library = library,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = onLibraryPinClick
            )
        }
    }

}