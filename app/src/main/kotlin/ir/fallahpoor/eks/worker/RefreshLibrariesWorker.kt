package ir.fallahpoor.eks.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.common.NotificationManager
import ir.fallahpoor.eks.data.ConnectivityChecker
import ir.fallahpoor.eks.data.model.Library
import ir.fallahpoor.eks.data.repository.LibraryRepository

@HiltWorker
class RefreshLibrariesWorker
@AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val libraryRepository: LibraryRepository,
    private val connectivityChecker: ConnectivityChecker,
    private val notificationBodyMaker: NotificationBodyMaker,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result =
        if (!connectivityChecker.isNetworkReachable()) {
            Result.retry()
        } else {
            try {
                val oldLibraries = libraryRepository.getLibraries()
                libraryRepository.refreshLibraries()
                val refreshedLibraries = libraryRepository.getLibraries()
                showNotification(oldLibraries, refreshedLibraries)
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }

    private fun showNotification(
        oldLibraries: List<Library>,
        refreshedLibraries: List<Library>
    ) {
        val body: String? = notificationBodyMaker.makeBody(oldLibraries, refreshedLibraries)
        body?.let {
            notificationManager.showNotification(
                title = context.getString(R.string.notification_title),
                body = it
            )
        }
    }

}