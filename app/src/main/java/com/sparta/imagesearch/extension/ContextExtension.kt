package com.sparta.imagesearch.extension

import android.content.Context
import android.widget.Toast

object ContextExtension {
    fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}