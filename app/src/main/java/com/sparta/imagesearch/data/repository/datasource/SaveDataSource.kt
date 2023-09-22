package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.IntegratedModel

// Repository 패턴의 DataSource 저장 영역 추상화
interface SaveDataSource {
    suspend fun getModel(key: String) : IntegratedModel?
    suspend fun setModel(key: String, value: IntegratedModel)
    suspend fun removeModel(key: String)
    suspend fun getAllModels() : MutableCollection<out Any?>
    suspend fun removeAllModels()
}