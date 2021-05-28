package org.fahrii.mangashi.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {
    fun mangaUseCase(): MangaUseCase
}