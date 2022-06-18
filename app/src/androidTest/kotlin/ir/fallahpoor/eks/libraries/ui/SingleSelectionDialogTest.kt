package ir.fallahpoor.eks.libraries.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SingleSelectionDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val dialogTitle = "Awesome Dialog"

    @Test
    fun dialog_is_initialized_correctly() {

        // Given
        composeSingleSelectionDialog(currentlySelectedItem = SortOrder.PINNED_FIRST)

        // Then
        with(composeTestRule) {
            onNodeWithText(dialogTitle)
                .assertIsDisplayed()
            SortOrder.values().forEach { item: SortOrder ->
                onNodeWithText(
                    context.getString(item.stringResId),
                    useUnmergedTree = true
                ).assertIsDisplayed()
                if (item == SortOrder.PINNED_FIRST) {
                    onNodeWithTag(context.getString(item.stringResId))
                        .assertIsSelected()
                } else {
                    onNodeWithTag(context.getString(item.stringResId))
                        .assertIsNotSelected()
                }
            }
        }

    }

    @Test
    fun correct_callback_is_called_when_an_item_is_selected() {

        // Given
        val onItemSelect: (SortOrder) -> Unit = mock()
        composeSingleSelectionDialog(
            currentlySelectedItem = SortOrder.Z_TO_A,
            onItemSelect = onItemSelect
        )

        // When
        composeTestRule.onNodeWithText(
            context.getString(SortOrder.A_TO_Z.stringResId),
            useUnmergedTree = true
        ).performClick()

        // Then
        Mockito.verify(onItemSelect).invoke(SortOrder.A_TO_Z)

    }

    @Test
    fun correct_callback_is_called_when_dialog_is_dismissed() {

        // Given
        val onDismiss: () -> Unit = mock()
        composeSingleSelectionDialog(onDismiss = onDismiss)

        // When
        Espresso.pressBack()

        // Then
        Mockito.verify(onDismiss).invoke()

    }

    private fun composeSingleSelectionDialog(
        currentlySelectedItem: SortOrder = SortOrder.A_TO_Z,
        onItemSelect: (SortOrder) -> Unit = {},
        onDismiss: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SingleSelectionDialog(
                title = dialogTitle,
                items = SortOrder.values(),
                currentlySelectedItem = currentlySelectedItem,
                onItemSelect = onItemSelect,
                onDismiss = onDismiss
            )
        }
    }

}