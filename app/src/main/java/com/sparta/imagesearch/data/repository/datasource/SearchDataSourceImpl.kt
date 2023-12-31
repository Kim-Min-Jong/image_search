package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.data.service.KaKaoSearchService
import retrofit2.Response

// Repository 패턴의 검색 DataSource 구현체 - Retrofit API (KaKao API) 데이터만 주고 받음
class SearchDataSourceImpl(
    private val searchService: KaKaoSearchService
) : SearchDataSource {
    override suspend fun getClips(
        token: String,
        query: String?,
        page: Int
    ): Response<ResponseClip> {
        return searchService.getClips(token, query, page)
    }

    override suspend fun getImages(
        token: String,
        query: String?,
        page: Int
    ): Response<ResponseImage> {
        return searchService.getImages(token, query, page)
    }
}
