package com.sparta.imagesearch.service

import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KaKaoSearchService {
    @GET("image")
    suspend fun getImages(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("page") page: Int
    ): Response<ResponseImage>

    @GET("vclip")
    suspend fun getClips(
        @Header("Authorization") token: String,
        @Query("query") query: String?,
        @Query("page") page: Int
    ): Response<ResponseClip>



}