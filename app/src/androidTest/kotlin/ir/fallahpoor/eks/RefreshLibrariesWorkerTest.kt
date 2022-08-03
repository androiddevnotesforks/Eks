package ir.fallahpoor.eks

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth
import ir.fallahpoor.eks.common.NotificationManager
import ir.fallahpoor.eks.data.ConnectivityChecker
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.fakes.FakeLibraryRepository
import ir.fallahpoor.eks.worker.NotificationBodyMaker
import ir.fallahpoor.eks.worker.RefreshLibrariesWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RefreshLibrariesWorkerTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var connectivityChecker: ConnectivityChecker

    @Mock
    private lateinit var notificationBodyMaker: NotificationBodyMaker

    @Mock
    private lateinit var notificationManager: NotificationManager

    private lateinit var libraryRepository: FakeLibraryRepository
    private lateinit var worker: RefreshLibrariesWorker
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun runBeforeEachTest() {
        libraryRepository = FakeLibraryRepository()
        worker = createWorker(libraryRepository)
    }

    private fun createWorker(libraryRepository: LibraryRepository): RefreshLibrariesWorker {
        return TestListenableWorkerBuilder<RefreshLibrariesWorker>(context)
            .setWorkerFactory(
                CustomWorkerFactory(
                    libraryRepository,
                    connectivityChecker,
                    notificationBodyMaker,
                    notificationManager
                )
            )
            .build()
    }

    @Test
    fun doWork_should_return_Retry_when_network_is_not_reachable() = runTest {

        // Given
        Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(false)

        // When
        val result = worker.doWork()

        // Then
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
        Mockito.verify(connectivityChecker).isNetworkReachable()
        Mockito.verifyNoInteractions(notificationManager)
        Mockito.verifyNoInteractions(notificationBodyMaker)

    }

    @Test
    fun doWork_should_return_Retry_when_an_exception_occurs() = runTest {

        // Given
        Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(true)
        libraryRepository.throwException = true

        // When
        val result = worker.doWork()

        // Then
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
        Mockito.verifyNoInteractions(notificationManager)
        Mockito.verifyNoInteractions(notificationBodyMaker)

    }

    @Test
    fun no_notification_is_displayed_when_there_is_no_update() =
        runTest {

            // Given
            Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(true)
            libraryRepository.updateIsAvailable = false

            // When
            val result = worker.doWork()

            // Then
            Mockito.verifyNoInteractions(notificationManager)
            Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)

        }

    @Test
    fun a_notification_is_displayed_when_there_are_updates() = runTest {

        // Given
        Mockito.`when`(connectivityChecker.isNetworkReachable())
            .thenReturn(true)
        Mockito.`when`(notificationBodyMaker.makeBody(anyList(), anyList()))
            .thenReturn("X")
        libraryRepository.updateIsAvailable = true

        // When
        val result = worker.doWork()

        // Then
        Mockito.verify(notificationManager)
            .showNotification(context.getString(R.string.notification_title), "X")
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)

    }

    private class CustomWorkerFactory(
        private val libraryRepository: LibraryRepository,
        private val connectivityChecker: ConnectivityChecker,
        private val notificationBodyMaker: NotificationBodyMaker,
        private val notificationManager: NotificationManager
    ) : WorkerFactory() {

        override fun createWorker(
            context: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker =
            RefreshLibrariesWorker(
                context,
                workerParameters,
                libraryRepository,
                connectivityChecker,
                notificationBodyMaker,
                notificationManager
            )

    }
}