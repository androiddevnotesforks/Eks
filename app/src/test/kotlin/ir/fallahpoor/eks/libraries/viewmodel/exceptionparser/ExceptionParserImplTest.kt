package ir.fallahpoor.eks.libraries.viewmodel.exceptionparser

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ExceptionParserImplTest {

    private lateinit var exceptionParser: ExceptionParserImpl

    @Before
    fun setup() {
        exceptionParser = ExceptionParserImpl(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun `correct message is returned when the exception is a IOException`() {

        // Given
        val throwable: Throwable = IOException()

        // When
        val message = exceptionParser.getMessage(throwable)

        // Then
        Truth.assertThat(message).isEqualTo(exceptionParser.INTERNET_NOT_CONNECTED)

    }

    @Test
    fun `correct message is returned when the exception is anything but IOException`() {

        // Given any exception other than than HttpException and IOException
        val throwable: Throwable = ArithmeticException()

        // When
        val message = exceptionParser.getMessage(throwable)

        // Then
        Truth.assertThat(message).isEqualTo(exceptionParser.SOMETHING_WENT_WRONG)

    }

}