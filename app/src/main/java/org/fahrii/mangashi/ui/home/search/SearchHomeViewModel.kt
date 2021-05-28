package org.fahrii.mangashi.ui.home.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase
import javax.inject.Inject

@HiltViewModel
class SearchHomeViewModel @Inject constructor(private val mangaUseCase: MangaUseCase) :
    ViewModel() {
    fun searchManga(query: String) = mangaUseCase.searchManga(query)
    fun insertManga(manga: Manga) = mangaUseCase.insertManga(manga)
}