package com.sparta.imagesearch.data.repository

import com.sparta.imagesearch.BuildConfig
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.service.KaKaoSearchService
import com.sparta.imagesearch.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {
    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                //로깅 인터셉터
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    private val kakaoSearchApiService: KaKaoSearchService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.SEARCH_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
            .build()
            .create()
    }

    suspend fun getClips(token:String, query: String?, page: Int): ResponseClip? =
        kakaoSearchApiService.getClips(token, query, page).body()

    suspend fun getImages(token:String, query: String?, page: Int): ResponseImage? =
        kakaoSearchApiService.getImages(token, query, page).body()



}