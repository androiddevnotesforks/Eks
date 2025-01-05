package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToKey
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import ir.fallahpoor.eks.commontest.FakeLibraryRepository
import ir.fallahpoor.eks.commontest.FakeStorageRepository
import ir.fallahpoor.eks.commontest.TestData
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.libraries.ui.robots.LibrariesScreenRobot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// TODO Add tests for checking the functionality of sort order

class LibrariesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val librariesScreenRobot = LibrariesScreenRobot(
        context = context,
        composeTestRule = composeTestRule
    )

    private lateinit var libraryRepository: FakeLibraryRepository
    private lateinit var storageRepository: FakeStorageRepository

    @Before
    fun runBeforeEachTest() {
        libraryRepository = FakeLibraryRepository()
        storageRepository = FakeStorageRepository()
    }

    @Test
    fun screen_is_initialized_correctly() {
        // Given
        librariesScreenRobot.composeLibrariesScreen(libraryRepository, storageRepository)

        // Then
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesScreenTags.TOOLBAR)
        composeTestRule.assertIsDisplayedNodeWithTag(LibrariesScreenTags.CONTENT)
    }

//    @Test
//    fun libraries_are_sorted_based_on_selected_sort_order() {
//
//        // Given
//        composeLibrariesScreen()
//
//        // When
//        composeTestRule.onNodeWithText(context.getString(SortOrder.Z_TO_A.stringResId), useUnmergedTree = true)
//            .performClick()
//
//        // Then
//
//    }

    @Test
    fun search() = runTest {
        // Given
        librariesScreenRobot.composeLibrariesScreen(libraryRepository, storageRepository)

        // When
        librariesScreenRobot.enterSearchQuery("co")

        // Then
        with(composeTestRule) {
            libraryRepository.getLibraries().forEach {
                if (it.name.contains("co", ignoreCase = true)) {
                    onNodeWithTag(LibrariesListTags.LIBRARIES_LIST)
                        .performScrollToKey(it.name)
                    assertIsDisplayedNodeWithTag(LibraryItemTags.ITEM + it.name)
                } else {
                    assertDoesNotExistNodeWithTag(LibraryItemTags.ITEM + it.name)
                }
            }
        }
    }

    @Test
    fun all_libraries_are_displayed_when_search_bar_is_closed() = runTest {
        // Given
        librariesScreenRobot.composeLibrariesScreen(libraryRepository, storageRepository)
            .enterSearchQuery("co")

        // When
        librariesScreenRobot.closeSearchBar()

        // Then
        with(composeTestRule) {
            libraryRepository.getLibraries().forEach {
                librariesScreenRobot.scrollToLibrary(it)
                assertIsDisplayedNodeWithTag(LibraryItemTags.ITEM + it.name)
            }
            assertDoesNotExistNodeWithTag(LibrariesListTags.NO_LIBRARY_TEXT)
            assertDoesNotExistNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
        }
    }

    @Test
    fun all_libraries_are_displayed_when_search_bar_is_cleared() = runTest {
        // Given
        librariesScreenRobot.composeLibrariesScreen(libraryRepository, storageRepository)
            .enterSearchQuery("co")

        // When
        librariesScreenRobot.clearSearchBar()

        // Then
        with(composeTestRule) {
            libraryRepository.getLibraries().forEach {
                librariesScreenRobot.scrollToLibrary(it)
                assertIsDisplayedNodeWithTag(LibraryItemTags.ITEM + it.name)
            }
            assertTextDoesNotExist(LibrariesListTags.NO_LIBRARY_TEXT)
            assertDoesNotExistNodeWithTag(LibrariesContentTags.PROGRESS_INDICATOR)
        }
    }

    @Test
    fun correct_callback_is_called_when_a_library_is_clicked() {
        // Given
        val library: Library = TestData.preference
        var callbackCalled = false
        var clickedLibrary: Library? = null
        val onLibraryClick: (Library) -> Unit = { library ->
            callbackCalled = true
            clickedLibrary = library
        }
        librariesScreenRobot.composeLibrariesScreen(
            libraryRepository = libraryRepository,
            storageRepository = storageRepository,
            onLibraryClick = onLibraryClick
        )
            .scrollToLibrary(library)

        // When
        librariesScreenRobot.clickOnLibrary(library)

        // Then
        Truth.assertThat(callbackCalled).isTrue()
        Truth.assertThat(clickedLibrary).isEqualTo(library)
    }

    @Test
    fun correct_callback_is_called_when_a_library_version_is_clicked() {
        // Given
        val library: Library = TestData.core
        var callbackCalled = false
        var clickedVersion: Version? = null
        val onLibraryVersionClick: (Version) -> Unit = { version ->
            callbackCalled = true
            clickedVersion = version
        }
        librariesScreenRobot.composeLibrariesScreen(
            libraryRepository = libraryRepository,
            storageRepository = storageRepository,
            onLibraryVersionClick = onLibraryVersionClick
        )
            .scrollToLibrary(library)

        // When
        librariesScreenRobot.clickOnLibraryBetaVersion(library)

        // Then
        Truth.assertThat(callbackCalled).isTrue()
        Truth.assertThat(clickedVersion).isEqualTo(library.betaVersion)
    }
}