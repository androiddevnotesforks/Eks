package ir.fallahpoor.eks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.fallahpoor.eks.data.database.DatabaseContract
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.database.LibraryDatabase
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.data.repository.LibraryRepositoryImpl
import ir.fallahpoor.eks.data.storage.LocalStorage
import ir.fallahpoor.eks.data.storage.Storage
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.ExceptionParser
import ir.fallahpoor.eks.libraries.viewmodel.exceptionparser.ExceptionParserImpl
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

// TODO Move all the data-related classes to the 'data' module
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideDateFormat(): DateFormat = SimpleDateFormat("MMM dd HH:mm", Locale.US)

    @Provides
    @Singleton
    fun provideStorage(localStorage: LocalStorage): Storage = localStorage

    @Provides
    @Singleton
    fun provideDataStore(context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("settings")
            }
        )

    @Provides
    fun provideLibraryRepository(libraryRepositoryImpl: LibraryRepositoryImpl): LibraryRepository =
        libraryRepositoryImpl

    @Provides
    @Singleton
    fun provideLibraryDao(libraryDatabase: LibraryDatabase): LibraryDao =
        libraryDatabase.libraryDao()

    @Provides
    @Singleton
    fun provideLibraryDatabase(context: Context): LibraryDatabase =
        Room.databaseBuilder(context, LibraryDatabase::class.java, DatabaseContract.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideExceptionParser(exceptionParserImpl: ExceptionParserImpl): ExceptionParser =
        exceptionParserImpl

}