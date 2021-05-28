package org.fahrii.mangashi.favorite.ui.home

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase

class FavoriteHomeViewModel(private val mangaUseCase: MangaUseCase) : ViewModel() {
    fun getFavoriteManga() = LiveDataReactiveStreams.fromPublisher(mangaUseCase.getFavoriteManga())
}