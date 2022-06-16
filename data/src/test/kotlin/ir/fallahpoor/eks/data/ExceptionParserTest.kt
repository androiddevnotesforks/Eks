package ir.fallahpoor.eks.data

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class ExceptionParserTest {

    private lateinit var exceptionParser: ExceptionParser

    @Before
    fun setup() {
        exceptionParser = ExceptionParser(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun test_getMessage_for_IOException() {

        // Given
        val throwable: Throwable = IOException()

        // When
        val message = exceptionParser.getMessage(throwable)

        // Then
        Truth.assertThat(message).isEqualTo(exceptionParser.INTERNET_NOT_CONNECTED)

    }

    @Test
    fun test_getMessage_for_other_exceptions() {

        // Given any exception other than than HttpException and IOException
        val throwable: Throwable = ArithmeticException()

        // When
        val message = exceptionParser.getMessage(throwable)

        // Then
        Truth.assertThat(message).isEqualTo(exceptionParser.SOMETHING_WENT_WRONG)

    }

}