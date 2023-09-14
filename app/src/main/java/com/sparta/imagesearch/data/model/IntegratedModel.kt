package com.sparta.imagesearch.data.model

data class IntegratedModel(
    val thumbnailUrl: String?,
    val title: String?,
    val dateTime: String?,
    var height: Int = 500,
    var width: Int = 1000,
    var isLiked: Boolean = false
)
