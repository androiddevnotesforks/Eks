package ir.fallahpoor.eks.data.di

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
import ir.fallahpoor.eks.data.repository.storage.StorageRepository
import ir.fallahpoor.eks.data.repository.storage.StorageRepositoryImpl
import ir.fallahpoor.eks.data.storage.LocalStorage
import ir.fallahpoor.eks.data.storage.Storage
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Module {

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
    fun provideStorageRepository(storageRepositoryImpl: StorageRepositoryImpl): StorageRepository =
        storageRepositoryImpl

    @Provides
    @Singleton
    fun provideLibraryDao(libraryDatabase: LibraryDatabase): LibraryDao =
        libraryDatabase.libraryDao()

    @Provides
    @Singleton
    fun provideLibraryDatabase(context: Context): LibraryDatabase =
        Room.databaseBuilder(context, LibraryDatabase::class.java, DatabaseContract.DATABASE_NAME)
            .build()

}