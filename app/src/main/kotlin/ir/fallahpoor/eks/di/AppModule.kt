package ir.fallahpoor.eks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.ExceptionParser
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.ExceptionParserImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideExceptionParser(exceptionParserImpl: ExceptionParserImpl): ExceptionParser =
        exceptionParserImpl

}