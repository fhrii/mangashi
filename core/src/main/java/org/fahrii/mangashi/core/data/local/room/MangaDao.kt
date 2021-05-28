package org.fahrii.mangashi.core.data.local.room

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import org.fahrii.mangashi.core.data.local.entity.MangaEntity

@Dao
interface MangaDao {
    @Query("SELECT * FROM manga")
    fun getAllManga(): Flowable<List<MangaEntity>>

    @Query("SELECT * FROM manga WHERE favorite = 1")
    fun getFavoriteManga(): Flowable<List<MangaEntity>>

    @Query("SELECT * FROM manga WHERE id = :id")
    fun getManga(id: String): Flowable<MangaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertManga(manga: MangaEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMangaList(mangaList: List<MangaEntity>): Completable

    @Update
    fun updateManga(manga: MangaEntity): Completable
}