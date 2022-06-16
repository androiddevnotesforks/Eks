package ir.fallahpoor.eks.libraries.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.data.entity.Library
import ir.fallahpoor.eks.data.entity.Version
import ir.fallahpoor.eks.theme.ReleaseTrackerTheme
import ir.fallahpoor.eks.theme.spacing

object LibrariesContentTags {
    const val PROGRESS_INDICATOR = "librariesContentProgressIndicator"
    const val LIBRARIES_LIST = "librariesContentLibrariesList"
    const val REFRESH_DATE = "librariesContentRefreshDate"
    const val TRY_AGAIN = "librariesContentTryAgain"
}

@Composable
fun LibrariesContent(
    modifier: Modifier = Modifier,
    librariesState: LibrariesState,
    refreshDate: String,
    onLibraryClick: (Library) -> Unit,
    onLibraryVersionClick: (Version) -> Unit,
    onLibraryPinClick: (Library, Boolean) -> Unit,
    onTryAgainClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (librariesState) {
            is LibrariesState.Loading -> ProgressIndicator()
            is LibrariesState.Success -> {
                RefreshDate(refreshDate)
                Divider()
                LibrariesList(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(LibrariesContentTags.LIBRARIES_LIST),
                    libraries = librariesState.libraries,
                    onLibraryClick = onLibraryClick,
                    onLibraryVersionClick = onLibraryVersionClick,
                    onLibraryPinClick = onLibraryPinClick
                )
            }
            is LibrariesState.Error -> {
                TryAgain(
                    errorMessage = librariesState.message,
                    onTryAgainClick = onTryAgainClick
                )
            }
        }
    }
}

@Composable
private fun ProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LibrariesContentTags.PROGRESS_INDICATOR),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun RefreshDate(refreshDate: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.normal)
            .testTag(LibrariesContentTags.REFRESH_DATE),
        text = stringResource(R.string.refresh_date, refreshDate)
    )
}

@Composable
private fun TryAgain(errorMessage: String, onTryAgainClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LibrariesContentTags.TRY_AGAIN)
    ) {
        val guideline = createGuidelineFromTop(0.5f)
        val (tryAgainButton, errorMessageText) = createRefs()
        Text(
            modifier = Modifier
                .padding(MaterialTheme.spacing.normal)
                .constrainAs(errorMessageText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(guideline)
                },
            text = errorMessage,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Button(
            modifier = Modifier.constrainAs(tryAgainButton) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(guideline)
            },
            onClick = onTryAgainClick
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}

@Composable
@Preview
private fun LibrariesListContentPreview() {
    ReleaseTrackerTheme {
        Surface {
            LibrariesContent(
                librariesState = LibrariesState.Error("An error has occurred."),
                refreshDate = "N/A",
                onLibraryClick = {},
                onLibraryVersionClick = {},
                onLibraryPinClick = { _, _ -> },
                onTryAgainClick = {}
            )
        }
    }
}