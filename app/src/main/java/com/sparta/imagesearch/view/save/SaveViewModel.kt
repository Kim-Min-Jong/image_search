package com.sparta.imagesearch.view.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.repository.ModelRepository
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.view.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {
    private val _modelState: MutableLiveData<APIResponse<List<IntegratedModel>>> =
        MutableLiveData()
    val modelState: LiveData<APIResponse<List<IntegratedModel>>>
        get() = _modelState

    private val _removeState: MutableLiveData<APIResponse<Unit>> = MutableLiveData()
    val removeState: LiveData<APIResponse<Unit>>
        get() = _removeState


    fun getAllModels() {
        _modelState.value = APIResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getAllModels()
            result(response, _modelState)
        }
    }
    fun saveClear() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.removeAllModels()
            result(response, _removeState)
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