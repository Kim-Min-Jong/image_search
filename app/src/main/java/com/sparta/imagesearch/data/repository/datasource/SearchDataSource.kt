package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import retrofit2.Response

// Repository 패턴의 DataSource 검색 영역 추상화
interface SearchDataSource {
    suspend fun getClips(token: String, query: String?, page: Int): Response<ResponseClip>
    suspend fun getImages(token: String, query: String?, page: Int): Response<ResponseImage>
}