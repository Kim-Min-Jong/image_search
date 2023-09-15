package com.sparta.imagesearch.data.model

import androidx.recyclerview.widget.DiffUtil

data class IntegratedModel(
    val thumbnailUrl: String?,
    val title: String?,
    val dateTime: String?,
    var height: Int = 500,
    var width: Int = 1000,
    var isLiked: Boolean = false,
    var ordering: Long = 0L
) {
    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<IntegratedModel>() {
            override fun areItemsTheSame(
                oldItem: IntegratedModel,
                newItem: IntegratedModel
            ): Boolean = oldItem.thumbnailUrl == newItem.thumbnailUrl


            override fun areContentsTheSame(
                oldItem: IntegratedModel,
                newItem: IntegratedModel
            ): Boolean = oldItem == newItem
        }
    }
}
