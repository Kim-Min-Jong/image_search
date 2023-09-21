package com.sparta.imagesearch.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 날짜 출력 포맷 변환 확장함수
object DateExtension {

    fun Date.dateToString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
        return sdf.format(this)
    }
}