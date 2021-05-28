package org.fahrii.mangashi.ui.home.main

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.fahrii.mangashi.core.domain.usecase.MangaUseCase
import javax.inject.Inject

@HiltViewModel
class MainHomeViewModel @Inject constructor(private val mangaUseCase: MangaUseCase) : ViewModel() {
    fun getAllManga() = LiveDataReactiveStreams.fromPublisher(mangaUseCase.getAllManga())
}