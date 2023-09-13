package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.data.repository.datasource.SaveDataSource
import com.sparta.imagesearch.data.repository.datasource.SearchDataSource
import com.sparta.imagesearch.util.APIResponse

class ModelRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val saveDataSource: SaveDataSource
) : ModelRepository {
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


    override suspend fun getModels(key: String): List<String> {
        return saveDataSource.getModels(key)
    }

    override suspend fun setModel(key: String, value: IntegratedModel) {
        return saveDataSource.setModel(key, value)
    }

    override suspend fun removeModel(key: String) {
        return saveDataSource.removeModel(key)
    }

}