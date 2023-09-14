package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.IntegratedModel

interface SaveDataSource {
    suspend fun getModel(key: String) : List<String>
    suspend fun setModel(key: String, value: IntegratedModel)
    suspend fun removeModel(key: String)
}