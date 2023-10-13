package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.util.APIResponse

// ViewModel에 데이터를 전달하는 Repositroy 추상화
interface ModelRepository {
    // Retrofit API
    suspend fun getClips(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<List<IntegratedModel>>

    suspend fun getImages(
        token: String,
        query: String?,
        page: Int
    ): APIResponse<List<IntegratedModel>>

    /***
     * SharedPreference API - DB 대용
     *
     * model 말고도 보관함 순서를 위한 preference, 마지막 검색어를 담는 preference도 있지만,
     * 뒤의 2개는 model이 아닌 단순한 문자열, 숫자만을 저장해서 repository에서 제외했음
     * 많은 데이터를 주고 받는 IntegratedModel만 repository에서 사용
     */
    suspend fun getModel(key: String): APIResponse<IntegratedModel>
    suspend fun setModel(key: String, value: IntegratedModel)
    suspend fun removeModel(key: String)
    suspend fun getAllModels(): APIResponse<List<IntegratedModel>>
    suspend fun removeAllModels(): APIResponse<Unit>
}
