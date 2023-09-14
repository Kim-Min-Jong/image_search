package com.sparta.imagesearch.view.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparta.imagesearch.data.repository.ModelRepositoryImpl
import com.sparta.imagesearch.data.repository.datasource.SaveDataSourcesImpl
import com.sparta.imagesearch.data.repository.datasource.SearchDataSourceImpl
import com.sparta.imagesearch.data.service.KaKaoSearchService
import com.sparta.imagesearch.preference.PreferenceUtils

import java.lang.IllegalArgumentException

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = ModelRepositoryImpl(
                SearchDataSourceImpl(KaKaoSearchService.create()),
                SaveDataSourcesImpl(
                    PreferenceUtils((context))
                )
            )
            return MainViewModel(repository) as T

        } else {
            throw IllegalArgumentException()
        }
    }
}