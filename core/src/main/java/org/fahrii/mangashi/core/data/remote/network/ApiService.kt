package org.fahrii.mangashi.core.data.remote.network

import io.reactivex.Flowable
import org.fahrii.mangashi.core.data.remote.response.MangaDetailResponse
import org.fahrii.mangashi.core.data.remote.response.MangaListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("manga/popular/1")
    fun getPopularManga(): Flowable<MangaListResponse>

    @GET("manga/detail/{id}")
    fun getManga(@Path("id") id: String): Flowable<MangaDetailResponse>

    @GET("search/{query}")
    fun searchManga(@Path("query") query: String): Flowable<MangaListResponse>
}