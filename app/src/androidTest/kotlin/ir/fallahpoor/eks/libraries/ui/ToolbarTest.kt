package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val appNameText = context.getString(R.string.app_name)
    private val sortText = context.getString(R.string.sort)
    private val searchText = context.getString(R.string.search)
    private val selectSortOrderText = context.getString(R.string.select_sort_order)

    @Test
    fun toolbar_is_initialized_correctly() {

        // Given
        composeToolbar()

        // Then
        composeTestRule.onNodeWithText(appNameText)
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(
            sortText,
            useUnmergedTree = true
        ).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(
            searchText,
            useUnmergedTree = true
        ).assertIsDisplayed()
        composeTestRule.onNodeWithTag(SearchBarTags.SEARCH_BAR)
            .assertDoesNotExist()

    }

    @Test
    fun sort_dialog_is_displayed_when_sort_button_is_clicked() {

        // Given
        composeToolbar()

        // When
        composeTestRule.onNodeWithContentDescription(
            sortText,
            useUnmergedTree = true
        ).performClick()

        // Then
        composeTestRule.onNodeWithText(selectSortOrderText)
            .assertIsDisplayed()

    }

    @Test
    fun search_bar_is_displayed_when_search_button_is_clicked() {

        // Given
        composeToolbar()

        // When
        composeTestRule.onNodeWithContentDescription(
            searchText,
            useUnmergedTree = true
        ).performClick()

        // Then
        composeTestRule.onNodeWithTag(SearchBarTags.SEARCH_BAR)
            .assertIsDisplayed()

    }

    @Test
    fun correct_callback_is_called_when_sort_order_is_selected() {

        // Given
        val onSortOrderChange: (SortOrder) -> Unit = mock()
        composeToolbar(onSortOrderChange = onSortOrderChange)

        // When
        with(composeTestRule) {
            onNodeWithContentDescription(
                sortText,
                useUnmergedTree = true
            ).performClick()
            onNodeWithText(
                context.getString(SortOrder.Z_TO_A.stringResId),
                useUnmergedTree = true
            ).performClick()
        }

        // Then
        Mockito.verify(onSortOrderChange).invoke(SortOrder.Z_TO_A)

    }

    @Test
    fun sort_dialog_is_closed_when_sort_order_is_selected() {

        // Given
        composeToolbar()

        // When
        with(composeTestRule) {
            onNodeWithContentDescription(
                sortText,
                useUnmergedTree = true
            ).performClick()
            onNodeWithText(
                context.getString(SortOrder.A_TO_Z.stringResId),
                useUnmergedTree = true
            ).performClick()
        }

        // Then
        composeTestRule.onNodeWithText(selectSortOrderText, useUnmergedTree = true)
            .assertDoesNotExist()

    }

    @Test
    fun correct_callback_is_called_when_search_query_is_changed() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        composeToolbar(onSearchQueryChange = onSearchQueryChange)

        // When
        composeTestRule.onNodeWithContentDescription(
            searchText,
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
            .performTextInput("Coil")

        // Then
        Mockito.verify(onSearchQueryChange).invoke("Coil")

    }

    @Test
    fun correct_callback_is_called_when_search_query_is_cleared() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        composeToolbar(searchQuery = "Koin", onSearchQueryChange = onSearchQueryChange)

        // When
        composeTestRule.onNodeWithContentDescription(
            searchText,
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithTag(SearchBarTags.CLEAR_BUTTON)
            .performClick()

        // Then
        Mockito.verify(onSearchQueryChange).invoke("")

    }

    @Test
    fun toolbar_is_set_to_normal_mode_when_search_bar_is_closed() {

        // Given
        composeToolbar()

        // When
        composeTestRule.onNodeWithContentDescription(
            searchText,
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithTag(SearchBarTags.CLOSE_BUTTON)
            .performClick()

        // Then
        composeTestRule.onNodeWithTag(SearchBarTags.SEARCH_BAR)
            .assertDoesNotExist()

    }

    private fun composeToolbar(
        sortOrder: SortOrder = SortOrder.A_TO_Z,
        onSortOrderChange: (SortOrder) -> Unit = {},
        searchQuery: String = "",
        onSearchQueryChange: (String) -> Unit = {},
        onSearchQuerySubmit: (String) -> Unit = {}
    ) {
        composeTestRule.setContent {
            Toolbar(
                sortOrder = sortOrder,
                onSortOrderChange = onSortOrderChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearchQuerySubmit = onSearchQuerySubmit
            )
        }
    }

}