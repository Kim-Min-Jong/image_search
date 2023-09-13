package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.util.APIResponse

interface SearchRepository {
    suspend fun getClips(token: String, query: String?, page: Int): APIResponse<ResponseClip>
    suspend fun getImages(token: String, query: String?, page: Int): APIResponse<ResponseImage>
}