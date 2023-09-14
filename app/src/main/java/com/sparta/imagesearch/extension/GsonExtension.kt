package com.sparta.imagesearch.extension

import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel

object GsonExtension {
    fun GsonBuilder.gsonToIntegrateModel(value: String?): IntegratedModel? {
        return this.create().fromJson(value, IntegratedModel::class.java)
    }
}