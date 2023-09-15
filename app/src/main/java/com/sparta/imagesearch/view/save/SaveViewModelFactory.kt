package com.sparta.imagesearch.view.save

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparta.imagesearch.data.repository.ModelRepositoryImpl
import com.sparta.imagesearch.data.repository.datasource.SaveDataSourcesImpl
import com.sparta.imagesearch.data.repository.datasource.SearchDataSourceImpl
import com.sparta.imagesearch.data.service.RetrofitModule
import com.sparta.imagesearch.preference.PreferenceUtils

import java.lang.IllegalArgumentException

class SaveViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SaveViewModel::class.java)) {
            val repository = ModelRepositoryImpl(
                SearchDataSourceImpl(RetrofitModule.create()),
                SaveDataSourcesImpl(
                    PreferenceUtils((context))
                )
            )
            return SaveViewModel(repository) as T

        } else {
            throw IllegalArgumentException()
        }
    }
}