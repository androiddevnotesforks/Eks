package ir.fallahpoor.eks.libraries.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import ir.fallahpoor.eks.data.NightMode
import ir.fallahpoor.eks.libraries.mock
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class SingleSelectionDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val dialogTitle = "Awesome Dialog"

    @Test
    fun dialog_is_initialized_correctly() {

        // Given
        composeSingleSelectionDialog(currentlySelectedItem = NightMode.OFF)

        // Then
        with(composeTestRule) {
            onNodeWithText(dialogTitle)
                .assertIsDisplayed()
            NightMode.values().forEach { item: NightMode ->
                onNodeWithText(
                    item.name,
                    useUnmergedTree = true
                ).assertIsDisplayed()
                if (item == NightMode.OFF) {
                    onNodeWithTag(item.name)
                        .assertIsSelected()
                } else {
                    onNodeWithTag(item.name)
                        .assertIsNotSelected()
                }
            }
        }

    }

    @Test
    fun correct_callback_is_called_when_an_item_is_selected() {

        // Given
        val onItemSelect: (NightMode) -> Unit = mock()
        composeSingleSelectionDialog(
            currentlySelectedItem = NightMode.ON,
            onItemSelect = onItemSelect
        )

        // When
        composeTestRule.onNodeWithText(
            InstrumentationRegistry.getInstrumentation().context.getString(NightMode.AUTO.stringResId),
            useUnmergedTree = true
        ).performClick()

        // Then
        Mockito.verify(onItemSelect).invoke(NightMode.AUTO)

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
        currentlySelectedItem: NightMode = NightMode.OFF,
        onItemSelect: (NightMode) -> Unit = {},
        onDismiss: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            SingleSelectionDialog(
                title = dialogTitle,
                items = NightMode.values(),
                currentlySelectedItem = currentlySelectedItem,
                onItemSelect = onItemSelect,
                onDismiss = onDismiss
            )
        }
    }

}