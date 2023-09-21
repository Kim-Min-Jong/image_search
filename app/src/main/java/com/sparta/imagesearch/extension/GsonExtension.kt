package com.sparta.imagesearch.extension

import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel

// SharedPreference에서 가져온 값(String)을 쉽게 모델로 변환
object GsonExtension {
    fun GsonBuilder.gsonToIntegrateModel(value: String?): IntegratedModel? {
        return this.create().fromJson(value, IntegratedModel::class.java)
    }
}