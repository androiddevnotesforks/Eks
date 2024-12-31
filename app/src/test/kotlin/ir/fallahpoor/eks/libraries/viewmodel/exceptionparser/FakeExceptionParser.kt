package ir.fallahpoor.eks.libraries.viewmodel.exceptionparser

class FakeExceptionParser : ExceptionParser {
    companion object {
        const val ERROR_MESSAGE = "Something went wrong"
    }

    override fun getMessage(t: Throwable): String = ERROR_MESSAGE
}