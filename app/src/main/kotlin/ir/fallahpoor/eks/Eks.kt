package ir.fallahpoor.eks

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ir.fallahpoor.eks.data.repository.model.Library
import ir.fallahpoor.eks.data.repository.model.Version
import ir.fallahpoor.eks.libraries.ui.LibrariesScreen

@Composable
fun Eks() {
    val context = LocalContext.current
    LibrariesScreen(
        onLibraryClick = { library: Library ->
            openWebPage(context, library.url)
        },
        onLibraryVersionClick = { version: Version ->
            openWebPage(context, version.releaseNotesUrl)
        }
    )
}

// TODO handle the case when provided URL is not valid
private fun openWebPage(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.no_browser_found),
            Toast.LENGTH_SHORT
        ).show()
    }
}