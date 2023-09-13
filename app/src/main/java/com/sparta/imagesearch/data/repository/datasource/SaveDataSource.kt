package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.IntegratedModel

interface SaveDataSource {
    suspend fun getModels(key: String) : List<String>
    suspend fun setModel(key: String, value: IntegratedModel)
    suspend fun removeModel(key: String)
}