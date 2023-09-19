package com.sparta.imagesearch.util

import android.net.Uri
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.extension.DateExtension.dateToString
import com.sparta.imagesearch.view.App
import com.sparta.imagesearch.view.adapter.SearchListViewAdapter
import com.sparta.imagesearch.view.search.SearchFragment
import com.sparta.imagesearch.view.search.SearchViewModel
import java.util.Date

object BindingAdapter {
    @BindingAdapter("app:bindImage")
    @JvmStatic
    fun loadImage(imageView: ImageView, model: IntegratedModel) {
        val dimensionRatio = model.width / model.height.toFloat()
        val targetWidth = imageView.resources.displayMetrics.widthPixels -
                (imageView.paddingStart + imageView.paddingEnd)
        val targetHeight = (targetWidth * dimensionRatio).toInt()
        Glide.with(imageView.rootView)
            .load(Uri.parse(model.thumbnailUrl))
            .override(model.width, targetHeight)
            .fitCenter()
            .into(imageView)
    }

    @BindingAdapter("app:timeText")
    @JvmStatic
    fun dateToString(textView: TextView, date: Date?) {
        val str = date?.dateToString()
        textView.text = str
    }

    @BindingAdapter("app:searchModel")
    @JvmStatic
    fun isCheckedItem(
        checkBox: CheckBox,
        model: IntegratedModel,
    ) {
        isLikedResources(checkBox.isChecked, checkBox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            isLikedResources(isChecked, checkBox)
            ++App.prefs.id
            val inputModel = model.copy(
                isLiked = isChecked,
                ordering = ++App.prefs.id
            )
            when (inputModel.isLiked) {
                true -> {
                    inputModel.thumbnailUrl?.let { App.prefs.setModel(it, inputModel) }
                }

                false -> {
                    inputModel.thumbnailUrl?.let { App.prefs.removeModel(it) }
                }
            }
        }
    }

    @BindingAdapter("app:items")
    @JvmStatic
    fun setData(recyclerView: RecyclerView, items: List<IntegratedModel>?) {
        val listAdapter = recyclerView.adapter as SearchListViewAdapter
        listAdapter.submitList(items?.toMutableList())
    }

    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when (isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.ic_star_favorite)
            false -> checkBox.setBackgroundResource(R.drawable.ic_star)
        }
    }
}

