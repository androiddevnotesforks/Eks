package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.libraries.ui.robots.LibraryItemRobot
import org.junit.Rule
import org.junit.Test

class LibraryItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val libraryItemRobot = LibraryItemRobot(context, composeTestRule)

    @Test
    fun library_information_is_displayed_correctly() {
        // Given
        libraryItemRobot.composeLibraryItem(library = TestData.core)

        // Then
        libraryItemRobot.assertAllInformationAreDisplayedCorrectly()
    }

    @Test
    fun when_a_library_is_clicked_correct_callback_is_called() {
        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mockk()
        libraryItemRobot.composeLibraryItem(library = library, onLibraryClick = onLibraryClick)

        // When
        libraryItemRobot.clickOnLibrary()

        // Then
        verify { onLibraryClick.invoke(library) }
    }

    @Test
    fun when_a_library_version_is_clicked_correct_callback_is_called() {
        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mockk()
        libraryItemRobot.composeLibraryItem(
            library = library,
            onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        libraryItemRobot.clickOnStableVersion()

        // Then
        verify { onLibraryVersionClick.invoke(library.stableVersion) }
    }

    @Test
    fun when_a_library_version_is_clicked_and_library_version_is_not_available_no_callback_is_called() {
        // Given
        val onLibraryVersionClick: (Version) -> Unit = mockk()
        libraryItemRobot.composeLibraryItem(
            library = TestData.room,
            onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        libraryItemRobot.clickOnRcVersion()

        // Then
        verify { onLibraryVersionClick wasNot Called }
    }

    @Test
    fun when_a_library_is_unpinned_correct_callback_is_called() {
        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mockk()
        libraryItemRobot.composeLibraryItem(
            library = library,
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        libraryItemRobot.clickOnPin()

        // Then
        verify { onLibraryPinClick.invoke(library, false) }
    }

    @Test
    fun when_a_library_is_pinned_correct_callback_is_called() {
        // Given
        val library: Library = TestData.room
        val onLibraryPinClick: (Library, Boolean) -> Unit = mockk()
        libraryItemRobot.composeLibraryItem(
            library = library,
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        libraryItemRobot.clickOnPin()

        // Then
        verify { onLibraryPinClick.invoke(library, true) }
    }
}