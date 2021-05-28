package org.fahrii.mangashi.core.data

import android.annotation.SuppressLint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.fahrii.mangashi.core.data.local.LocalDataSource
import org.fahrii.mangashi.core.data.mapper.MangaListMapper
import org.fahrii.mangashi.core.data.mapper.MangaMapper
import org.fahrii.mangashi.core.data.remote.RemoteDataSource
import org.fahrii.mangashi.core.data.remote.network.ApiResponse
import org.fahrii.mangashi.core.data.remote.response.MangaDetailResponse
import org.fahrii.mangashi.core.data.remote.response.MangaListResponse
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.repository.MangaRepository
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val mangaMapper: MangaMapper,
    private val mangaListMapper: MangaListMapper,
) : MangaRepository {
    override fun getAllManga(): Flowable<ResultState<List<Manga>>> =
        object : NetworkBoundResource<List<Manga>, MangaListResponse>() {
            override fun loadFromDB(): Flowable<List<Manga>> =
                localDataSource.getAllManga().map { mangaMapper.mapFromEntities(it) }

            override fun shouldFetch(data: List<Manga>?): Boolean = data == null || data.isEmpty()

            override fun createCall(): Flowable<ApiResponse<MangaListResponse>> =
                remoteDataSource.getPopularManga()

            override fun saveCallResult(data: MangaListResponse) {
                localDataSource.insertMangaList(mangaListMapper.mapFromResponse(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
        }.asFlowable()

    override fun getManga(id: String): Flowable<ResultState<Manga>> =
        object : NetworkBoundResource<Manga, MangaDetailResponse>() {
            override fun loadFromDB(): Flowable<Manga> =
                localDataSource.getManga(id).map { mangaMapper.mapFromEntity(it) }

            override fun shouldFetch(data: Manga?): Boolean {
                return data?.author == null
            }

            override fun createCall(): Flowable<ApiResponse<MangaDetailResponse>> =
                remoteDataSource.getManga(id)

            override fun saveCallResult(data: MangaDetailResponse) {
                localDataSource.insertManga(mangaMapper.mapFromResponse(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
        }.asFlowable()

    @SuppressLint("CheckResult")
    override fun searchManga(query: String): Flowable<ResultState<List<Manga>>> {
        val resultData = BehaviorSubject.create<ResultState<List<Manga>>>()
        resultData.onNext(ResultState.LOADING)

        val remoteData = remoteDataSource.searchManga(query)
        remoteData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { remoteData.unsubscribeOn(Schedulers.io()) }
            .subscribe({ response ->
                when (response) {
                    is ApiResponse.SUCCESS -> {
                        val mangaEntities = mangaListMapper.mapFromResponse(response.data)
                        val manga = mangaMapper.mapFromEntities(mangaEntities)
                        resultData.onNext(ResultState.SUCCESS(manga))
                    }
                    is ApiResponse.EMPTY -> {
                        val mangaEntities = mangaListMapper.mapFromResponse(response.data)
                        val manga = mangaMapper.mapFromEntities(mangaEntities)
                        resultData.onNext(ResultState.SUCCESS(manga))
                    }
                    is ApiResponse.ERROR -> {
                        resultData.onNext(ResultState.ERROR(response.errMessage))
                    }
                }
            }, { error ->
                resultData.onNext(ResultState.ERROR(error.message.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    override fun getFavoriteManga(): Flowable<ResultState<List<Manga>>> {
        val resultData = BehaviorSubject.create<ResultState<List<Manga>>>()
        resultData.onNext(ResultState.LOADING)

        val localData = localDataSource.getFavoriteManga()
        localData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { localData.unsubscribeOn(Schedulers.io()) }
            .subscribe({ mangaEntities ->
                val mangas = mangaMapper.mapFromEntities(mangaEntities)
                resultData.onNext(ResultState.SUCCESS(mangas))
            }, { error ->
                resultData.onNext(ResultState.ERROR(error.message.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    override fun setFavorite(manga: Manga, state: Boolean): Flowable<ResultState<Boolean>> {
        val resultData = BehaviorSubject.create<ResultState<Boolean>>()
        resultData.onNext(ResultState.LOADING)

        val mangaEntity = mangaMapper.mapToEntity(manga)
        val localData = localDataSource.setFavoriteManga(mangaEntity, state)
        localData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { localData.unsubscribeOn(Schedulers.io()) }
            .subscribe({
                resultData.onNext(ResultState.SUCCESS(true))
            }, { error ->
                resultData.onNext(ResultState.ERROR(error.message.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    override fun insertManga(manga: Manga): Flowable<ResultState<Boolean>> {
        val resultData = BehaviorSubject.create<ResultState<Boolean>>()
        resultData.onNext(ResultState.LOADING)

        val mangaEntity = mangaMapper.mapToEntity(manga)
        val localData = localDataSource.insertManga(mangaEntity)
        localData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { localData.unsubscribeOn(Schedulers.io()) }
            .subscribe({
                resultData.onNext(ResultState.SUCCESS(true))
            }, { error ->
                resultData.onNext(ResultState.ERROR(error.message.toString()))
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }
}