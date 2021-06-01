package org.fahrii.mangashi.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.usecase.MangaUseCaseImpl
import org.fahrii.mangashi.ui.utils.DummyData
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    private val manga = DummyData.getManga()
    private lateinit var viewModel: DetailViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mangaUseCaseImpl: MangaUseCaseImpl

    @Mock
    private lateinit var observer: Observer<ResultState<Manga>>

    @Before
    fun setUp() {
        viewModel = DetailViewModel(mangaUseCaseImpl)
    }

    @Test
    fun `get specific manga test`() {
        val query = "test/"
        val resultState = ResultState.SUCCESS(manga)
        val result = BehaviorSubject.create<ResultState<Manga>>()
        result.onNext(resultState)

        assertEquals(resultState, result.value)

        `when`(mangaUseCaseImpl.getManga(query))
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        viewModel.getManga(query).observeForever(observer)
        verify(mangaUseCaseImpl).getManga(query)
        verify(observer).onChanged(resultState)
    }

    @Test
    fun `set favorite manga test`() {
        val state = true
        val resultState = ResultState.SUCCESS(true)
        val result = BehaviorSubject.create<ResultState<Boolean>>()
        result.onNext(resultState)

        `when`(mangaUseCaseImpl.setFavoriteManga(manga, state))
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        val testObserver = viewModel.setFavorite(manga, state).test()
        testObserver.assertValue(resultState)
        testObserver.dispose()
    }
}