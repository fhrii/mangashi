package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaResponse(
    @SerializedName("endpoint")
    val id: String,
    val title: String,
    @SerializedName("thumb")
    val thumbnail: String,
    val type: String
)
