package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import retrofit2.Response

interface SearchDataSource {
    suspend fun getClips(token: String, query: String?, page: Int): Response<ResponseClip>
    suspend fun getImages(token: String, query: String?, page: Int): Response<ResponseImage>
}