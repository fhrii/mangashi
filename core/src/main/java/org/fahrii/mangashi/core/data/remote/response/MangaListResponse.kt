package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaListResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("manga_list")
    val mangaList: List<MangaResponse>
)
