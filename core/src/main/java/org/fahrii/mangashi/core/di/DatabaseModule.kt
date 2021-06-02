package org.fahrii.mangashi.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.fahrii.mangashi.core.BuildConfig
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
            .openHelperFactory(SupportFactory(SQLiteDatabase.getBytes(BuildConfig.SQLITE_PASSPHRASE.toCharArray())))
            .build()

    @Provides
    fun provideMangaDao(database: MangaDatabase): MangaDao = database.mangaDao()
}