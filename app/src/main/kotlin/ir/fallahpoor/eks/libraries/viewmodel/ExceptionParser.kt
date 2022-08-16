package ir.fallahpoor.eks.libraries.viewmodel

import android.content.Context
import ir.fallahpoor.eks.R
import java.io.IOException
import javax.inject.Inject

class ExceptionParser @Inject constructor(context: Context) {

    val INTERNET_NOT_CONNECTED =
        context.getString(R.string.internet_not_connected)
    val SOMETHING_WENT_WRONG =
        context.getString(R.string.something_went_wrong)

    fun getMessage(t: Throwable): String {
        return when (t) {
            is IOException -> INTERNET_NOT_CONNECTED
            else -> SOMETHING_WENT_WRONG
        }
    }

}