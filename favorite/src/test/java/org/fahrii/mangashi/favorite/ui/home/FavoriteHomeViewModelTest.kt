package org.fahrii.mangashi.favorite.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.BehaviorSubject
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.domain.usecase.MangaUseCaseImpl
import org.fahrii.mangashi.favorite.utils.DummyData
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
class FavoriteHomeViewModelTest {
    private val mangaList = DummyData.getMangaList()
    private lateinit var viewModel: FavoriteHomeViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mangaUseCaseImpl: MangaUseCaseImpl

    @Mock
    private lateinit var observer: Observer<ResultState<List<Manga>>>

    @Before
    fun setUp() {
        viewModel = FavoriteHomeViewModel(mangaUseCaseImpl)
    }

    @Test
    fun `getting favorite manga test`() {
        val resultState = ResultState.SUCCESS(mangaList)
        val result = BehaviorSubject.create<ResultState<List<Manga>>>()
        result.onNext(resultState)

        assertEquals(resultState, result.value)

        `when`(mangaUseCaseImpl.getFavoriteManga())
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        viewModel.getFavoriteManga().observeForever(observer)
        verify(mangaUseCaseImpl).getFavoriteManga()
        verify(observer).onChanged(resultState)
    }
}