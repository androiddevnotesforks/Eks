package ir.fallahpoor.eks.libraries.viewmodel.exceptionparser

interface ExceptionParser {
    fun getMessage(t: Throwable): String
}