package com.sparta.imagesearch.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateExtension {

    fun Date.dateToString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return sdf.format(this)
    }
}