package com.sparta.imagesearch.view.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sparta.imagesearch.data.repository.SearchRepositoryImpl
import com.sparta.imagesearch.data.repository.datasource.SearchDataSourceImpl
import com.sparta.imagesearch.data.service.KaKaoSearchService

import java.lang.IllegalArgumentException

class SearchViewModelFactory: ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val repository = SearchRepositoryImpl(SearchDataSourceImpl(KaKaoSearchService.create()))
            return SearchViewModel(repository) as T

        } else {
            throw IllegalArgumentException()
        }
    }
}