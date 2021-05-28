package org.fahrii.mangashi.favorite.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.EntryPointAccessors
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.ui.ItemMangaAdapter
import org.fahrii.mangashi.core.utils.setGone
import org.fahrii.mangashi.core.utils.setVisible
import org.fahrii.mangashi.di.FavoriteModuleDependencies
import org.fahrii.mangashi.favorite.R
import org.fahrii.mangashi.favorite.databinding.FragmentFavoriteHomeBinding
import org.fahrii.mangashi.favorite.di.DaggerFavoriteComponent
import org.fahrii.mangashi.favorite.ui.ViewModelFactory
import javax.inject.Inject

class FavoriteHomeFragment : Fragment() {
    private var _binding: FragmentFavoriteHomeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ItemMangaAdapter? = null
    private val adapter get() = _adapter!!

    @Inject
    lateinit var factory: ViewModelFactory

    private val favoriteHomeViewModel: FavoriteHomeViewModel by viewModels { factory }

    companion object {
        @StringRes
        private const val FAVORITE_TEXT = R.string.favorite

        @DrawableRes
        private const val ARROW_BACK_ICON = R.drawable.ic_arrow_back
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerFavoriteComponent.builder()
            .context(requireContext())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireActivity().applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteHomeBinding.inflate(inflater, container, false)
        _adapter = ItemMangaAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setManga()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun setLayout() {
        with(binding) {
            rvFavorite.layoutManager = LinearLayoutManager(requireContext())
            rvFavorite.adapter = adapter
            adapter.onItemClicked = object : ItemMangaAdapter.OnItemClicked {
                override fun setOnItemClicked(manga: Manga) {
                    findNavController().navigate(
                        FavoriteHomeFragmentDirections
                            .actionFavoriteHomeFragmentToDetailActivity(manga)
                    )
                }
            }
        }

        with(binding.toolbar) {
            title = getString(FAVORITE_TEXT)
            navigationIcon = ContextCompat.getDrawable(requireContext(), ARROW_BACK_ICON)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setManga() {
        favoriteHomeViewModel.getFavoriteManga().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.LOADING -> {
                    binding.vEmpty.root.setGone()
                    binding.vError.root.setGone()
                    binding.rvFavorite.setGone()
                    binding.shimmerFrameLayout.showShimmer(true)
                    binding.shimmerFrameLayout.setVisible()
                }
                is ResultState.SUCCESS -> {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.hideShimmer()
                    binding.shimmerFrameLayout.setGone()
                    if (result.data.isEmpty()) {
                        binding.vEmpty.root.setVisible()
                        binding.rvFavorite.setGone()
                    } else {
                        binding.vEmpty.root.setGone()
                        binding.rvFavorite.setVisible()
                        adapter.setMangaList(result.data)
                    }
                }
                is ResultState.ERROR -> {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.hideShimmer()
                    binding.shimmerFrameLayout.setGone()
                    binding.vEmpty.root.setGone()
                    binding.rvFavorite.setGone()
                    binding.vError.root.setVisible()
                }
            }
        }
    }
}