package org.fahrii.mangashi.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fahrii.mangashi.core.data.MangaRepositoryImpl
import org.fahrii.mangashi.core.domain.repository.MangaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideRepository(mangaRepositoryImpl: MangaRepositoryImpl): MangaRepository
}