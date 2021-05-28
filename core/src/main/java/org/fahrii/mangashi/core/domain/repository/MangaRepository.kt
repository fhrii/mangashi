package org.fahrii.mangashi.core.domain.repository

import io.reactivex.Flowable
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga

interface MangaRepository {
    fun getAllManga(): Flowable<ResultState<List<Manga>>>
    fun getManga(id: String): Flowable<ResultState<Manga>>
    fun searchManga(query: String): Flowable<ResultState<List<Manga>>>
    fun getFavoriteManga(): Flowable<ResultState<List<Manga>>>
    fun setFavorite(manga: Manga, state: Boolean): Flowable<ResultState<Boolean>>
    fun insertManga(manga: Manga): Flowable<ResultState<Boolean>>
}