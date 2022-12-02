package ir.fallahpoor.eks.data.repository

import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

class DateProvider @Inject constructor(private val dateFormat: DateFormat) {
    fun getCurrentDate(): String = dateFormat.format(Date())
}