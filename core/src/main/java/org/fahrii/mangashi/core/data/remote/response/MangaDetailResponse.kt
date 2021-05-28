package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaDetailResponse(
    @SerializedName("manga_endpoint")
    val id: String,
    val title: String,
    @SerializedName("thumb")
    val thumbnail: String,
    val type: String,
    val author: String,
    val status: String,
    val synopsis: String
)
