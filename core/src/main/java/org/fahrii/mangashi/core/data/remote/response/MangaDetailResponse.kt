package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaDetailResponse(
    @SerializedName("manga_endpoint")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumb")
    val thumbnail: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("synopsis")
    val synopsis: String
)
