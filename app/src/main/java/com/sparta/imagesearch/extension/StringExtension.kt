package com.sparta.imagesearch.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object StringExtension {
    fun String.dateToString() :String
            = this.split(".").first()

    fun String.dateTimeToString(): String
            = this.split("T").joinToString(" ")


    @RequiresApi(Build.VERSION_CODES.O)
    fun String.stringToDateTime(): LocalDateTime =
        LocalDateTime.parse(this)


}