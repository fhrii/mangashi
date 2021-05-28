package org.fahrii.mangashi.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase
import org.fahrii.mangashi.core.domain.usecase.MangaUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideMangaUseCase(mangaUseCaseImpl: MangaUseCaseImpl): MangaUseCase
}