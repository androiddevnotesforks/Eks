package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun list_of_libraries_is_displayed() {

        // Given
        val libraries = listOf(
            TestData.core,
            TestData.room,
            TestData.preference
        )

        // When
        composeLibrariesList(libraries = libraries)

        // Then
        with(composeRule) {
            libraries.forEach {
                onNodeWithTag(
                    LibraryItemTags.ITEM + it.name,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
            onNodeWithText(context.getString(R.string.no_library), useUnmergedTree = true)
                .assertDoesNotExist()
        }

    }

    @Test
    fun a_proper_message_is_displayed_when_there_is_no_library() {

        // When
        val testTag = "libraries"
        composeLibrariesList(modifier = Modifier.testTag(testTag), libraries = emptyList())

        // Then
        with(composeRule) {
            onNodeWithTag(testTag)
                .onChildren()
                .assertCountEquals(1)
            onNodeWithText(context.getString(R.string.no_library), useUnmergedTree = true)
                .assertIsDisplayed()
        }

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_clicked() {

        // Given
        val library: Library = TestData.room
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library),
            onLibraryClick = onLibraryClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.ITEM + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(library)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_pinned() {

        // Given
        val library: Library = TestData.preference
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library),
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryPinClick).invoke(library, true)

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_unpinned() {

        // Given
        val library: Library = TestData.core
        val onLibraryPinClick: (Library, Boolean) -> Unit = mock()
        composeLibrariesList(
            libraries = listOf(library),
            onLibraryPinClick = onLibraryPinClick
        )

        // When
        composeRule.onNodeWithTag(LibraryItemTags.PIN_BUTTON + library.name)
            .performClick()

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