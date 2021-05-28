package org.fahrii.mangashi.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.fahrii.mangashi.core.data.local.entity.MangaEntity

@Database(entities = [MangaEntity::class], version = 1, exportSchema = false)
abstract class MangaDatabase : RoomDatabase() {
    abstract fun mangaDao(): MangaDao
}