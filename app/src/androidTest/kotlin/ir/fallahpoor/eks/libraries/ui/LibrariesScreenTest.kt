package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.TestData
import ir.fallahpoor.eks.data.ExceptionParser
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.mock
import ir.fallahpoor.eks.libraries.viewmodel.LibrariesViewModel
import ir.fallahpoor.eks.testfakes.FakeLibraryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class LibrariesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val searchText = context.getString(R.string.search)

    private lateinit var librariesViewModel: LibrariesViewModel
    private lateinit var fakeLibraryRepository: FakeLibraryRepository

    @Test
    fun screen_is_initialized_correctly() {

        // Given
        composeLibrariesScreen()

        // Then
        with(composeTestRule) {
            onNodeWithTag(LibrariesScreenTags.TOOLBAR)
                .assertIsDisplayed()
            onNodeWithTag(LibrariesScreenTags.CONTENT)
                .assertIsDisplayed()
        }

    }

    @Test
    fun search() = runTest {

        // Given
        composeLibrariesScreen()

        // When
        with(composeTestRule) {
            onNodeWithContentDescription(searchText, useUnmergedTree = true)
                .performClick()
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .performTextInput("co")
        }

        // Then
        fakeLibraryRepository.getLibraries().forEach {
            if (it.name.contains("co", ignoreCase = true)) {
                composeTestRule.onNodeWithTag(
                    LibraryItemTags.ITEM + it.name,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            } else {
                composeTestRule.onNodeWithTag(
                    LibraryItemTags.ITEM + it.name,
                    useUnmergedTree = true
                ).assertDoesNotExist()
            }
        }

    }

    @Test
    fun all_libraries_are_displayed_when_search_bar_is_closed() = runTest {

        // Given
        composeLibrariesScreen()
        with(composeTestRule) {
            onNodeWithContentDescription(searchText, useUnmergedTree = true)
                .performClick()
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .performTextInput("co")
        }

        // When
        composeTestRule.onNodeWithTag(SearchBarTags.CLOSE_BUTTON)
            .performClick()

        // Then
        with(composeTestRule) {
            fakeLibraryRepository.getLibraries().forEach {
                onNodeWithTag(
                    LibraryItemTags.ITEM + it.name,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
            onNodeWithTag(LibrariesListTags.NO_LIBRARY_TEXT)
                .assertDoesNotExist()
            onNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
                .assertDoesNotExist()
            onNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
                .assertDoesNotExist()
        }

    }

    @Test
    fun all_libraries_are_displayed_when_search_bar_is_cleared() = runTest {

        // Given
        composeLibrariesScreen()
        with(composeTestRule) {
            onNodeWithContentDescription(searchText, useUnmergedTree = true)
                .performClick()
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .performTextInput("co")
        }

        // When
        composeTestRule.onNodeWithTag(SearchBarTags.CLEAR_BUTTON)
            .performClick()

        // Then
        with(composeTestRule) {
            fakeLibraryRepository.getLibraries().forEach {
                onNodeWithTag(
                    LibraryItemTags.ITEM + it.name,
                    useUnmergedTree = true
                ).assertIsDisplayed()
            }
            onNodeWithText(LibrariesListTags.NO_LIBRARY_TEXT)
                .assertDoesNotExist()
            onNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
                .assertDoesNotExist()
        }

    }

    @Test
    fun correct_callback_is_called_when_a_library_is_clicked() = runTest {

        // Given
        val onLibraryClick: (Library) -> Unit = mock()
        composeLibrariesScreen(onLibraryClick = onLibraryClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.ITEM + TestData.preference.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryClick).invoke(TestData.preference)

    }

    @Test
    fun correct_callback_is_called_when_a_library_version_is_clicked() {

        // Given
        val onLibraryVersionClick: (Version) -> Unit = mock()
        composeLibrariesScreen(onLibraryVersionClick = onLibraryVersionClick)

        // When
        composeTestRule.onNodeWithTag(LibraryItemTags.VERSION_BETA + TestData.core.name)
            .performClick()

        // Then
        Mockito.verify(onLibraryVersionClick).invoke(TestData.core.betaVersion)

    }

    private fun composeLibrariesScreen(
        onLibraryClick: (Library) -> Unit = {},
        onLibraryVersionClick: (Version) -> Unit = {}
    ) {
        fakeLibraryRepository = FakeLibraryRepository()
        librariesViewModel = LibrariesViewModel(
            libraryRepository = fakeLibraryRepository,
            exceptionParser = ExceptionParser(context)
        )
        composeTestRule.setContent {
            LibrariesScreen(
                librariesViewModel = librariesViewModel,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick
            )
        }
    }

}