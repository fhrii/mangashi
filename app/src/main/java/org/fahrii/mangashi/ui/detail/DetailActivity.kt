package org.fahrii.mangashi.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.navArgs
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import org.fahrii.mangashi.R
import org.fahrii.mangashi.core.data.ResultState
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.utils.setImageFromUrl
import org.fahrii.mangashi.databinding.ActivityDetailBinding

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private var _manga: Manga? = null
    private val manga get() = _manga!!

    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailActivityArgs by navArgs()

    private val mCompositeDisposable = CompositeDisposable()

    companion object {
        private const val TAG = "DetailActivity"

        @StringRes
        private const val ADD_MANGA_TEXT = R.string.add_manga

        @StringRes
        private const val ERROR_TEXT = R.string.error_text

        @StringRes
        private const val ADD_FAVORITE_TEXT = R.string.add_favorite

        @StringRes
        private const val REMOVE_FAVORITE_TEXT = R.string.remove_favorite

        @DrawableRes
        private const val FAVORITE_TRUE_ICON = R.drawable.ic_favorite_red

        @DrawableRes
        private const val FAVORITE_FALSE_ICON = R.drawable.ic_favorite_border_red
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayout()

        detailViewModel.getManga(manga.id).observe(this) { result ->
            when (result) {
                is ResultState.LOADING -> {
                    Log.d(TAG, getString(ADD_MANGA_TEXT))
                }
                is ResultState.SUCCESS -> {
                    with(binding) {
                        _manga = result.data
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.hideShimmer()
                        tvAuthor.text = result.data.author
                        tvStatus.text = result.data.status
                        tvSynopsis.text = result.data.synopsis
                        setFavoriteIcon(result.data.favorite)
                    }
                }
                is ResultState.ERROR -> {
                    Log.e(TAG, "ERROR: ${result.errMessage}")
                    Toast.makeText(this, getString(ERROR_TEXT), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _manga = null
        mCompositeDisposable.dispose()
    }

    private fun setLayout() {
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        _manga = args.manga
        setContentView(binding.root)

        with(binding) {
            tvTitle.text = manga.title
            tvType.text = manga.type
            ivThumbnail.setImageFromUrl(manga.thumbnail)

            val btnClickStream = btnFavorite.clicks()
                .filter { manga.author != null }
                .flatMap { detailViewModel.setFavorite(manga, !manga.favorite).toObservable() }
                .subscribe { result ->
                    when (result) {
                        is ResultState.SUCCESS -> {
                            manga.favorite = !manga.favorite
                            setFavoriteIcon(manga.favorite)
                        }
                        is ResultState.ERROR -> {
                            Log.d(TAG, "ERROR: ${result.errMessage}")
                            Toast.makeText(
                                this@DetailActivity, getString(ERROR_TEXT), Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ResultState.LOADING -> {
                            Log.d(
                                TAG,
                                if (!manga.favorite) getString(ADD_FAVORITE_TEXT)
                                else getString(REMOVE_FAVORITE_TEXT)
                            )
                        }
                    }
                }

            mCompositeDisposable.add(btnClickStream)
        }
    }

    private fun setFavoriteIcon(state: Boolean) {
        binding.btnFavorite.setImageResource(if (state) FAVORITE_TRUE_ICON else FAVORITE_FALSE_ICON)
    }
}