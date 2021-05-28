package org.fahrii.mangashi.core.data.mapper

import org.fahrii.mangashi.core.data.local.entity.MangaEntity
import org.fahrii.mangashi.core.data.remote.response.MangaListResponse
import org.fahrii.mangashi.core.utils.ResponseMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaListMapper @Inject constructor() : ResponseMapper<MangaListResponse, List<MangaEntity>> {
    override fun mapFromResponse(response: MangaListResponse): List<MangaEntity> =
        response.mangaList.map { manga ->
            MangaEntity(
                manga.id,
                manga.title,
                manga.thumbnail,
                manga.type,
                null,
                null,
                null,
                false
            )
        }
}