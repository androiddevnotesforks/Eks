package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SortOrderDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun dialog_is_initialized_correctly() {

        // Given
        composeSortOrderDialog(sortOrder = SortOrder.PINNED_FIRST)

        // Then
        with(composeTestRule) {
            onNodeWithText(context.getString(R.string.select_sort_order))
                .assertIsDisplayed()
            SortOrder.values().forEach {
                onNodeWithText(
                    context.getString(it.stringResId),
                    useUnmergedTree = true
                ).assertIsDisplayed()
                if (it == SortOrder.PINNED_FIRST) {
                    onNodeWithTag(context.getString(it.stringResId))
                        .assertIsSelected()
                } else {
                    onNodeWithTag(context.getString(it.stringResId))
                        .assertIsNotSelected()
                }
            }
        }

    }

    @Test
    fun correct_callback_is_called_when_an_sort_order_is_selected() {

        // Given
        val onSortOrderClick: (SortOrder) -> Unit = mock()
        composeSortOrderDialog(
            sortOrder = SortOrder.Z_TO_A,
            onSortOrderClick = onSortOrderClick
        )

        // When
        composeTestRule.onNodeWithText(
            context.getString(SortOrder.A_TO_Z.stringResId),
            useUnmergedTree = true
        ).performClick()

        // Then
        Mockito.verify(onSortOrderClick).invoke(SortOrder.A_TO_Z)

    }

    @Test
    fun correct_callback_is_called_when_dialog_is_dismissed() {

        // Given
        val onDismiss: () -> Unit = mock()
        composeSortOrderDialog(onDismiss = onDismiss)

        // When
        Espresso.pressBack()

        // Then
        Mockito.verify(onDismiss).invoke()

    }

    private fun composeSortOrderDialog(
        sortOrder: SortOrder = SortOrder.A_TO_Z,
        onSortOrderClick: (SortOrder) -> Unit = {},
        onDismiss: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SortOrderDialog(
                sortOrder = sortOrder,
                onSortOrderClick = onSortOrderClick,
                onDismiss = onDismiss
            )
        }
    }

}