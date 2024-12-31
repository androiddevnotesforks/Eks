package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.theme.EksTheme

@Composable
fun SortOrderDialog(
    sortOrderProvider: () -> SortOrder,
    onSortOrderClick: (SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_sort_order))
        },
        text = {
            SortOrderDialogContent(
                sortOrderProvider = sortOrderProvider,
                onSortOrderClick = onSortOrderClick
            )
        },
        confirmButton = {}
    )
}

@Composable
private fun SortOrderDialogContent(
    sortOrderProvider: () -> SortOrder,
    onSortOrderClick: (SortOrder) -> Unit
) {
    Column {
        SortOrder.entries.forEach {
            SortOrderItem(
                text = stringResource(it.stringResId),
                onClick = { onSortOrderClick(it) },
                isSelected = sortOrderProvider() == it
            )
        }
    }
}

@Composable
private fun SortOrderItem(
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

@Composable
@PreviewLightDark
private fun SortOrderDialogPreview() {
    EksTheme {
        Surface {
            SortOrderDialog(
                sortOrderProvider = { SortOrder.PINNED_FIRST },
                onSortOrderClick = {},
                onDismiss = {}
            )
        }
    }
}