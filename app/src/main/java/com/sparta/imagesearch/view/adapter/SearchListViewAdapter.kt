package com.sparta.imagesearch.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.databinding.ItemSearchBinding
import com.sparta.imagesearch.extension.DateExtension.dateToString
import com.sparta.imagesearch.view.App

// 검색창 리사이클러뷰 어댑터 (ListAdapter - DiffUtil 사용)
class SearchListViewAdapter(
    private val onStarChecked: (IntegratedModel) -> Unit
) : ListAdapter<IntegratedModel, SearchListViewAdapter.SearchViewHolder>(IntegratedModel.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onStarChecked
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun isLikedResources(isLiked: Boolean, checkBox: CheckBox) {
        when (isLiked) {
            true -> checkBox.setBackgroundResource(R.drawable.ic_star_favorite)
            false -> checkBox.setBackgroundResource(R.drawable.ic_star)
        }
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchBinding,
        private val onStarChecked: (IntegratedModel) -> Unit
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
            timeTextView.text = model.dateTime?.dateToString()
            isLikedResources(model.isLiked, likedCheckBox)
            likedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                isLikedResources(isChecked, likedCheckBox)
                if (model.isLiked != isChecked) {
                    onStarChecked(
                        model.copy(
                            isLiked = isChecked,
                            ordering = ++App.prefs.orderingId
                        )
                    )
                }
            }
        }
    }
}