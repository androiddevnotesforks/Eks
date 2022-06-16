package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LibraryItemTest {

    private val library = Library(
        name = "Room",
        description = "Create, store, and manage persistent data backed by a SQLite database.",
        releaseDate = "September 21, 2021",
        url = "https://developer.android.com/jetpack/androidx/releases/room",
        stableVersion = Version("1.3.1"),
        rcVersion = Version(),
        betaVersion = Version("1.5.0-beta05"),
        alphaVersion = Version("1.6.0-alpha02")
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun library_information_is_displayed() {

        // Given
        composeLibraryItem(library = library)

        // Then
        with(composeTestRule) {
            onNodeWithTag(LibraryItemTags.NAME)
                .assertTextEquals(library.name)
            onNodeWithTag(LibraryItemTags.DESCRIPTION)
                .assertTextEquals(library.description)
            onNodeWithTag(LibraryItemTags.VERSION_STABLE + library.name)
                .assertTextEquals(library.stableVersion.name)
            onNodeWithTag(LibraryItemTags.VERSION_RC + library.name)
                .assertTextEquals(library.rcVersion.name)
            onNodeWithTag(LibraryItemTags.VERSION_BETA + library.name)
                .assertTextEquals(library.betaVersion.name)
            onNodeWithTag(LibraryItemTags.VERSION_ALPHA + library.name)
                .assertTextEquals(library.alphaVersion.name)
            onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
                .assertIsOff()
        }

    }

    @Test
    fun click_callback_is_called_when_library_is_clicked() {

        // Given
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryClick = onLibraryClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.ITEM + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun version_callback_is_called_when_library_version_is_clicked() {

        // Given
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.VERSION_BETA + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.betaVersion)

    }

    @Test
    fun version_callback_is_not_called_when_library_version_is_not_available_and_library_version_is_clicked() {

        // Given
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryVersionClick = onLibraryVersionClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.VERSION_RC + library.name)
            .performClick()

        // Then
        Mockito.verifyNoInteractions(onLibraryVersionClick)

    }

    @Test
    fun pin_callback_is_called_when_library_is_pinned() {

        // Given
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(library = library, onLibraryPinClick = onLibraryPinClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    @Test
    fun pin_callback_is_called_when_library_is_unpinned() {

        // Given
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibraryItem(
            library = library.copy(pinned = 1),
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, false)

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