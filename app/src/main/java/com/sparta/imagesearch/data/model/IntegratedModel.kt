package com.sparta.imagesearch.data.model

import java.util.Date

data class IntegratedModel(
    val thumbnailUrl: String?,
    val title: String?,
    val dateTime: String?,
    var height: Int = 500,
    var width: Int = 1000
) {

}
