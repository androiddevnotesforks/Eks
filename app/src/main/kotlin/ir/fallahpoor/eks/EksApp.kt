package ir.fallahpoor.eks

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import ir.fallahpoor.eks.common.NotificationManager
import ir.fallahpoor.eks.worker.RefreshLibrariesWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class EksApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager.createNotificationChannel()
        setupTimber()
        startWorker()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun startWorker() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                getString(R.string.worker_tag),
                ExistingPeriodicWorkPolicy.REPLACE,
                createWorkRequest()
            )
    }

    private fun createWorkRequest(): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        return PeriodicWorkRequestBuilder<RefreshLibrariesWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                1,
                TimeUnit.HOURS
            )
            .setInitialDelay(10, TimeUnit.MINUTES)
            .addTag(getString(R.string.worker_tag))
            .build()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}