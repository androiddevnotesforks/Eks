package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
            assertIsDisplayedNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
            assertTextDoesNotExist(context.getString(R.string.no_library))
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
            assertTextDoesNotExist(context.getString(R.string.no_library))
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
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)

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
        composeRule.assertIsDisplayedNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)

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
        composeRule.clickOnNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)

        // Then
        composeRule.assertIsDisplayedNodeWithTag(LibraryItemTags.ITEM + TestData.room.name)

    }

    @Test
    fun when_there_are_no_libraries_no_libraries_message_is_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.assertTextIsDisplayed(context.getString(R.string.no_library))

    }

    @Test
    fun when_there_are_no_libraries_list_of_libraries_is_not_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.LIBRARIES_LIST)

    }

    @Test
    fun when_there_are_no_libraries_scroll_to_top_button_is_not_displayed() {

        // Given
        composeLibrariesList(libraries = emptyList())

        // Then
        composeRule.assertDoesNotExistNodeWithTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON)

    }

    @Test
    fun when_a_library_is_clicked_correct_callback_is_called() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryClick = onLibraryClick
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
        composeLibrariesList(
            libraries = listOf(library), onLibraryVersionClick = onLibraryVersionClick
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
        val library: Library = TestData.preference
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library), onLibraryPinClick = onLibraryPinClick
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
        composeLibrariesList(
            libraries = listOf(library), onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.clickOnNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)

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