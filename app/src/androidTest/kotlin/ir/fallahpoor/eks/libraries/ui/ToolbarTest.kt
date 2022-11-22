package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sortText = context.getString(R.string.sort)
    private val searchText = context.getString(R.string.search)
    private val selectSortOrderText = context.getString(R.string.select_sort_order)

    @Test
    fun toolbar_is_initialized_correctly() {

        // Given
        composeToolbar()

        // Then
        with(composeTestRule) {
            assertTextIsDisplayed(context.getString(R.string.app_name))
            assertIsDisplayedNodeWithContentDescription(sortText)
            assertIsDisplayedNodeWithContentDescription(searchText)
            assertDoesNotExistNodeWithTag(SearchBarTags.SEARCH_BAR)
        }

    }

    @Test
    fun when_sort_button_is_clicked_sort_dialog_is_displayed() {

        // Given
        composeToolbar()

        // When
        composeTestRule.clickOnNodeWithContentDescription(sortText)

        // Then
        composeTestRule.assertTextIsDisplayed(selectSortOrderText)

    }

    @Test
    fun when_search_button_is_clicked_search_bar_is_displayed() {

        // Given
        composeToolbar()

        // When
        clickOnSearchButton()

        // Then
        composeTestRule.assertIsDisplayedNodeWithTag(SearchBarTags.SEARCH_BAR)

    }

    @Test
    fun when_sort_order_is_selected_correct_callback_is_called() {

        // Given
        val onSortOrderChange: (SortOrder) -> Unit = mock()
        composeToolbar(onSortOrderChange = onSortOrderChange)

        // When
        selectSortOrder(SortOrder.Z_TO_A)

        // Then
        Mockito.verify(onSortOrderChange).invoke(SortOrder.Z_TO_A)

    }

    private fun selectSortOrder(sortOrder: SortOrder) {
        composeTestRule.clickOnNodeWithContentDescription(sortText)
        composeTestRule.clickOnNodeWithText(context.getString(sortOrder.stringResId))
    }

    @Test
    fun when_sort_order_is_selected_sort_order_dialog_is_closed() {

        // Given
        composeToolbar()

        // When
        selectSortOrder(SortOrder.A_TO_Z)

        // Then
        composeTestRule.assertTextDoesNotExist(selectSortOrderText)

    }

    @Test
    fun when_search_query_is_changed_correct_callback_is_called() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        composeToolbar(onSearchQueryChange = onSearchQueryChange)

        // When
        enterSearchQuery("Coil")

        // Then
        Mockito.verify(onSearchQueryChange).invoke("Coil")

    }

    private fun enterSearchQuery(searchQuery: String) {
        clickOnSearchButton()
        composeTestRule.onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD)
            .performTextInput(searchQuery)
    }

    @Test
    fun when_search_query_is_cleared_correct_callback_is_called() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        composeToolbar(searchQuery = "Koin", onSearchQueryChange = onSearchQueryChange)

        // When
        clearSearchQuery()

        // Then
        Mockito.verify(onSearchQueryChange).invoke("")

    }

    @Test
    fun when_search_bar_is_closed_toolbar_is_set_to_normal_mode() {

        // Given
        composeToolbar()

        // When
        closeSearchBar()

        // Then
        composeTestRule.assertDoesNotExistNodeWithTag(SearchBarTags.SEARCH_BAR)

    }

    private fun clearSearchQuery() {
        clickOnSearchButton()
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLEAR_BUTTON)
    }

    private fun closeSearchBar() {
        composeTestRule.clickOnNodeWithContentDescription(searchText)
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLOSE_BUTTON)
    }

    private fun clickOnSearchButton() {
        composeTestRule.clickOnNodeWithContentDescription(searchText)
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