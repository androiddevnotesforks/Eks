package ir.fallahpoor.eks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.data.repository.LibraryRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideLibraryRepository(libraryRepositoryImpl: LibraryRepositoryImpl): LibraryRepository =
        libraryRepositoryImpl

}