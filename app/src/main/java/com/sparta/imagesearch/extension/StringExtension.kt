package com.sparta.imagesearch.extension

object StringExtension {
    fun String.dateToString() :String
            = this.split(".").first().split("T").joinToString(" ")
}