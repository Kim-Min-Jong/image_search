package com.sparta.imagesearch.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringExtension {
    fun String.dateToString() :String
            = this.split(".").first()

    private fun String.dateTimeToString(): String
            = this.split("T").joinToString(" ")

    fun String.stringToDate(): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return sdf.parse(this.dateTimeToString())
    }
}