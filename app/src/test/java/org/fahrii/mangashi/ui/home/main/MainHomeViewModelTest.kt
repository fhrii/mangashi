package org.fahrii.mangashi.ui.home.main

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
class MainHomeViewModelTest {
    private val mangaList = DummyData.getMangaList()
    private lateinit var viewModel: MainHomeViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mangaUseCaseImpl: MangaUseCaseImpl

    @Mock
    private lateinit var observer: Observer<ResultState<List<Manga>>>

    @Before
    fun setUp() {
        viewModel = MainHomeViewModel(mangaUseCaseImpl)
    }

    @Test
    fun `getting all manga test`() {
        val resultState = ResultState.SUCCESS(mangaList)
        val result = BehaviorSubject.create<ResultState<List<Manga>>>()
        result.onNext(resultState)

        assertEquals(resultState, result.value)

        `when`(mangaUseCaseImpl.getAllManga())
            .thenReturn(result.toFlowable(BackpressureStrategy.BUFFER))

        viewModel.getAllManga().observeForever(observer)
        verify(mangaUseCaseImpl).getAllManga()
        verify(observer).onChanged(resultState)
    }
}