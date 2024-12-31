package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import io.mockk.verify
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.libraries.ui.robots.LibrariesListRobot
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test

class LibrariesListTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val librariesListRobot = LibrariesListRobot(context, composeRule)

    @Test
    fun when_there_are_libraries_the_list_of_libraries_is_displayed() {
        // Given
        librariesListRobot.composeLibrariesList(libraries = getLibraries())

        // Then
        with(composeRule) {
            assertIsDisplayedNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            assertTextDoesNotExist(context.getString(R.string.no_library))
        }
    }

    private fun getLibraries(): ImmutableList<Library> {
        val libraries: List<Library> = buildList {
            repeat(30) {
                this += TestData.core.copy(name = "core$it")
            }
        }
        return libraries.toImmutableList()
    }

    @Test
    fun when_there_are_libraries_no_libraries_message_is_not_displayed() {
        // Given
        librariesListRobot.composeLibrariesList(libraries = getLibraries())

        // Then
        composeRule.assertTextDoesNotExist(context.getString(R.string.no_library))
    }

    @Test
    fun when_at_the_top_of_list_of_libraries_scroll_to_top_button_is_not_displayed() {
        // Given
        val libraries = getLibraries()
        librariesListRobot.composeLibrariesList(libraries = libraries).scrollToBottom(libraries)

        // When
        librariesListRobot.scrollToTop()

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)
    }

    @Test
    fun when_not_at_the_top_of_the_list_of_libraries_scroll_to_top_button_is_displayed() {
        // Given
        val libraries = getLibraries()
        librariesListRobot.composeLibrariesList(libraries = libraries)

        // When
        librariesListRobot.scrollToBottom(libraries)

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)
    }

    @Test
    fun when_clicking_the_scroll_to_top_button_the_list_of_libraries_is_scrolled_to_the_top() {
        // Given
        val libraries = getLibraries().toMutableList()
        libraries.add(0, TestData.room)
        librariesListRobot.composeLibrariesList(libraries = libraries.toImmutableList())
            .scrollToBottom(libraries.toImmutableList())

        // When
        librariesListRobot.clickOnScrollToTopButton()

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibraryItemTags.ITEM + TestData.room.name)
    }

    @Test
    fun when_there_are_no_libraries_no_libraries_message_is_displayed() {
        // Given
        librariesListRobot.composeLibrariesList(libraries = persistentListOf())

        // Then
        composeRule.assertTextIsDisplayed(context.getString(R.string.no_library))
    }

    @Test
    fun when_there_are_no_libraries_list_of_libraries_is_not_displayed() {
        // Given
        librariesListRobot.composeLibrariesList(libraries = persistentListOf())

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
    }

    @Test
    fun when_there_are_no_libraries_scroll_to_top_button_is_not_displayed() {
        // Given
        librariesListRobot.composeLibrariesList(libraries = persistentListOf())

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)
    }

    @Test
    fun when_a_library_is_clicked_correct_callback_is_called() {
        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mockk()
        librariesListRobot.composeLibrariesList(
            libraries = persistentListOf(library), onLibraryClick = onLibraryClick
        )

        // When
        librariesListRobot.clickOnLibrary(library)

        // Then
        verify { onLibraryClick.invoke(library) }
    }

    @Test
    fun when_a_library_version_is_clicked_correct_callback_is_called() {
        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mockk()
        librariesListRobot.composeLibrariesList(
            libraries = persistentListOf(library), onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        librariesListRobot.clickOnLibraryStableVersion(library)

        // Then
        verify { onLibraryVersionClick.invoke(library.stableVersion) }
    }

    @Test
    fun when_a_library_is_pinned_correct_callback_is_called() {
        // Given
        val library: Library = TestData.preference
        val onLibraryPinClick: (Library, Boolean) -> Unit = mockk()
        librariesListRobot.composeLibrariesList(
            libraries = persistentListOf(library), onLibraryPinClick = onLibraryPinClick
        )

        // When
        librariesListRobot.clickOnPin(library)

        // Then
        verify { onLibraryPinClick.invoke(library, true) }
    }

    @Test
    fun when_a_library_is_unpinned_correct_callback_is_called() {
        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mockk()
        librariesListRobot.composeLibrariesList(
            libraries = persistentListOf(library), onLibraryPinClick = onLibraryPinClick
        )

        // When
        librariesListRobot.clickOnPin(library)

        // Then
        verify { onLibraryPinClick.invoke(library, false) }
    }
}