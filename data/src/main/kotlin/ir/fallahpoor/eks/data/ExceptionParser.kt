package ir.fallahpoor.eks.data

import android.content.Context
import java.io.IOException
import javax.inject.Inject

class ExceptionParser @Inject constructor(context: Context) {

    val INTERNET_NOT_CONNECTED =
        context.getString(ir.fallahpoor.eks.data.R.string.internet_not_connected)
    val SOMETHING_WENT_WRONG =
        context.getString(ir.fallahpoor.eks.data.R.string.something_went_wrong)

    fun getMessage(t: Throwable): String {
        return when (t) {
            is IOException -> INTERNET_NOT_CONNECTED
            else -> SOMETHING_WENT_WRONG
        }
    }

}