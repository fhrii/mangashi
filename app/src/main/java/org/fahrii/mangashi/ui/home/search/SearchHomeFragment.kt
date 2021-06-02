package org.fahrii.mangashi.ui.home.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.fahrii.mangashi.R
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.ui.ItemMangaAdapter
import org.fahrii.mangashi.core.ui.ItemMangaSmallAdapter
import org.fahrii.mangashi.core.utils.setGone
import org.fahrii.mangashi.core.utils.setVisible
import org.fahrii.mangashi.databinding.FragmentSearchHomeBinding
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchHomeFragment : Fragment() {
    private var _binding: FragmentSearchHomeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ItemMangaSmallAdapter? = null
    private val adapter get() = _adapter!!

    private val searchHomeViewModel: SearchHomeViewModel by viewModels()

    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchHomeBinding.inflate(inflater, container, false)
        _adapter = ItemMangaSmallAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
        mCompositeDisposable.dispose()
    }

    private fun setLayout() {
        binding.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearch.adapter = adapter
        adapter.onItemClicked = object : ItemMangaAdapter.OnItemClicked {
            override fun setOnItemClicked(manga: Manga) {
                val insertStream = searchHomeViewModel.insertManga(manga)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        when (result) {
                            is ResultState.LOADING -> {
                                Log.d(TAG, getString(ADD_MANGA_TEXT))
                            }
                            is ResultState.SUCCESS -> {
                                findNavController()
                                    .navigate(
                                        SearchHomeFragmentDirections
                                            .actionSearchHomeFragmentToDetailActivity(manga)
                                    )
                            }
                            is ResultState.ERROR -> {
                                Log.e(TAG, "ERROR: ${result.errMessage}")
                                Toast.makeText(
                                    requireContext(),
                                    getString(ERROR_TEXT),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                mCompositeDisposable.add(insertStream)
            }
        }

        with(binding.toolbar) {
            inflateMenu(R.menu.menu_home_search)
            val menuItem = menu.findItem(R.id.action_search_bar)

            menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    findNavController().popBackStack()
                    return true
                }
            })

            menu.performIdentifierAction(R.id.action_search_bar, 0)

            val searchView = menuItem.actionView as SearchView

            val searchStream = searchView.queryTextChanges()
                .skipInitialValue()
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { query ->
                    searchHomeViewModel.searchManga(query).toObservable()
                        .subscribeOn(Schedulers.computation())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    when (result) {
                        is ResultState.LOADING -> {
                            Log.d(TAG, getString(SEARCH_MANGA_TEXT))
                        }
                        is ResultState.SUCCESS -> {
                            binding.vError.root.setGone()
                            val isEmpty = result.data.isEmpty()
                            binding.vEmpty.root.isVisible = isEmpty
                            binding.rvSearch.isVisible = !isEmpty
                            if (!isEmpty) adapter.setMangaList(result.data)
                        }
                        is ResultState.ERROR -> {
                            binding.vError.root.setVisible()
                            binding.rvSearch.setGone()
                            binding.vEmpty.root.setGone()
                        }
                    }
                }

            mCompositeDisposable.add(searchStream)
        }
    }

    companion object {
        private const val TAG = "SearchHomeFragment"

        @StringRes
        private const val ADD_MANGA_TEXT = R.string.add_manga

        @StringRes
        private const val SEARCH_MANGA_TEXT = R.string.search_text

        @StringRes
        private const val ERROR_TEXT = R.string.error_text
    }
}