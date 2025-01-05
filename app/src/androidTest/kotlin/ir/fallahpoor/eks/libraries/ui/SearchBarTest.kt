package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.google.common.truth.Truth
import ir.fallahpoor.eks.libraries.ui.robots.SearchBarRobot
import org.junit.Rule
import org.junit.Test

class SearchBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val searchBarRobot = SearchBarRobot(composeTestRule)

    @Test
    fun search_bar_is_initialized_correctly() {
        // Given
        val hint = "search"
        searchBarRobot.composeSearchBar(hint = hint)

        // When the composable is freshly composed

        // Then
        with(composeTestRule) {
            onNodeWithText(hint, useUnmergedTree = true).assertIsDisplayed()
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD).assertTextEquals("")
            assertIsDisplayedNodeWithTag(SearchBarTags.CLOSE_BUTTON)
            assertIsDisplayedNodeWithTag(SearchBarTags.CLEAR_BUTTON)
        }
    }

    @Test
    fun when_search_query_is_not_empty_hint_is_not_displayed() {
        // Given
        val hint = "Enter library name"
        val query = "Coil"
        searchBarRobot.composeSearchBar(hint = hint, query = query)

        // When

        // Then
        composeTestRule.assertTextDoesNotExist(hint)
    }

    @Test
    fun when_clear_button_is_clicked_correct_callback_is_called() {
        // Given
        var callbackCalled = false
        val onClearClick: () -> Unit = {
            callbackCalled = true
        }
        searchBarRobot.composeSearchBar(onClearClick = onClearClick)

        // When
        searchBarRobot.clickClearButton()

        // Then
        Truth.assertThat(callbackCalled).isTrue()
    }

    @Test
    fun when_close_button_is_clicked_correct_callback_is_called() {
        // Given
        var callbackCalled = false
        val onCloseClick: () -> Unit = {
            callbackCalled = true
        }
        searchBarRobot.composeSearchBar(onCloseClick = onCloseClick)

        // When
        searchBarRobot.clickCloseButton()

        // Then
        Truth.assertThat(callbackCalled).isTrue()
    }

    @Test
    fun when_query_is_changed_correct_callback_is_called() {
        // Given
        var callbackCalled = false
        var searchQuery = ""
        val onQueryChange: (String) -> Unit = {
            callbackCalled = true
            searchQuery = it
        }
        searchBarRobot.composeSearchBar(onQueryChange = onQueryChange)

        // When
        searchBarRobot.enterSearchQuery("Coroutines")

        // Then
        Truth.assertThat(callbackCalled).isTrue()
        Truth.assertThat(searchQuery).isEqualTo("Coroutines")
    }

    // TODO Test if the correct callback is called when query is submitted
}