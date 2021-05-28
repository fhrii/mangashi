package org.fahrii.mangashi.core.data

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.fahrii.mangashi.core.data.remote.network.ApiResponse

abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = BehaviorSubject.create<ResultState<ResultType>>()
    private val mCompositeDisposable = CompositeDisposable()

    init {
        result.onNext(ResultState.LOADING)

        @Suppress("LeakingThis")
        val dbSource = loadFromDB()
        val db = dbSource
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { dbSource.unsubscribeOn(Schedulers.io()) }
            .subscribe({ value ->
                if (shouldFetch(value)) fetchFromNetwork()
                else {
                    mCompositeDisposable.dispose()
                    result.onNext(ResultState.SUCCESS(value))
                }
            }, {
                mCompositeDisposable.dispose()
                result.onNext(ResultState.ERROR(it.message))
            })

        mCompositeDisposable.add(db)
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flowable<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun createCall(): Flowable<ApiResponse<RequestType>>

    protected abstract fun saveCallResult(data: RequestType)

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        val response = apiResponse
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .doOnComplete { mCompositeDisposable.dispose() }
            .subscribe { response ->
                when (response) {
                    is ApiResponse.SUCCESS -> {
                        saveCallResult(response.data)
                        val dbSource = loadFromDB()
                        dbSource
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.SUCCESS(it))
                            }, { error ->
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.ERROR(error.message))
                            })
                    }
                    is ApiResponse.EMPTY -> {
                        val dbSource = loadFromDB()
                        dbSource
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.SUCCESS(it))
                            }, { error ->
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.ERROR(error.message))
                            })
                    }
                    is ApiResponse.ERROR -> {
                        onFetchFailed()
                        val dbSource = loadFromDB()
                        dbSource
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.ERROR())
                            }, { error ->
                                dbSource.unsubscribeOn(Schedulers.io())
                                result.onNext(ResultState.ERROR(error.message))
                            })
                    }
                }
            }
        mCompositeDisposable.add(response)
    }

    fun asFlowable(): Flowable<ResultState<ResultType>> =
        result.toFlowable(BackpressureStrategy.BUFFER)
}