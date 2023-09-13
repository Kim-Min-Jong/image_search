package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.data.repository.datasource.SearchDataSource
import com.sparta.imagesearch.util.APIResponse

class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource
) : SearchRepository {
    override suspend fun getClips(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<ResponseClip> {
       val response = searchDataSource.getClips(token, query, page)
        if(response.isSuccessful) {
            response.body()?.let {
                return APIResponse.Success(it)
            }
        }
        return APIResponse.Error(response.message())
    }

    override suspend fun getImages(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<ResponseImage> {
        val response = searchDataSource.getImages(token, query, page)
        if(response.isSuccessful) {
            response.body()?.let {
                return APIResponse.Success(it)
            }
        }
        return APIResponse.Error(response.message())
    }

}