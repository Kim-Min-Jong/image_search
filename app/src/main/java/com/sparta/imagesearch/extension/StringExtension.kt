package com.sparta.imagesearch.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Retrofit에서 받아온 날짜 값을 파싱하는 확장함수
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