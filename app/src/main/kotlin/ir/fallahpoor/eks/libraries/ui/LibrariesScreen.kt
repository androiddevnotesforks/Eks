package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.viewmodel.LibrariesViewModel
import ir.fallahpoor.eks.theme.ReleaseTrackerTheme

object LibrariesScreenTags {
    const val SCREEN = "librariesScreen"
    const val TOOLBAR = "librariesScreenToolbar"
    const val CONTENT = "librariesScreenContent"
}

@Composable
fun LibrariesScreen(
    librariesViewModel: LibrariesViewModel = viewModel(),
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit
) {
    val uiState: LibrariesScreenState by librariesViewModel.librariesScreenState
        .collectAsState()

    LaunchedEffect(true) {
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)
    }

    ReleaseTrackerTheme {
        Scaffold(
            modifier = Modifier.testTag(LibrariesScreenTags.SCREEN),
            topBar = {
                Toolbar(
                    modifier = Modifier.testTag(LibrariesScreenTags.TOOLBAR),
                    sortOrder = uiState.sortOrder,
                    onSortOrderChange = { sortOrder: SortOrder ->
                        librariesViewModel.handleEvent(
                            LibrariesViewModel.Event.ChangeSortOrder(sortOrder)
                        )
                    },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { searchQuery: String ->
                        librariesViewModel.handleEvent(
                            LibrariesViewModel.Event.ChangeSearchQuery(searchQuery)
                        )
                    },
                    onSearchQuerySubmit = { searchQuery: String ->
                        librariesViewModel.handleEvent(
                            LibrariesViewModel.Event.ChangeSearchQuery(searchQuery)
                        )
                    }
                )
            },
            scaffoldState = rememberScaffoldState()
        ) {
            LibrariesContent(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(LibrariesScreenTags.CONTENT),
                librariesState = uiState.librariesState,
                refreshDate = uiState.refreshDate,
                onLibraryClick = onLibraryClick,
                onLibraryVersionClick = onLibraryVersionClick,
                onLibraryPinClick = { library: Library, pin: Boolean ->
                    librariesViewModel.handleEvent(
                        LibrariesViewModel.Event.PinLibrary(library, pin)
                    )
                },
                onTryAgainClick = { librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries) }
            )
        }
    }
}