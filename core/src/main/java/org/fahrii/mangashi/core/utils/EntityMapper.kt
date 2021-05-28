package org.fahrii.mangashi.core.utils

interface EntityMapper<Entity, Model> {
    fun mapFromEntity(entity: Entity): Model
    fun mapToEntity(model: Model): Entity
    fun mapFromEntities(entities: List<Entity>): List<Model>
    fun mapToEntities(models: List<Model>): List<Entity>
}