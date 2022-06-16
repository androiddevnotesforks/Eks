package ir.fallahpoor.eks.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ir.fallahpoor.eks.data.repository.LibraryRepository
import ir.fallahpoor.eks.testfakes.FakeLibraryRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {

    @Provides
    fun provideLibraryRepository(): LibraryRepository {
        return FakeLibraryRepository()
    }

}