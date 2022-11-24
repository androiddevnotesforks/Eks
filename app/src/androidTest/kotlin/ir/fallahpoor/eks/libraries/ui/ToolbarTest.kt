package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.ui.robots.ToolbarRobot
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ToolbarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val toolbarRobot = ToolbarRobot(context, composeTestRule)
    private val sortText = context.getString(R.string.sort)
    private val selectSortOrderText = context.getString(R.string.select_sort_order)

    @Test
    fun toolbar_is_initialized_correctly() {

        // Given
        toolbarRobot.composeToolbar()

        // Then
        toolbarRobot.assertElementsAreDisplayedCorrectly()

    }

    @Test
    fun when_sort_button_is_clicked_sort_dialog_is_displayed() {

        // Given
        toolbarRobot.composeToolbar()

        // When
        composeTestRule.clickOnNodeWithContentDescription(sortText)

        // Then
        composeTestRule.assertTextIsDisplayed(selectSortOrderText)

    }

    @Test
    fun when_search_button_is_clicked_search_bar_is_displayed() {

        // Given
        toolbarRobot.composeToolbar()

        // When
        toolbarRobot.clickOnSearchButton()

        // Then
        composeTestRule.assertIsDisplayedNodeWithTag(SearchBarTags.SEARCH_BAR)

    }

    @Test
    fun when_sort_order_is_selected_correct_callback_is_called() {

        // Given
        val onSortOrderChange: (SortOrder) -> Unit = mock()
        toolbarRobot.composeToolbar(onSortOrderChange = onSortOrderChange)

        // When
        toolbarRobot.selectSortOrder(SortOrder.Z_TO_A)

        // Then
        Mockito.verify(onSortOrderChange).invoke(SortOrder.Z_TO_A)

    }

    @Test
    fun when_sort_order_is_selected_sort_order_dialog_is_closed() {

        // Given
        toolbarRobot.composeToolbar()

        // When
        toolbarRobot.selectSortOrder(SortOrder.A_TO_Z)

        // Then
        composeTestRule.assertTextDoesNotExist(selectSortOrderText)

    }

    @Test
    fun when_search_query_is_changed_correct_callback_is_called() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        toolbarRobot.composeToolbar(onSearchQueryChange = onSearchQueryChange)

        // When
        toolbarRobot.enterSearchQuery("Coil")

        // Then
        Mockito.verify(onSearchQueryChange).invoke("Coil")

    }

    @Test
    fun when_search_query_is_cleared_correct_callback_is_called() {

        // Given
        val onSearchQueryChange: (String) -> Unit = mock()
        toolbarRobot.composeToolbar(searchQuery = "Koin", onSearchQueryChange = onSearchQueryChange)

        // When
        toolbarRobot.clearSearchQuery()

        // Then
        Mockito.verify(onSearchQueryChange).invoke("")

    }

    @Test
    fun when_search_bar_is_closed_toolbar_is_set_to_normal_mode() {

        // Given
        toolbarRobot.composeToolbar()

        // When
        toolbarRobot.closeSearchBar()

        // Then
        composeTestRule.assertDoesNotExistNodeWithTag(SearchBarTags.SEARCH_BAR)

    }

}