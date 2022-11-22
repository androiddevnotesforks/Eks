package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
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
        assertTextIsDisplayed(library.name)
        assertTextIsDisplayed(library.description)
        assertTextIsDisplayed(
            context.getString(
                R.string.version_stable,
                library.stableVersion.name
            )
        )
        assertTextIsDisplayed(
            context.getString(R.string.version_rc, library.rcVersion.name)
        )
        assertTextIsDisplayed(
            context.getString(R.string.version_beta, library.betaVersion.name)
        )
        assertTextIsDisplayed(
            context.getString(R.string.version_alpha, library.alphaVersion.name)
        )
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .assertIsOn()

    }

    private fun assertTextIsDisplayed(text: String) {
        composeTestRule.onNodeWithText(text, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun when_a_library_is_clicked_correct_callback_is_called_() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryClick = onLibraryClick)

        // When
        clickOnElementWithText(library.name)

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun when_a_library_version_is_clicked_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        clickOnElementWithText(
            context.getString(
                R.string.version_stable,
                library.stableVersion.name
            )
        )

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.stableVersion)

    }

    @Test
    fun when_a_library_version_is_clicked_and_library_version_is_not_available_no_callback_is_called() {

        // Given
        val library: Library = TestData.room

        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        clickOnElementWithText(context.getString(R.string.version_rc, library.rcVersion.name))

        // Then
        Mockito.verifyNoInteractions(onLibraryVersionClick)

    }

    private fun clickOnElementWithText(text: String) {
        composeTestRule.onNodeWithText(text, useUnmergedTree = true)
            .performClick()
    }

    @Test
    fun when_a_library_is_unpinned_correct_callback_is_called() {

        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryPinClick = onLibraryPinClick)

        // When
        clickOnPinButtonForLibraryWithName(library.name)

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, false)

    }

    @Test
    fun when_a_library_is_pinned_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(
            library = library,
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        clickOnPinButtonForLibraryWithName(library.name)

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    private fun clickOnPinButtonForLibraryWithName(libraryName: String) {
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + libraryName)
            .performClick()
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