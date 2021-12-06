package com.mvlprem.apod.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mvlprem.apod.R
import com.mvlprem.apod.domain.Pictures
import com.mvlprem.apod.util.dateFormatter

/**
 * Binding adapter used to display picture date
 * calling [dateFormatter] util function to format date
 * @param item Models data Class
 */
@BindingAdapter("date")
fun TextView.date(item: Pictures) {
    item.let {
        text = dateFormatter(it.date)
    }
}

/**
 * Binding adapter used to display picture title
 * @param item Models data Class
 */
@BindingAdapter("title")
fun TextView.title(item: Pictures) {
    item.let {
        text = it.title
    }
}

/**
 * Binding adapter used to display picture explanation
 * @param item Models data Class
 */
@BindingAdapter("explanation")
fun TextView.explanation(item: Pictures) {
    item.let {
        text = it.explanation
    }
}

/**
 * Function using Glide library to display images from URL.
 * @param imageView to provide context for Glide
 * @param item Models data Class
 */
@BindingAdapter("image")
fun image(imageView: ImageView, item: Pictures) {
    item.let {
        val url = it.url
        val uri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.broken_image)
            )
            .into(imageView)
    }
}

/**
 * Function using Glide library to display images from URL.
 * @param imageView to provide context for Glide
 * @param item Models data Class
 */
@BindingAdapter("hdImage")
fun hdImage(imageView: ImageView, item: Pictures) {
    item.let {
        val url = it.hdurl
        val uri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        Glide.with(imageView.context)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.broken_image)
            )
            .into(imageView)
    }
}