package com.sparta.imagesearch.data.model

import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.atomic.AtomicLong

data class IntegratedModel(
    val thumbnailUrl: String?,
    val title: String?,
    val dateTime: LocalDateTime,
    var height: Int = 500,
    var width: Int = 1000,
    var isLiked: Boolean = false
)
