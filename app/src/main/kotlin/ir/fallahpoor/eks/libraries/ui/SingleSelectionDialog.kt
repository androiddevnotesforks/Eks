package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ir.fallahpoor.eks.data.BaseEnum
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.theme.ReleaseTrackerTheme

@Composable
fun <T : BaseEnum> SingleSelectionDialog(
    title: String,
    items: Array<T>,
    currentlySelectedItem: T,
    onItemSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            SingleSelectionDialogContent(
                currentlySelectedItem = currentlySelectedItem,
                onItemSelect = onItemSelect,
                items = items
            )
        },
        confirmButton = {}
    )
}

@Composable
private fun <T : BaseEnum> SingleSelectionDialogContent(
    currentlySelectedItem: T,
    onItemSelect: (T) -> Unit,
    items: Array<T>
) {
    Column {
        items.forEach { item: T ->
            Item(
                text = stringResource(item.stringResId),
                onClick = { onItemSelect(item) },
                isSelected = currentlySelectedItem == item
            )
        }
    }
}

@Composable
private fun Item(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.testTag(text),
            selected = isSelected,
            onClick = onClick
        )
        Text(text = text)
    }
}

@Preview
@Composable
private fun DialogPreview() {
    ReleaseTrackerTheme(darkTheme = false) {
        Surface {
            SingleSelectionDialog(
                title = "Some title",
                items = SortOrder.values(),
                currentlySelectedItem = SortOrder.PINNED_FIRST,
                onItemSelect = {},
                onDismiss = {}
            )
        }
    }
}