package org.fahrii.mangashi.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.fahrii.mangashi.core.data.local.room.MangaDao
import org.fahrii.mangashi.core.data.local.room.MangaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MangaDatabase =
        Room.databaseBuilder(context, MangaDatabase::class.java, "manga_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMangaDao(database: MangaDatabase): MangaDao = database.mangaDao()
}