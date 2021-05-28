package org.fahrii.mangashi.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Manga(
    val id: String,
    val title: String,
    val thumbnail: String,
    val type: String,
    val author: String?,
    val status: String?,
    val synopsis: String?,
    var favorite: Boolean = false
): Parcelable
