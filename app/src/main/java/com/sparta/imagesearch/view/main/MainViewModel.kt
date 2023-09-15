package com.sparta.imagesearch.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sparta.imagesearch.data.repository.ModelRepository

class MainViewModel(
    private val modelRepository: ModelRepository
): ViewModel() {

    private val _networkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val networkStatus: LiveData<Boolean>
        get() = _networkStatus

    fun setStatus(isAvailable: Boolean) {
        _networkStatus.value = isAvailable
    }

}