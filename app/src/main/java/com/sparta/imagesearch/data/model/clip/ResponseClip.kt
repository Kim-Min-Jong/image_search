package com.sparta.imagesearch.data.model.clip


import com.google.gson.annotations.SerializedName
import com.sparta.imagesearch.data.model.clip.Document
import com.sparta.imagesearch.data.model.clip.Meta

data class ResponseClip(
    @SerializedName("documents")
    val documents: List<Document?>?,
    @SerializedName("meta")
    val meta: Meta?
)