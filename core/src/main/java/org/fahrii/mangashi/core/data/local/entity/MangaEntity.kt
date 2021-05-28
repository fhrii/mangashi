package org.fahrii.mangashi.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val thumbnail: String,
    val type: String,
    val author: String?,
    val status: String?,
    val synopsis: String?,
    var favorite: Boolean = false,
)
