package org.fahrii.mangashi.core.domain.usecase

import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.repository.MangaRepository
import javax.inject.Inject

class MangaUseCaseImpl @Inject constructor(private val mangaRepository: MangaRepository) :
    MangaUseCase {
    override fun getAllManga() = mangaRepository.getAllManga()
    override fun getManga(id: String) = mangaRepository.getManga(id)
    override fun searchManga(query: String) = mangaRepository.searchManga(query)
    override fun getFavoriteManga() = mangaRepository.getFavoriteManga()
    override fun setFavoriteManga(manga: Manga, state: Boolean) =
        mangaRepository.setFavorite(manga, state)

    override fun insertManga(manga: Manga) = mangaRepository.insertManga(manga)
}