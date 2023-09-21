package com.sparta.imagesearch.view.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.repository.ModelRepository
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

    private val list = arrayListOf<IntegratedModel>()

    //현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    private var _isEndClip: Boolean? = null
    private var _isEndImage: Boolean? = null
    val isEndClip: Boolean?
        get() = _isEndClip
    val isEndImage: Boolean?
        get() = _isEndImage
    private val keyList = App.prefs.getAllKeys()

    fun clearList() = list.clear()

    // 스크롤 타입에 따라 데이터를 가져오는 함수
    fun getDatas(token: String, query: String, page: Int, scrollFlag: Int) {
        // 맨 밑에서 스크롤 시 로딩을 보여주도록
        if (scrollFlag == SCROLL_DEFAULT) {
            _state.value = APIResponse.Loading(emptyList())
        } else {
            _state.value = APIResponse.Loading()
        }
        getImages(token, query, page, scrollFlag)
        getClips(token, query, page, scrollFlag)
    }

    // Clip 데이터를 가져와 list에 합치는 함수
    private fun getClips(token: String, query: String, page: Int, scrollFlag: Int) {
        // 스크롤 업데이트가 아닐 떄, 없애 주지 않으면 같은 데이터가 한번 더 들어옴
        if (scrollFlag == SCROLL_DEFAULT)
            clearList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getClips(token, query, page)
            response.data?.forEach {
                if(it.thumbnailUrl in keyList){
                    list.add(it.apply { isLiked = true })
                } else {
                    list.add(it)
                }
                _isEndClip = response.data.last().isEnd
            }
            _state.postValue(APIResponse.Success(list.sortedByDescending { it.dateTime }))
        }
    }

    // Image 데이터를 가져와 list에 합치는 함수
    private fun getImages(token: String, query: String, page: Int, scrollFlag: Int) {
        if (scrollFlag == SCROLL_DEFAULT)
            clearList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = modelRepository.getImages(token, query, page)
            response.data?.forEach {
                if(it.thumbnailUrl in keyList){
                    list.add(it.apply { isLiked = true })
                } else {
                    list.add(it)
                }

                _isEndImage = response.data.last().isEnd
            }
            _state.postValue(APIResponse.Success(list.sortedByDescending { it.dateTime }))
        }
    }

    // sharedPreference에 모델을 저장하는 함수
    fun addModelFromPreference(model: IntegratedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            model.thumbnailUrl?.let { url ->
                modelRepository.setModel(url, model)
            }
        }
    }

    // sharedPreference에서 모델을 제거하는 함수
    fun removeModelFromPreference(model: IntegratedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            model.thumbnailUrl?.let {
                modelRepository.removeModel(it)
            }
        }
    }
}