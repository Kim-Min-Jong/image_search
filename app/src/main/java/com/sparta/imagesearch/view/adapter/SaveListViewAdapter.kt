package com.sparta.imagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemBookmarkBinding
import com.sparta.imagesearch.extension.StringExtension.dateTimeToString

class SaveListViewAdapter :
    ListAdapter<IntegratedModel, SaveListViewAdapter.SaveViewHolder>(IntegratedModel.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveViewHolder =
        SaveViewHolder(
            ItemBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SaveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SaveViewHolder(
        private val binding: ItemBookmarkBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: IntegratedModel) = with(binding) {
            val dimensionRatio = model.width / model.height.toFloat()
            val targetWidth = binding.root.resources.displayMetrics.widthPixels -
                    (binding.root.paddingStart + binding.root.paddingEnd)
            val targetHeight = (targetWidth * dimensionRatio).toInt()
            Glide.with(root)
                .load(model.thumbnailUrl)
                .override(model.width, targetHeight)
                .fitCenter()
                .into(thumbnailImageView)

            titleTextView.text = model.title
            timeTextView.text = model.dateTime?.dateTimeToString()
        }
    }
}