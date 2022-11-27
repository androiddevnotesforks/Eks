package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.fallahpoor.eks.data.SortOrder
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.libraries.viewmodel.LibrariesViewModel
import ir.fallahpoor.eks.theme.ReleaseTrackerTheme

object LibrariesScreenTags {
    const val TOOLBAR = "librariesScreenToolbar"
    const val CONTENT = "librariesScreenContent"
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LibrariesScreen(
    librariesViewModel: LibrariesViewModel = viewModel(),
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit
) {
    val uiState: LibrariesScreenUiState by librariesViewModel.librariesScreenUiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        librariesViewModel.handleEvent(LibrariesViewModel.Event.GetLibraries)
    }

    ReleaseTrackerTheme {
        Scaffold(
            topBar = {
                Toolbar(
                    modifier = Modifier.testTag(LibrariesScreenTags.TOOLBAR),
                    sortOrderProvider = { uiState.sortOrder },
                    onSortOrderChange = { sortOrder: SortOrder ->
                        librariesViewModel.handleEvent(
                            LibrariesViewModel.Event.ChangeSortOrder(sortOrder)
                        )
                    },
                    searchQueryProvider = { uiState.searchQuery },
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
        ) { paddingValues ->
            LibrariesContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
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