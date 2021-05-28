package org.fahrii.mangashi.core.data.mapper

import org.fahrii.mangashi.core.data.local.entity.MangaEntity
import org.fahrii.mangashi.core.data.remote.response.MangaDetailResponse
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.utils.EntityMapper
import org.fahrii.mangashi.core.utils.ResponseMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaMapper @Inject constructor() : EntityMapper<MangaEntity, Manga>,
    ResponseMapper<MangaDetailResponse, MangaEntity> {
    override fun mapFromEntity(entity: MangaEntity): Manga = Manga(
        entity.id,
        entity.title,
        entity.thumbnail,
        entity.type,
        entity.author,
        entity.status,
        entity.synopsis,
        entity.favorite
    )

    override fun mapToEntity(model: Manga): MangaEntity = MangaEntity(
        model.id,
        model.title,
        model.thumbnail,
        model.type,
        model.author,
        model.status,
        model.synopsis,
        model.favorite
    )

    override fun mapFromEntities(entities: List<MangaEntity>): List<Manga> =
        entities.map { mapFromEntity(it) }

    override fun mapToEntities(models: List<Manga>): List<MangaEntity> =
        models.map { mapToEntity(it) }

    override fun mapFromResponse(response: MangaDetailResponse): MangaEntity =
        MangaEntity(
            response.id,
            response.title,
            response.thumbnail,
            response.type,
            response.author,
            response.status,
            response.synopsis,
            false
        )
}