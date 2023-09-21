package com.sparta.imagesearch.view.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.repository.ModelRepository
import com.sparta.imagesearch.util.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {
    // 모델을 가져올 떄의 상태를 담을 변수
    private val _modelState: MutableLiveData<APIResponse<List<IntegratedModel>>> =
        MutableLiveData()
    val modelState: LiveData<APIResponse<List<IntegratedModel>>>
        get() = _modelState

    // 모델을 삭제할 때의 상태를 담을 변수
    private val _removeState: MutableLiveData<APIResponse<Unit>> = MutableLiveData()
    val removeState: LiveData<APIResponse<Unit>>
        get() = _removeState


    // sharedPreference의 모든 값 가져오기
    fun getAllModels() {
        _modelState.value = APIResponse.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getAllModels()
            result(response, _modelState)
        }
    }

    // sharedPreference의 모든 값 삭제하기
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

    // // sharedPreference의 특정 모델 삭제하기
    fun removeModel(model: IntegratedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            modelRepository.removeModel(model.thumbnailUrl ?: return@launch)
            val reload = modelRepository.getAllModels()
            result(reload, _modelState)
        }
    }
}