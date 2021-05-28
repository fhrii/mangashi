package org.fahrii.mangashi.ui.detail

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val mangaUseCase: MangaUseCase) : ViewModel() {
    fun getManga(id: String) = LiveDataReactiveStreams.fromPublisher(mangaUseCase.getManga(id))
    fun setFavorite(manga: Manga, state: Boolean) = mangaUseCase.setFavoriteManga(manga, state)
}