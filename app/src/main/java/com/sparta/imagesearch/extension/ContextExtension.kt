package com.sparta.imagesearch.extension

import android.content.Context
import android.widget.Toast

// 자주 사용하는 Toast 메세지를 사용하기 쉽게 확장함수로 만듦
object ContextExtension {
    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}