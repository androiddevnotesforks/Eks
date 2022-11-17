package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class LibrariesListTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun when_there_are_libraries_the_list_of_libraries_is_displayed() {

        // Given
        composeLibrariesList(libraries = getListOfLibraries())

        // Then
        with(composeRule) {
            onNodeWithTag(
                LibrariesListTags.LIBRARIES_LIST,
                useUnmergedTree = true
            ).assertIsDisplayed()
            onNodeWithText(
                context.getString(R.string.no_library),
                useUnmergedTree = true
            ).assertDoesNotExist()
        }

    }

    private fun getListOfLibraries(): List<Library> = mutableListOf<Library>().apply {
        repeat(30) {
            this += TestData.core.copy(name = "core$it")
        }
    }

    @Test
    fun when_there_are_libraries_no_libraries_message_is_not_displayed() {

        // Given
        composeLibrariesList(libraries = getListOfLibraries())

        // Then
        with(composeRule) {
            onNodeWithText(
                context.getString(R.string.no_library),
                useUnmergedTree = true
            ).assertDoesNotExist()
        }

    }

    @Test
    fun when_at_the_top_of_list_of_libraries_scroll_to_top_button_is_not_displayed() {

        // Given
        val libraries = getListOfLibraries()
        composeLibrariesList(libraries = libraries)
        composeRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            .performScrollToIndex(libraries.lastIndex)

        // When
        composeRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST).performScrollToIndex(0)

        // Then
        composeRule.onNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON, useUnmergedTree = true)
            .assertDoesNotExist()

    }

    @Test
    fun when_not_at_the_top_of_the_list_of_libraries_scroll_to_top_button_is_displayed() {

        // Given
        val libraries = getListOfLibraries()
        composeLibrariesList(libraries = libraries)

        // When
        composeRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            .performScrollToIndex(libraries.lastIndex)

        // Then
        composeRule.onNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON, useUnmergedTree = true)
            .assertIsDisplayed()

    }

    @Test
    fun when_clicking_the_scroll_to_top_button_the_list_of_libraries_is_scrolled_to_the_top() {

        // Given
        val libraries = getListOfLibraries().toMutableList()
        libraries.add(0, TestData.room)
        composeLibrariesList(libraries = libraries)
        composeRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            .performScrollToIndex(libraries.lastIndex)

        // When
        composeRule.onNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON).performClick()

        // Then
        composeRule.onNodeWithTag(LibraryItemTags.ITEM + TestData.room.name, useUnmergedTree = true)
            .assertIsDisplayed()

    }

    @Test
    fun when_there_are_no_libraries_no_libraries_message_is_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.onNodeWithText(context.getString(R.string.no_library), useUnmergedTree = true)
            .assertIsDisplayed()

    }

    @Test
    fun when_there_are_no_libraries_list_of_libraries_is_not_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.onNodeWithTag(LibrariesListTags.LIBRARIES_LIST, useUnmergedTree = true)
            .assertDoesNotExist()

    }

    @Test
    fun when_there_are_no_libraries_scroll_to_top_button_is_not_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.onNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON, useUnmergedTree = true)
            .assertDoesNotExist()

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryClick = onLibraryClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.ITEM + library.name).performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun correct_callback_is_called_when_a_library_version_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryVersionClick = onLibraryVersionClick
        )

        // When
        composeRule.onNodeWithText(
            context.getString(R.string.version_stable, library.stableVersion.name),
            useUnmergedTree = true
        ).performClick()

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(library.stableVersion)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_pinned() {

        // Given
        val library: Library = TestData.preference
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name).performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_unpinned() {

        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name).performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, false)

    }

    private fun composeLibrariesList(
        modifier: Modifier = Modifier,
        libraries: List<Library>,
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {},
        onLibraryPinClick: (Library, Boolean) -> Unit = { _, _ -> }
    ) {
        composeRule.setContent {
            LibrariesList(
                modifier = modifier,
                libraries = libraries,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = onLibraryPinClick
            )
        }
    }

}