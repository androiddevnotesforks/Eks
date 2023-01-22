package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.ui.robots.SortOrderDialogRobot
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SortOrderDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sortOrderDialogRobot = SortOrderDialogRobot(context, composeTestRule)

    @Test
    fun dialog_is_initialized_correctly() {

        // Given
        sortOrderDialogRobot.composeSortOrderDialog(sortOrder = SortOrder.PINNED_FIRST)

        // Then
        sortOrderDialogRobot.assertElementsAreCorrectlyDisplayedInitially(selectedSortOrder = SortOrder.PINNED_FIRST)

    }

    @Test
    fun when_a_sort_order_is_selected_correct_callback_is_called() {

        // Given
        val onSortOrderClick: (SortOrder) -> Unit = mock()
        sortOrderDialogRobot.composeSortOrderDialog(
            sortOrder = SortOrder.Z_TO_A,
            onSortOrderClick = onSortOrderClick
        )

        // When
        sortOrderDialogRobot.selectSortOrder(SortOrder.A_TO_Z)

        // Then
        Mockito.verify(onSortOrderClick).invoke(SortOrder.A_TO_Z)

    }

    @Test
    fun when_dialog_is_dismissed_correct_callback_is_called() {

        // Given
        val onDismiss: () -> Unit = mock()
        sortOrderDialogRobot.composeSortOrderDialog(onDismiss = onDismiss)

        // When
        Espresso.pressBack()

        // Then
        Mockito.verify(onDismiss).invoke()

    }

}