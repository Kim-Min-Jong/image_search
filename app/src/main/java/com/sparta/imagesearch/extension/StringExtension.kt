package com.sparta.imagesearch.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

object StringExtension {
    fun String.dateToString() :String
            = this.split(".").first()

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.stringToDateTime(): LocalDateTime =
        LocalDateTime.parse(this)


}