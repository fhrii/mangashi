package org.fahrii.mangashi.core.data.local

import io.reactivex.Completable
import org.fahrii.mangashi.core.data.local.entity.MangaEntity
import org.fahrii.mangashi.core.data.local.room.MangaDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val mangaDao: MangaDao) {
    fun getAllManga() = mangaDao.getAllManga()
    fun getFavoriteManga() = mangaDao.getFavoriteManga()
    fun getManga(id: String) = mangaDao.getManga(id)
    fun insertManga(manga: MangaEntity) = mangaDao.insertManga(manga)
    fun insertMangaList(mangaList: List<MangaEntity>) = mangaDao.insertMangaList(mangaList)
    fun setFavoriteManga(manga: MangaEntity, state: Boolean): Completable {
        manga.favorite = state
        return mangaDao.updateManga(manga)
    }
}