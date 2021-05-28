package org.fahrii.mangashi.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import org.fahrii.mangashi.core.databinding.ItemMangaRowBinding
import org.fahrii.mangashi.core.domain.model.Manga
import org.fahrii.mangashi.core.utils.setImageFromUrl

class ItemMangaAdapter : RecyclerView.Adapter<ItemMangaAdapter.ItemMangaViewHolder>() {
    private val mangaList = ArrayList<Manga>()
    var onItemClicked: OnItemClicked? = null

    fun setMangaList(mangaList: List<Manga>) {
        this.mangaList.clear()
        this.mangaList.addAll(mangaList)
        notifyDataSetChanged()
    }

    inner class ItemMangaViewHolder(private val binding: ItemMangaRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(manga: Manga) {
            with(binding) {
                tvTitle.text = manga.title
                tvType.text = manga.type
                ivThumbnail.setImageFromUrl(manga.thumbnail)

                itemView.clicks()
                    .subscribe { onItemClicked?.setOnItemClicked(manga) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMangaViewHolder {
        val binding =
            ItemMangaRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMangaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemMangaViewHolder, position: Int) {
        holder.bind(mangaList[position])
    }

    override fun getItemCount(): Int = mangaList.size

    interface OnItemClicked {
        fun setOnItemClicked(manga: Manga)
    }
}