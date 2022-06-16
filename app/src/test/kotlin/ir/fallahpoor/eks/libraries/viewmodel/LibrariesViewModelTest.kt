//package ir.fallahpoor.eks.libraries.viewmodel
//
//import android.content.Intent
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.core.app.ApplicationProvider
//import com.google.common.truth.Truth
//import ir.fallahpoor.eks.TestData
//import ir.fallahpoor.eks.data.ExceptionParser
//import ir.fallahpoor.eks.data.SortOrder
//import ir.fallahpoor.eks.fakes.FakeStorage
//import ir.fallahpoor.eks.libraries.Event
//import ir.fallahpoor.eks.libraries.ui.LibrariesListState
//import ir.fallahpoor.eks.testfakes.FakeLibraryRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.*
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//
//@Config(manifest = Config.NONE)
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(RobolectricTestRunner::class)
//class LibrariesViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var librariesViewModel: LibrariesViewModel
//    private lateinit var fakeLibraryRepository: FakeLibraryRepository
//    private lateinit var fakeStorage: FakeStorage
//
//    @Before
//    fun runBeforeEachTest() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
//        fakeLibraryRepository = FakeLibraryRepository()
//        fakeStorage = FakeStorage()
//        librariesViewModel = LibrariesViewModel(
//            libraryRepository = fakeLibraryRepository,
//            exceptionParser = ExceptionParser(ApplicationProvider.getApplicationContext())
//        )
//    }
//
//    @After
//    fun runAfterEachTest() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `getLibraries() should return all libraries sorted by given order`() = runTest {
//
//        // Given
//        val expectedLibraries = fakeLibraryRepository.getLibraries()
//            .sortedByDescending { it.name }
//
//        // When
//        librariesViewModel.handleIntent(Intent.get())
//
//        // Then
//        librariesViewModel.librariesListState.observeForever {}
//        val librariesListState: LibrariesListState? =
//            librariesViewModel.librariesListState.value
//        val librariesLoadedState = librariesListState as LibrariesListState.LibrariesLoaded
//        Truth.assertThat(librariesLoadedState.libraries)
//            .isEqualTo(expectedLibraries)
//
//    }
//
//    @Test
//    fun `getLibraries() should return all matched libraries sorted by given order`() = runTest {
//
//        // Given
//        val searchQuery = "ko"
//        val expectedLibraries = fakeLibraryRepository.getLibraries().filter {
//            it.name.contains(searchQuery, ignoreCase = true)
//        }.sortedByDescending {
//            it.name
//        }
//
//        // When
//        librariesViewModel.getLibraries(sortOrder = SortOrder.Z_TO_A, searchQuery = searchQuery)
//
//        // Then
//        librariesViewModel.librariesListState.observeForever {}
//        val librariesListState: LibrariesListState? =
//            librariesViewModel.librariesListState.value
//        val librariesLoadedState = librariesListState as LibrariesListState.LibrariesLoaded
//        Truth.assertThat(librariesLoadedState.libraries)
//            .isEqualTo(expectedLibraries)
//
//    }
//
//    @Test
//    fun `pin library`() = runTest {
//        // Assert that the state becomes loading
//        // Then it becomes error if something goes wrong
//        // or it becomes Loaded if everything goes fine
//        // Also assert that the library is correctly pinned
//    }
//
//    @Test
//    fun `unpin library unpins the library and updates the state correctly`() = runTest {
//
//        // When
//        librariesViewModel.handleEvent(Event.PinLibrary(library = TestData.core, pin = false))
//
//        // Then
//        val library = fakeLibraryRepository.getLibraries().first { it.name == TestData.core.name }
//        Truth.assertThat(library.pinned).isEqualTo(0)
//
//    }
//
//}