package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.util.APIResponse

interface ModelRepository {
    // Retrofit API
    suspend fun getClips(token: String, query: String?, page: Int): APIResponse<ResponseClip>
    suspend fun getImages(token: String, query: String?, page: Int): APIResponse<ResponseImage>

    // SharedPreference API
    suspend fun getModel(key: String) : APIResponse<IntegratedModel>
    suspend fun setModel(key: String, value: IntegratedModel)
    suspend fun removeModel(key: String)
    suspend fun getAllModels(): APIResponse<MutableCollection<out Any?>>
    suspend fun removeAllModels(): APIResponse<Unit>
}