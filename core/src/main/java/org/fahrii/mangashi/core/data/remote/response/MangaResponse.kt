package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    @SerializedName("endpoint")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumb")
    val thumbnail: String,
    @SerializedName("type")
    val type: String
)
