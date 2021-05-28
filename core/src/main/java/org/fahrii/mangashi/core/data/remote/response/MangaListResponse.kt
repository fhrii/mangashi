package org.fahrii.mangashi.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MangaListResponse(
    val status: Boolean,
    val message: String,
    @SerializedName("manga_list")
    val mangaList: List<MangaResponse>
)
