@file:OptIn(ExperimentalFoundationApi::class)

package ir.fallahpoor.eks.libraries.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.model.Library
import ir.fallahpoor.eks.data.model.Version
import ir.fallahpoor.eks.theme.spacing
import kotlinx.coroutines.launch

object LibrariesListTags {
    const val LIBRARIES_LIST = "librariesListLibrariesList"
    const val NO_LIBRARY_TEXT = "librariesListNoLibraryText"
    const val SCROLL_TO_TOP_BUTTON = "librariesListScrollToTopButton"
}

@Composable
fun LibrariesList(
    modifier: Modifier = Modifier,
    libraries: List<Library>,
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit,
    onLibraryPinClick: (Library, Boolean) -> Unit
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = libraries.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NoLibrary()
        }
        AnimatedVisibility(
            visible = libraries.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(LibrariesListTags.LIBRARIES_LIST),
                    state = listState
                ) {
                    items(
                        items = libraries,
                        key = { library: Library -> library.name }
                    ) { library: Library ->
                        LibraryItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            library = library,
                            onLibraryClick = onLibraryClick,
                            onLibraryVersionClick = onLibraryVersionClick,
                            onLibraryPinClick = onLibraryPinClick
                        )
                        Divider()
                    }
                }
                val coroutineScope = rememberCoroutineScope()
                val showScrollToTopButton by remember {
                    derivedStateOf {
                        listState.firstVisibleItemIndex > 0
                    }
                }
                ScrollToTopButton(show = showScrollToTopButton) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            }
        }
    }
}

@Composable
private fun NoLibrary() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LibrariesListTags.NO_LIBRARY_TEXT),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.normal),
            text = stringResource(R.string.no_library)
        )
    }
}

@Composable
private fun ScrollToTopButton(show: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            modifier = Modifier
                .padding(MaterialTheme.spacing.normal)
                .testTag(LibrariesListTags.SCROLL_TO_TOP_BUTTON),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.scroll_to_top)
            )
        }
    }
}