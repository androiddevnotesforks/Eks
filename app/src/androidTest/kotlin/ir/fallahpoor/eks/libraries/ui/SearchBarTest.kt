package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun search_bar_is_initialized_correctly() {

        // Given
        val hint = "hint"
        composeSearchBar(hint = "hint")

        // When the composable is freshly composed

        // Then
        with(composeTestRule) {
            onNodeWithText(hint).assertIsDisplayed()
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .assertTextEquals("")
            assertIsDisplayedNodeWithTag(SearchBarTags.CLOSE_BUTTON)
            assertIsDisplayedNodeWithTag(SearchBarTags.CLEAR_BUTTON)
        }

    }

    @Test
    fun when_search_query_is_not_empty_hint_is_not_displayed() {

        // Given
        val hint = "Enter library name"
        val query = "Coil"
        composeSearchBar(hint = hint, query = query)

        // When

        // Then
        with(composeTestRule) {
            assertTextDoesNotExist(hint)
            onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
                .assertTextEquals(query)
        }

    }

    @Test
    fun when_clear_button_is_clicked_correct_callback_is_called() {

        // Given
        val onClearClick: () -> Unit = mock()
        composeSearchBar(onClearClick = onClearClick)

        // When
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLEAR_BUTTON)

        // Then
        Mockito.verify(onClearClick).invoke()

    }

    @Test
    fun when_close_button_is_clicked_correct_callback_is_called() {

        // Given
        val onCloseClick: () -> Unit = mock()
        composeSearchBar(onCloseClick = onCloseClick)

        // When
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLOSE_BUTTON)

        // Then
        Mockito.verify(onCloseClick).invoke()

    }

    @Test
    fun when_query_is_changed_correct_callback_is_called() {

        // Given
        val onQueryChange: (String) -> Unit = mock()
        composeSearchBar(onQueryChange = onQueryChange)

        // When
        enterSearchQuery("Coroutines")

        // Then
        Mockito.verify(onQueryChange).invoke("Coroutines")

    }

    private fun enterSearchQuery(searchQuery: String) {
        composeTestRule.onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
            .performTextInput(searchQuery)
    }

    // TODO Test if the correct callback is called when query is submitted

    private fun composeSearchBar(
        hint: String = "",
        query: String = "",
        onQueryChange: (String) -> Unit = {},
        onQuerySubmit: (String) -> Unit = {},
        onClearClick: () -> Unit = {},
        onCloseClick: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SearchBar(
                hint = hint,
                query = query,
                onQueryChange = onQueryChange,
                onQuerySubmit = onQuerySubmit,
                onClearClick = onClearClick,
                onCloseClick = onCloseClick
            )
        }
    }

}