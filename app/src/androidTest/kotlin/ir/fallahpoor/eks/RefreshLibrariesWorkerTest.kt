package ir.fallahpoor.eks

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth
import io.mockk.Called
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import ir.fallahpoor.eks.common.NotificationManager
import ir.fallahpoor.eks.commontest.FakeLibraryRepository
import ir.fallahpoor.eks.data.ConnectivityChecker
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.worker.NotificationBodyMaker
import ir.fallahpoor.eks.worker.RefreshLibrariesWorker
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RefreshLibrariesWorkerTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val connectivityChecker: ConnectivityChecker = mockk()
    private val notificationBodyMaker: NotificationBodyMaker = mockk()
    private val notificationManager: NotificationManager = mockk()

    private lateinit var libraryRepository: FakeLibraryRepository
    private lateinit var worker: RefreshLibrariesWorker
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun runBeforeEachTest() {
        libraryRepository = FakeLibraryRepository()
        worker = createWorker(libraryRepository)
    }

    private fun createWorker(libraryRepository: LibraryRepository): RefreshLibrariesWorker =
        TestListenableWorkerBuilder<RefreshLibrariesWorker>(context).setWorkerFactory(
            CustomWorkerFactory(
                libraryRepository,
                connectivityChecker,
                notificationBodyMaker,
                notificationManager
            )
        ).build()

    @Test
    fun doWork_should_return_Retry_when_network_is_not_reachable() = runTest {
        // Given
        coEvery { connectivityChecker.isNetworkReachable() } returns false

        // When
        val result = worker.doWork()

        // Then
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
        coVerify { connectivityChecker.isNetworkReachable() }
        verify { notificationManager wasNot Called }
        verify { notificationBodyMaker wasNot Called }
    }

    @Test
    fun doWork_should_return_Retry_when_an_exception_occurs() = runTest {
        // Given
        coEvery { connectivityChecker.isNetworkReachable() } returns true
        libraryRepository.throwException = true

        // When
        val result = worker.doWork()

        // Then
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
        verify { notificationManager wasNot Called }
        verify { notificationBodyMaker wasNot Called }
    }

    @Test
    fun no_notification_is_displayed_when_there_is_no_update() = runTest {
        // Given
        coEvery { connectivityChecker.isNetworkReachable() } returns true
        every { notificationBodyMaker.makeBody(any(), any()) } returns null
        every { notificationManager.showNotification(any(), any()) } just Runs
        libraryRepository.updateIsAvailable = false

        // When
        val result = worker.doWork()

        // Then
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)
        verify { notificationManager wasNot Called }
    }

    @Test
    fun a_notification_is_displayed_when_there_are_updates() = runTest {
        // Given
        coEvery { connectivityChecker.isNetworkReachable() } returns true
        every { notificationBodyMaker.makeBody(any(), any()) } returns "X"
        every { notificationManager.showNotification(any(), any()) } just Runs
        libraryRepository.updateIsAvailable = true

        // When
        val result = worker.doWork()

        // Then
        verify {
            notificationManager.showNotification(
                context.getString(R.string.notification_title),
                "X"
            )
        }
        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)
    }

    private class CustomWorkerFactory(
        private val libraryRepository: LibraryRepository,
        private val connectivityChecker: ConnectivityChecker,
        private val notificationBodyMaker: NotificationBodyMaker,
        private val notificationManager: NotificationManager
    ) : WorkerFactory() {
        override fun createWorker(
            context: Context, workerClassName: String, workerParameters: WorkerParameters
        ): ListenableWorker = RefreshLibrariesWorker(
            context,
            workerParameters,
            libraryRepository,
            connectivityChecker,
            notificationBodyMaker,
            notificationManager
        )
    }
}