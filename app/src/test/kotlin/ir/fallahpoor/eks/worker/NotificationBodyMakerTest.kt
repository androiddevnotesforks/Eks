package ir.fallahpoor.eks.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import ir.fallahpoor.eks.R
import ir.fallahpoor.eks.TestData
import ir.fallahpoor.eks.data.entity.Library
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationBodyMakerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val notificationBodyMaker = NotificationBodyMaker(context, LibraryDiffer())

    @Test
    fun `when there is no update makeBody() should return null`() {

        // Given
        val oldLibraries: List<Library> = listOf(TestData.core, TestData.room)
        val refreshedLibraries: List<Library> = listOf(TestData.core, TestData.room)

        // When
        val body: String? = notificationBodyMaker.makeBody(
            oldLibraries = oldLibraries,
            refreshedLibraries = refreshedLibraries
        )

        // Then
        Truth.assertThat(body).isNull()

    }

    @Test
    fun new() {

        // Given
        val newLibrary: Library = TestData.room
        val oldLibraries: List<Library> = listOf(TestData.core)
        val refreshedLibraries: List<Library> = listOf(TestData.core, newLibrary)

        // When
        val body: String? = notificationBodyMaker.makeBody(
            oldLibraries = oldLibraries,
            refreshedLibraries = refreshedLibraries
        )

        // Then
        val expectedBody = context.getString(R.string.new_libraries) + "\n" + newLibrary.name
        Truth.assertThat(body).isEqualTo(expectedBody)

    }

    @Test
    fun removed() {

        // Given
        val removedLibrary: Library = TestData.room
        val oldLibraries: List<Library> = listOf(TestData.core, removedLibrary)
        val refreshedLibraries: List<Library> = listOf(TestData.core)

        // When
        val body: String? = notificationBodyMaker.makeBody(
            oldLibraries = oldLibraries,
            refreshedLibraries = refreshedLibraries
        )

        // Then
        val expectedBody =
            context.getString(R.string.removed_libraries) + "\n" + removedLibrary.name
        Truth.assertThat(body).isEqualTo(expectedBody)

    }

    @Test
    fun updated() {

        // Given
        val oldLibraries: List<Library> = listOf(TestData.activityOld, TestData.core, TestData.room)
        val refreshedLibraries: List<Library> =
            listOf(TestData.activityNew, TestData.core, TestData.room)

        // When
        val actualBody: String? = notificationBodyMaker.makeBody(
            oldLibraries = oldLibraries,
            refreshedLibraries = refreshedLibraries
        )

        // Then
        Truth.assertThat(actualBody).isEqualTo("")

    }

}