package com.sparta.imagesearch.data.service

import com.sparta.imagesearch.BuildConfig
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KaKaoSearchService {
    @GET("image")
    suspend fun getImages(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("page") page: Int,
        @Query("sort") sort: String? = "recency"
    ): Response<ResponseImage>

    @GET("vclip")
    suspend fun getClips(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("page") page: Int,
        @Query("sort") sort: String? = "recency"
    ): Response<ResponseClip>


    companion object {
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
        fun create(): KaKaoSearchService {
            return Retrofit.Builder()
                .baseUrl(Url.SEARCH_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient())
                .build()
                .create(KaKaoSearchService::class.java)
        }
    }
}