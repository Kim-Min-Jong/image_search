package com.sparta.imagesearch.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _networkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val networkStatus: LiveData<Boolean>
        get() = _networkStatus

    fun setStatus(isAvailable: Boolean) {
        _networkStatus.value = isAvailable
    }

}