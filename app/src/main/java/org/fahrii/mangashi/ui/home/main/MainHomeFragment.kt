package org.fahrii.mangashi.ui.home.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.fahrii.mangashi.R
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.ui.ItemMangaAdapter
import org.fahrii.mangashi.core.utils.setGone
import org.fahrii.mangashi.core.utils.setVisible
import org.fahrii.mangashi.databinding.FragmentMainHomeBinding

@AndroidEntryPoint
class MainHomeFragment : Fragment() {
    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ItemMangaAdapter? = null
    private val adapter get() = _adapter!!

    private val mainHomeViewModel: MainHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)
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
            rvMain.layoutManager = LinearLayoutManager(requireActivity())
            rvMain.adapter = adapter
            adapter.onItemClicked = object : ItemMangaAdapter.OnItemClicked {
                override fun setOnItemClicked(manga: Manga) {
                    findNavController().navigate(
                        MainHomeFragmentDirections.actionMainHomeFragmentToDetailActivity(manga)
                    )
                }
            }
        }

        with(binding.toolbar) {
            inflateMenu(R.menu.menu_home)
            title = getString(R.string.app_name)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(MainHomeFragmentDirections.actionMainHomeFragmentToSearchHomeFragment())
                        true
                    }
                    R.id.action_favorite -> {
                        findNavController().navigate(MainHomeFragmentDirections.actionMainHomeFragmentToFavoriteHomeFragment())
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setManga() {
        mainHomeViewModel.getAllManga().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.LOADING -> {
                    binding.vEmpty.root.setGone()
                    binding.vError.root.setGone()
                    binding.rvMain.setGone()
                    binding.shimmerFrameLayout.showShimmer(true)
                    binding.shimmerFrameLayout.setVisible()
                }
                is ResultState.SUCCESS -> {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.hideShimmer()
                    binding.shimmerFrameLayout.setGone()
                    if (result.data.isEmpty()) {
                        binding.vEmpty.root.setVisible()
                        binding.rvMain.setGone()
                    } else {
                        binding.vEmpty.root.setGone()
                        binding.rvMain.setVisible()
                        adapter.setMangaList(result.data)
                    }
                }
                is ResultState.ERROR -> {
                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.hideShimmer()
                    binding.shimmerFrameLayout.setGone()
                    binding.vEmpty.root.setGone()
                    binding.rvMain.setGone()
                    binding.vError.root.setVisible()
                }
            }
        }
    }
}