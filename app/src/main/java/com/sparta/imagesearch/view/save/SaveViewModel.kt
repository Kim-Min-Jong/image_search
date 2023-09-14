package com.sparta.imagesearch.view.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.repository.ModelRepository
import com.sparta.imagesearch.util.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {
    private val _modelState: MutableLiveData<APIResponse<MutableCollection<out Any?>>> =
        MutableLiveData()
    val modelState: LiveData<APIResponse<MutableCollection<out Any?>>>
        get() = _modelState

    fun getAllModels() {
        _modelState.value = APIResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getAllModels()
            result(response, _modelState)
        }
    }

    private fun <T> result(response: APIResponse<T>, livedata: MutableLiveData<APIResponse<T>>) {
        try {
            if (response.data != null) {
                livedata.postValue(response)
            } else {
                livedata.postValue(APIResponse.Error(response.message.toString()))
            }
        } catch (e: Exception) {
            livedata.postValue(APIResponse.Error(e.message.toString()))
        }
    }
}