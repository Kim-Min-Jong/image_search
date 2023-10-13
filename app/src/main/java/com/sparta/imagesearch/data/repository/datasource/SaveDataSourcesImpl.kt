package com.sparta.imagesearch.data.repository.datasource

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.preference.PreferenceUtils

// Repository 패턴의 저장 DataSource 구현체 - SharedPreference 데이터만 주고 받음
class SaveDataSourcesImpl(
    private val prefsUtils: PreferenceUtils
) : SaveDataSource {
    override suspend fun getModel(key: String): IntegratedModel? {
        return prefsUtils.getModel(key)
    }

    override suspend fun setModel(key: String, value: IntegratedModel) {
        return prefsUtils.setModel(key, value)
    }

    override suspend fun removeModel(key: String) {
        return prefsUtils.removeModel(key)
    }

    override suspend fun getAllModels(): MutableCollection<out Any?> {
        return prefsUtils.getAllModels()
    }

    override suspend fun removeAllModels() {
        return prefsUtils.clear()
    }
}
