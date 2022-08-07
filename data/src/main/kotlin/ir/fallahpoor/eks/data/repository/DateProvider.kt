package ir.fallahpoor.eks.data.repository

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateProvider @Inject constructor() {

    fun getCurrentDate(simpleDateFormat: SimpleDateFormat): String = simpleDateFormat.format(Date())

}