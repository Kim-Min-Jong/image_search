package com.sparta.imagesearch.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateExtension {

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.dateToString(): String {
        val str = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return str
    }
}