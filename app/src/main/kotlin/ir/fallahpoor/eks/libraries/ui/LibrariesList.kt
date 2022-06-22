@file:OptIn(ExperimentalFoundationApi::class)

package ir.fallahpoor.eks.libraries.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.theme.spacing

object LibrariesListTags {
    const val NO_LIBRARY_TEXT = "librariesListNoLibrary"
}

@Composable
fun LibrariesList(
    modifier: Modifier = Modifier,
    libraries: List<Library>,
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit,
    onLibraryPinClick: (Library, Boolean) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(
            visible = libraries.isEmpty(),
            enter = fadeIn()
        ) {
            NoLibrary()
        }
        AnimatedVisibility(
            visible = libraries.isNotEmpty(),
            enter = fadeIn()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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