package ir.fallahpoor.eks.libraries.ui.robots

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import ir.fallahpoor.eks.libraries.ui.SearchBar
import ir.fallahpoor.eks.libraries.ui.SearchBarTags
import ir.fallahpoor.eks.libraries.ui.clickOnNodeWithTag

class SearchBarRobot(private val composeTestRule: ComposeContentTestRule) {

    fun composeSearchBar(
        hint: String = "",
        query: String = "",
        onQueryChange: (String) -> Unit = {},
        onQuerySubmit: (String) -> Unit = {},
        onClearClick: () -> Unit = {},
        onCloseClick: () -> Unit = {}
    ): SearchBarRobot {
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
        return this
    }

    fun clickClearButton() {
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLEAR_BUTTON)
    }

    fun clickCloseButton() {
        composeTestRule.clickOnNodeWithTag(SearchBarTags.CLOSE_BUTTON)
    }

    fun enterSearchQuery(searchQuery: String) {
        composeTestRule.onNodeWithTag(SearchBarTags.QUERY_TEXT_FIELD).performTextInput(searchQuery)
    }

}