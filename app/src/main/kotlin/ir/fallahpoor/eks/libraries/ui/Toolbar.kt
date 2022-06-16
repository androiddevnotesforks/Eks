package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.SortOrder

private enum class ToolbarMode {
    Normal,
    Search
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    sortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchQuerySubmit: (String) -> Unit
) {
    TopAppBar(modifier = modifier) {
        var toolbarMode by rememberSaveable { mutableStateOf(ToolbarMode.Normal) }
        Box(contentAlignment = Alignment.BottomCenter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h6
                )
                SortOrderButton(
                    currentSortOrder = sortOrder,
                    onSortOrderChange = onSortOrderChange
                )
                SearchButton(onClick = { toolbarMode = ToolbarMode.Search })
            }
            androidx.compose.animation.AnimatedVisibility(visible = toolbarMode == ToolbarMode.Search) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    hint = stringResource(R.string.search),
                    query = searchQuery,
                    onQueryChange = {
                        onSearchQueryChange(it)
                    },
                    onQuerySubmit = onSearchQuerySubmit,
                    onClearClick = {
                        onSearchQueryChange("")
                    },
                    onCloseClick = {
                        toolbarMode = ToolbarMode.Normal
                        onSearchQueryChange("")
                    }
                )
            }
        }
    }
}

@Composable
private fun SortOrderButton(currentSortOrder: SortOrder, onSortOrderChange: (SortOrder) -> Unit) {
    var showSortOrderDialog by rememberSaveable { mutableStateOf(false) }
    IconButton(onClick = { showSortOrderDialog = true }) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = stringResource(R.string.sort)
        )
    }
    if (showSortOrderDialog) {
        SingleSelectionDialog(
            title = stringResource(R.string.select_sort_order),
            items = SortOrder.values(),
            currentlySelectedItem = currentSortOrder,
            onItemSelect = { sortOrder: SortOrder ->
                showSortOrderDialog = false
                onSortOrderChange(sortOrder)
            },
            onDismiss = { showSortOrderDialog = false }
        )
    }
}

@Composable
private fun SearchButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search)
        )
    }
}

@Preview
@Composable
private fun ToolbarPreview() {
    Toolbar(
        sortOrder = SortOrder.A_TO_Z,
        onSortOrderChange = {},
        searchQuery = "",
        onSearchQueryChange = {},
        onSearchQuerySubmit = {}
    )
}