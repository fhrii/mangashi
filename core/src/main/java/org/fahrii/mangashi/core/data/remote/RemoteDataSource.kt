package org.fahrii.mangashi.core.data.remote

import android.annotation.SuppressLint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.fahrii.mangashi.core.data.remote.network.ApiResponse
import org.fahrii.mangashi.core.data.remote.network.ApiService
import org.fahrii.mangashi.core.data.remote.response.MangaDetailResponse
import org.fahrii.mangashi.core.data.remote.response.MangaListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    @SuppressLint("CheckResult")
    fun getPopularManga(): Flowable<ApiResponse<MangaListResponse>> {
        val resultData = PublishSubject.create<ApiResponse<MangaListResponse>>()

        val client = apiService.getPopularManga()
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doFinally { client.unsubscribeOn(Schedulers.computation()) }
            .subscribe({ response ->
                if (!response.status) {
                    resultData.onNext(ApiResponse.ERROR("Server Error"))
                    return@subscribe
                }

                if (response.mangaList.isEmpty()) {
                    resultData.onNext(ApiResponse.EMPTY(response))
                    return@subscribe
                }

                resultData.onNext(ApiResponse.SUCCESS(response))
            }, { error ->
                resultData.onNext(ApiResponse.ERROR(error.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    fun getManga(id: String): Flowable<ApiResponse<MangaDetailResponse>> {
        val resultData = PublishSubject.create<ApiResponse<MangaDetailResponse>>()

        val client = apiService.getManga(id)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doFinally { client.unsubscribeOn(Schedulers.computation()) }
            .subscribe({ response ->
                resultData.onNext(ApiResponse.SUCCESS(response))
            }, { error ->
                resultData.onNext(ApiResponse.ERROR(error.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    fun searchManga(query: String): Flowable<ApiResponse<MangaListResponse>> {
        val resultData = PublishSubject.create<ApiResponse<MangaListResponse>>()

        val client = apiService.searchManga(query)
        client.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doFinally { client.unsubscribeOn(Schedulers.computation()) }
            .subscribe({ response ->
                if (!response.status) {
                    resultData.onNext(ApiResponse.ERROR("Server Error"))
                    return@subscribe
                }

                if (response.mangaList.isEmpty()) {
                    resultData.onNext(ApiResponse.EMPTY(response))
                    return@subscribe
                }

                resultData.onNext(ApiResponse.SUCCESS(response))
            }, { error ->
                resultData.onNext(ApiResponse.ERROR(error.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }
}