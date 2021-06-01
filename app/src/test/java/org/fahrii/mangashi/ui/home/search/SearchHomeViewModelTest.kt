package org.fahrii.mangashi.ui.home.search

import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.usecase.MangaUseCaseImpl
import org.fahrii.mangashi.ui.utils.DummyData
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchHomeViewModelTest {
    private val manga = DummyData.getManga()
    private val mangaList = DummyData.getMangaList()
    private lateinit var viewModel: SearchHomeViewModel

    @Mock
    private lateinit var mangaUseCaseImpl: MangaUseCaseImpl

    @Before
    fun setUp() {
        viewModel = SearchHomeViewModel(mangaUseCaseImpl)
    }

    @Test
    fun `searching manga test`() {
        val query = "test/"
        val resultState = ResultState.SUCCESS(mangaList)
        val result = BehaviorSubject.create<ResultState<List<Manga>>>()
        result.onNext(resultState)

        `when`(mangaUseCaseImpl.searchManga(query))
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        val testObserver = viewModel.searchManga(query).test()
        testObserver.assertValue(resultState)
        testObserver.dispose()
    }

    @Test
    fun `inserting manga test`() {
        val resultState = ResultState.SUCCESS(true)
        val result = BehaviorSubject.create<ResultState<Boolean>>()
        result.onNext(resultState)

        `when`(mangaUseCaseImpl.insertManga(manga))
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        val testObserver = viewModel.insertManga(manga).test()
        testObserver.assertValue(resultState)
        testObserver.dispose()
    }
}