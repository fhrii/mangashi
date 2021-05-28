package org.fahrii.mangashi.core.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import org.fahrii.mangashi.core.R

fun ImageView.setImageFromUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .override(Target.SIZE_ORIGINAL)
        .placeholder(R.drawable.ic_image_with_background)
        .into(this)
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}