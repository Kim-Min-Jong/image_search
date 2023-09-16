package com.sparta.imagesearch.view.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparta.imagesearch.data.repository.ModelRepositoryImpl
import com.sparta.imagesearch.data.repository.datasource.SaveDataSourcesImpl
import com.sparta.imagesearch.data.repository.datasource.SearchDataSourceImpl
import com.sparta.imagesearch.data.service.RetrofitModule
import com.sparta.imagesearch.preference.PreferenceUtils

import java.lang.IllegalArgumentException

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val repository = ModelRepositoryImpl(
                SearchDataSourceImpl(RetrofitModule.create()),
                SaveDataSourcesImpl(
                    PreferenceUtils((context))
                )
            )
            return SearchViewModel(repository) as T

        } else {
            throw IllegalArgumentException()
        }
    }
}