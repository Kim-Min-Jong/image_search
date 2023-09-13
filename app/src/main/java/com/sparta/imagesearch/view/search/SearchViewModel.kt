package com.sparta.imagesearch.view.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.data.repository.SearchRepository
import com.sparta.imagesearch.extension.StringExtension.dateToString
import com.sparta.imagesearch.extension.StringExtension.stringToDateTime
import com.sparta.imagesearch.util.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SearchViewModel(
    private val searchRepository: SearchRepository
): ViewModel() {
    private val _state: MutableLiveData<APIResponse<List<IntegratedModel>>> = MutableLiveData()
    val state: LiveData<APIResponse<List<IntegratedModel>>>
        get() = _state
    private val clipList: MutableLiveData<APIResponse<ResponseClip>> = MutableLiveData()
    private val imageList: MutableLiveData<APIResponse<ResponseImage>> = MutableLiveData()
    private var responseClip: ResponseClip? = null
    private var responseImage: ResponseImage? = null
    private val list = arrayListOf<IntegratedModel>()

    fun getDatas(token: String, query: String, page: Int) {
        _state.value = APIResponse.Loading(emptyList())
       getImages(token, query, page)
       getClips(token, query, page)
    }

    private fun getClips(token: String, query: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.getClips(token, query, page)
            responseClip = response.data
            result(response,clipList)
            responseClip?.documents?.forEach {
                list.add(
                        IntegratedModel(
                            it?.thumbnail,
                            "[Clip] " + it?.title,
                            it?.datetime!!.dateToString().stringToDateTime()
                        )
                    )
            }
            _state.postValue(APIResponse.Success(list))
        }
    }

    private fun getImages(token: String, query: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.getImages(token, query, page)
            responseImage = response.data
            result(response, imageList)
            responseImage?.documents?.forEach {
                list.add(
                    IntegratedModel(
                        it.thumbnailUrl,
                        "[Image] " + it.displaySitename,
                        it.datetime.dateToString().stringToDateTime(),
                        it.height,
                        it.width
                    )
                )
            }
            _state.postValue(APIResponse.Success(list))
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