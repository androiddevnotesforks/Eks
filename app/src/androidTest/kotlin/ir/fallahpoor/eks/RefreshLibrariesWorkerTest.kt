//package ir.fallahpoor.eks
//
//import android.content.Context
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.test.core.app.ApplicationProvider
//import androidx.work.ListenableWorker
//import androidx.work.WorkerFactory
//import androidx.work.WorkerParameters
//import androidx.work.testing.TestListenableWorkerBuilder
//import com.google.common.truth.Truth
//import ir.fallahpoor.eks.common.NotificationManager
//import ir.fallahpoor.eks.data.ConnectivityChecker
//import ir.fallahpoor.eks.data.repository.LibraryRepository
//import ir.fallahpoor.eks.worker.NotificationBodyMaker
//import ir.fallahpoor.eks.worker.RefreshLibrariesWorker
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.ArgumentMatchers.anyList
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.junit.MockitoJUnitRunner
//
//@ExperimentalAnimationApi
//@RunWith(MockitoJUnitRunner::class)
//class RefreshLibrariesWorkerTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var connectivityChecker: ConnectivityChecker
//
//    @Mock
//    private lateinit var notificationBodyMaker: NotificationBodyMaker
//
//    @Mock
//    private lateinit var notificationManager: NotificationManager
//
//    private lateinit var libraryRepository: FakeLibraryRepository
//
//    private lateinit var worker: RefreshLibrariesWorker
//
//    private val context: Context = ApplicationProvider.getApplicationContext()
//
//    @Before
//    fun runBeforeEachTest() {
//        libraryRepository = FakeLibraryRepository()
//        worker = createWorker(libraryRepository)
//    }
//
//    private fun createWorker(libraryRepository: LibraryRepository): RefreshLibrariesWorker {
//        return TestListenableWorkerBuilder<RefreshLibrariesWorker>(context)
//            .setWorkerFactory(
//                CustomWorkerFactory(
//                    libraryRepository,
//                    connectivityChecker,
//                    notificationBodyMaker,
//                    notificationManager
//                )
//            )
//            .build()
//    }
//
//    @Test
//    fun doWork_should_return_Retry_when_network_is_not_reachable() = runBlockingTest {
//
//        // Given
//        Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(false)
//
//        // When
//        val result = worker.doWork()
//
//        // Then
//        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
//        Mockito.verify(connectivityChecker).isNetworkReachable()
//        Mockito.verifyNoInteractions(notificationManager)
//        Mockito.verifyNoInteractions(notificationBodyMaker)
//        Truth.assertThat(libraryRepository.clearCalled).isTrue()
//
//    }
//
//    @Test
//    fun doWork_should_return_Retry_when_an_exception_occurs() = runBlockingTest {
//
//        // Given
//        Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(true)
//        libraryRepository.throwException = true
//
//        // When
//        val result = worker.doWork()
//
//        // Then
//        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Retry::class.java)
//        Mockito.verifyNoInteractions(notificationManager)
//        Mockito.verifyNoInteractions(notificationBodyMaker)
//        Truth.assertThat(libraryRepository.clearCalled).isTrue()
//
//    }
//
//    @Test
//    fun no_notification_is_displayed_when_there_is_no_update() =
//        runBlockingTest {
//
//            // Given
//            Mockito.`when`(connectivityChecker.isNetworkReachable()).thenReturn(true)
//            libraryRepository.updatesAvailable = false
//
//            // When
//            val result = worker.doWork()
//
//            // Then
//            Mockito.verifyNoInteractions(notificationManager)
//            Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)
//            Truth.assertThat(libraryRepository.clearCalled).isTrue()
//
//        }
//
//    @Test
//    fun a_notification_is_displayed_when_there_are_updates() = runBlockingTest {
//
//        // Given
//        Mockito.`when`(connectivityChecker.isNetworkReachable())
//            .thenReturn(true)
//        Mockito.`when`(notificationBodyMaker.makeBody(anyList(), anyList()))
//            .thenReturn("X")
//        libraryRepository.updatesAvailable = true
//
//        // When
//        val result = worker.doWork()
//
//        // Then
//        Mockito.verify(notificationManager)
//            .showNotification(context.getString(R.string.notification_title), "X")
//        Mockito.verify(notificationBodyMaker)
//            .makeBody(TestData.libraries, TestData.refreshedLibraries)
//        Truth.assertThat(result).isInstanceOf(ListenableWorker.Result.Success::class.java)
//        Truth.assertThat(libraryRepository.clearCalled).isTrue()
//
//    }
//
//    private class CustomWorkerFactory(
//        private val libraryRepository: LibraryRepository,
//        private val connectivityChecker: ConnectivityChecker,
//        private val notificationBodyMaker: NotificationBodyMaker,
//        private val notificationManager: NotificationManager
//    ) : WorkerFactory() {
//
//        override fun createWorker(
//            context: Context,
//            workerClassName: String,
//            workerParameters: WorkerParameters
//        ): ListenableWorker =
//            RefreshLibrariesWorker(
//                context,
//                workerParameters,
//                libraryRepository,
//                connectivityChecker,
//                notificationBodyMaker,
//                notificationManager
//            )
//
//    }
//}