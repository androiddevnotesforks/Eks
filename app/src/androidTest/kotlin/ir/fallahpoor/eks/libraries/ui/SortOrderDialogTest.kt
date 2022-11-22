package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
            assertTextIsDisplayed(context.getString(R.string.select_sort_order))
            SortOrder.values().forEach {
                assertTextIsDisplayed(context.getString(it.stringResId))
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
    fun when_a_sort_order_is_selected_correct_callback_is_called() {

        // Given
        val onSortOrderClick: (SortOrder) -> Unit = mock()
        composeSortOrderDialog(
            sortOrder = SortOrder.Z_TO_A,
            onSortOrderClick = onSortOrderClick
        )

        // When
        composeTestRule.clickOnNodeWithText(context.getString(SortOrder.A_TO_Z.stringResId))

        // Then
        Mockito.verify(onSortOrderClick).invoke(SortOrder.A_TO_Z)

    }

    @Test
    fun when_dialog_is_dismissed_correct_callback_is_called() {

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