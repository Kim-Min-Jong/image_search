package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.repository.datasource.SaveDataSource
import com.sparta.imagesearch.data.repository.datasource.SearchDataSource
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.util.ModelMapper.toIntegratedModelList

// ViewModel에 데이터를 전달하는 Repositroy 구현체 - ViewModel은 여기서 받는 데이터의 출처를 몰라도 됨
// 그저 이용만 하기 때문에, Data 와 비즈니스 로직이 분리 됨
class ModelRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val saveDataSource: SaveDataSource
) : ModelRepository {
    override suspend fun getClips(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<List<IntegratedModel>> {
        val response = searchDataSource.getClips(token, query, page)
        if (response.isSuccessful) {
            response.body()?.let {
                return APIResponse.Success(it.toIntegratedModelList())
            }
        }
        return APIResponse.Error(response.message())
    }

    override suspend fun getImages(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<List<IntegratedModel>> {
        val response = searchDataSource.getImages(token, query, page)
        if (response.isSuccessful) {
            response.body()?.let {
                return APIResponse.Success(it.toIntegratedModelList())
            }
        }
        return APIResponse.Error(response.message())
    }


    override suspend fun getModel(key: String): APIResponse<IntegratedModel> {
        val response = saveDataSource.getModel(key)
        if (response != null) {
            return APIResponse.Success(response)
        }
        return APIResponse.Error("matching key nothing")
    }

    override suspend fun setModel(key: String, value: IntegratedModel) {
        return saveDataSource.setModel(key, value)
    }

    override suspend fun removeModel(key: String) {
        return saveDataSource.removeModel(key)
    }

    override suspend fun getAllModels(): APIResponse<List<IntegratedModel>> {
        val response = saveDataSource.getAllModels()
        if (response.isEmpty()) {
            return APIResponse.Error("보관함 목록이 없습니다.")
        }
        return APIResponse.Success(response.toIntegratedModelList())
    }

    override suspend fun removeAllModels(): APIResponse<Unit> {
        val response = saveDataSource.removeAllModels()
        if (response != Unit) {
            return APIResponse.Error("삭제 실패")
        }
        return APIResponse.Success(response)
    }

}
