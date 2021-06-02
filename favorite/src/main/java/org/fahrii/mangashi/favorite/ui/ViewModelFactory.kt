package org.fahrii.mangashi.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase
import org.fahrii.mangashi.favorite.ui.home.FavoriteHomeViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val mangaUseCase: MangaUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FavoriteHomeViewModel::class.java) -> FavoriteHomeViewModel(
                mangaUseCase
            ) as T
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}