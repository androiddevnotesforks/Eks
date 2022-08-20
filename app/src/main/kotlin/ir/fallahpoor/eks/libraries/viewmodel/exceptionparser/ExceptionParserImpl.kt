package ir.fallahpoor.eks.libraries.viewmodel.exceptionparser

import android.content.Context
import ir.fallahpoor.eks.R
import java.io.IOException
import javax.inject.Inject

class ExceptionParserImpl @Inject constructor(context: Context) : ExceptionParser {

    val INTERNET_NOT_CONNECTED =
        context.getString(R.string.internet_not_connected)
    val SOMETHING_WENT_WRONG =
        context.getString(R.string.something_went_wrong)

    override fun getMessage(t: Throwable): String = when (t) {
        is IOException -> INTERNET_NOT_CONNECTED
        else -> SOMETHING_WENT_WRONG
    }

}