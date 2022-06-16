package ir.fallahpoor.eks.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.fallahpoor.eks.data.database.DatabaseContract
import ir.fallahpoor.eks.data.database.LibraryDao
import ir.fallahpoor.eks.data.database.LibraryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideLibraryDao(libraryDatabase: LibraryDatabase): LibraryDao =
        libraryDatabase.libraryDao()

    @Provides
    @Singleton
    fun provideLibraryDatabase(context: Context): LibraryDatabase =
        Room.databaseBuilder(context, LibraryDatabase::class.java, DatabaseContract.DATABASE_NAME)
            .build()

}