package com.sparta.imagesearch.data.model

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

// Image, Clip을 가져왔을 때, 합쳐서 한번에 보여줄 데이터 모델
data class IntegratedModel(
    val thumbnailUrl: String?,
    val title: String?,
    val dateTime: Date?,
    var height: Int = 500,
    var width: Int = 1000,
    var isLiked: Boolean = false,
    var ordering: Long = 0L,
    var isEnd: Boolean?
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IntegratedModel>() {
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
