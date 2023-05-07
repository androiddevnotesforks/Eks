package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.Constants
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.theme.ReleaseTrackerTheme
import ir.fallahpoor.eks.theme.spacing

object LibraryItemTags {
    const val ITEM = "libraryItem_"
    const val PIN_BUTTON = "pinLibraryButton_"
}

@Composable
fun LibraryItem(
    library: Library,
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit,
    onLibraryPinClick: (Library, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onLibraryClick(library) })
            .padding(MaterialTheme.spacing.small)
            .testTag(LibraryItemTags.ITEM + library.name)
    ) {
        LibraryPinButton(
            modifier = Modifier.testTag(LibraryItemTags.PIN_BUTTON + library.name),
            isPinned = library.isPinned,
            onPinChange = { onLibraryPinClick(library, it) }
        )
        LibraryInformation(
            library = library,
            onLibraryVersionClick = onLibraryVersionClick
        )
    }
}

@Composable
private fun LibraryPinButton(
    isPinned: Boolean,
    onPinChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        modifier = modifier,
        checked = isPinned,
        onCheckedChange = onPinChange
    ) {
        val pinImage: Painter
        val contentDescription: String
        if (isPinned) {
            pinImage = painterResource(R.drawable.ic_pin_filled)
            contentDescription = stringResource(R.string.unpin_library)
        } else {
            pinImage = painterResource(R.drawable.ic_pin_outline)
            contentDescription = stringResource(R.string.pin_library)
        }
        Icon(
            painter = pinImage,
            tint = MaterialTheme.colors.secondary,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun LibraryInformation(
    library: Library,
    onLibraryVersionClick: (Version) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LibraryName(name = library.name)
        LibraryDescription(description = library.description)
        LibraryVersions(
            library = library,
            onLibraryVersionClick = onLibraryVersionClick
        )
    }
}

@Composable
private fun LibraryName(name: String) {
    Surface(
        color = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.background,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun LibraryDescription(description: String) {
    Text(
        modifier = Modifier
            .padding(vertical = MaterialTheme.spacing.small),
        text = description,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body1
    )
}

@Composable
private fun LibraryVersions(
    library: Library,
    onLibraryVersionClick: (Version) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            LibraryVersion(
                text = stringResource(id = R.string.version_stable, library.stableVersion.name),
                version = library.stableVersion,
                onLibraryVersionClick = onLibraryVersionClick
            )
            LibraryVersion(
                text = stringResource(id = R.string.version_beta, library.betaVersion.name),
                version = library.betaVersion,
                onLibraryVersionClick = onLibraryVersionClick
            )
        }
        Row {
            LibraryVersion(
                text = stringResource(id = R.string.version_rc, library.rcVersion.name),
                version = library.rcVersion,
                onLibraryVersionClick = onLibraryVersionClick
            )
            LibraryVersion(
                text = stringResource(id = R.string.version_alpha, library.alphaVersion.name),
                version = library.alphaVersion,
                onLibraryVersionClick = onLibraryVersionClick
            )
        }
    }
}

@Composable
private fun RowScope.LibraryVersion(
    version: Version,
    text: String,
    onLibraryVersionClick: (Version) -> Unit
) {
    Text(
        modifier = Modifier
            .weight(1f)
            .clickable(
                onClick = {
                    if (version.name != Constants.NOT_AVAILABLE) {
                        onLibraryVersionClick(version)
                    }
                }
            )
            .padding(vertical = MaterialTheme.spacing.small),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        text = text
    )
}

@Composable
@Preview
private fun LibraryItemPreviewLight() {
    LibraryItemPreview()
}

@Composable
@Preview
private fun LibraryItemPreviewDark() {
    LibraryItemPreview(isDark = true)
}

@Composable
private fun LibraryItemPreview(isDark: Boolean = false) {
    ReleaseTrackerTheme(darkTheme = isDark) {
        Surface {
            LibraryItem(
                library = Library(
                    name = "Room",
                    description = "Create, store, and manage persistent data backed by a SQLite database.",
                    releaseDate = "September 21, 2021",
                    stableVersion = Version("1.3.1"),
                    rcVersion = Version(),
                    betaVersion = Version("1.5.0-beta05"),
                    alphaVersion = Version("1.6.0-alpha02")
                ),
                onLibraryClick = {},
                onLibraryVersionClick = {},
                onLibraryPinClick = { _, _ -> }
            )
        }
    }
}