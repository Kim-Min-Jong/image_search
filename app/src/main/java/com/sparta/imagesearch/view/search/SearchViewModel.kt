package com.sparta.imagesearch.view.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.data.repository.ModelRepository
import com.sparta.imagesearch.extension.StringExtension.dateToString
import com.sparta.imagesearch.util.APIResponse
import com.sparta.imagesearch.util.ScrollConstant.SCROLL_DEFAULT
import com.sparta.imagesearch.view.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {
    private val _state: MutableLiveData<APIResponse<List<IntegratedModel>>> = MutableLiveData()
    val state: LiveData<APIResponse<List<IntegratedModel>>>
        get() = _state
    private val _prefsState: MutableLiveData<APIResponse<IntegratedModel>> = MutableLiveData()
    val prefsState: LiveData<APIResponse<IntegratedModel>>
        get() = _prefsState

    private var responseClip: ResponseClip? = null
    private var responseImage: ResponseImage? = null
    private val list = arrayListOf<IntegratedModel>()

    private var _isEndClip: Boolean? = null
    private var _isEndImage: Boolean? = null
    val isEndClip: Boolean?
        get() = _isEndClip
    val isEndImage: Boolean?
        get() = _isEndImage
    private val keyList = App.prefs.getAllKeys()

    fun clearList() = list.clear()

    fun getDatas(token: String, query: String, page: Int, scrollFlag: Int) {
        // 맨 밑에서 스크롤 시 로딩을 하지 않도록
        if (scrollFlag == SCROLL_DEFAULT) {
            _state.value = APIResponse.Loading(emptyList())
        } else {
            _state.value = APIResponse.Loading()
        }
        getImages(token, query, page)
        getClips(token, query, page)
    }

    private fun getClips(token: String, query: String, page: Int) {
        // 다음 페이지로 넘기기 위해 이전 값들을 없앰
        clearList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getClips(token, query, page)
            responseClip = response.data
            responseClip?.documents?.forEach {
                if(it?.thumbnail in keyList){
                    list.add(
                        IntegratedModel(
                            it?.thumbnail,
                            "[Clip] " + it?.title,
                            it?.datetime!!.dateToString(),
                            isLiked = true
                        )
                    )
                } else {
                    list.add(
                        IntegratedModel(
                            it?.thumbnail,
                            "[Clip] " + it?.title,
                            it?.datetime!!.dateToString()
                        )
                    )
                }
                _isEndClip = responseClip?.meta?.isEnd
            }
            _state.postValue(APIResponse.Success(list.sortedByDescending { it.dateTime }))
        }
    }

    private fun getImages(token: String, query: String, page: Int) {
        // 다음 페이지로 넘기기 위해 이전 값들을 없앰
        clearList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getImages(token, query, page)
            responseImage = response.data
            responseImage?.documents?.forEach {
                if(it.thumbnailUrl in keyList){
                    list.add(
                        IntegratedModel(
                            it.thumbnailUrl,
                            "[Image] " + it.displaySitename,
                            it.datetime.dateToString(),
                            it.height,
                            it.width,
                            isLiked = true
                        )
                    )
                } else {
                    list.add(
                        IntegratedModel(
                            it.thumbnailUrl,
                            "[Image] " + it.displaySitename,
                            it.datetime.dateToString(),
                            it.height,
                            it.width
                        )
                    )
                }

                _isEndImage = responseImage?.meta?.isEnd
            }
            _state.postValue(APIResponse.Success(list.sortedByDescending { it.dateTime }))
        }
    }

    fun addModelFromPreference(model: IntegratedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            model.thumbnailUrl?.let { url ->
                modelRepository.setModel(url, model)
            }
        }
    }

    fun removeModelFromPreference(model: IntegratedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            model.thumbnailUrl?.let {
                modelRepository.removeModel(it)
            }
        }
    }

     fun getModelFromPreference(url: String) {
         _prefsState.value = APIResponse.Loading()
         viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getModel(url)
             result(response, _prefsState)
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