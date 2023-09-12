package com.sparta.imagesearch.data.model.image


import com.google.gson.annotations.SerializedName

data class ResponseImage(
    @SerializedName("documents")
    val documents: List<Document>,
    @SerializedName("meta")
    val meta: Meta
)