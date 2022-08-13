package ir.fallahpoor.eks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ir.fallahpoor.eks.commontest.FakeLibraryRepository
import ir.fallahpoor.eks.data.repository.LibraryRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {

    @Provides
    fun provideLibraryRepository(): LibraryRepository = FakeLibraryRepository()

}