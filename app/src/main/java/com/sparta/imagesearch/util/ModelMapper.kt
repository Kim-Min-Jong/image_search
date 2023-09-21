package com.sparta.imagesearch.util

import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel
import com.sparta.imagesearch.extension.StringExtension.dateToString
import com.sparta.imagesearch.extension.StringExtension.stringToDate

object ModelMapper {
    fun ResponseClip.toIntegratedModelList(): List<IntegratedModel> {
        val list = arrayListOf<IntegratedModel>()
        this.documents?.forEach {
            list.add(
                IntegratedModel(
                    it?.thumbnail,
                    "[Clip] " + it?.title,
                    it?.datetime!!.dateToString().stringToDate(),
                    isEnd = this.meta?.isEnd
                )
            )
        }
        return list
    }
    fun ResponseImage.toIntegratedModelList(): List<IntegratedModel>  {
        val list = arrayListOf<IntegratedModel>()
        this.documents.forEach {
            list.add(
                IntegratedModel(
                    it.thumbnailUrl,
                    "[Image] " + it.displaySitename,
                    it.datetime.dateToString().stringToDate(),
                    it.height,
                    it.width,
                    isEnd = this.meta.isEnd
                )
            )
        }
        return list
    }
    fun MutableCollection<out Any?>?.toIntegratedModelList(): List<IntegratedModel> {
        val list = arrayListOf<IntegratedModel>()
        for (item in this?.toList()!!) {
            val gsonData =
                GsonBuilder().gsonToIntegrateModel(item.toString()) ?: continue
            list.add(gsonData)
        }
        return list
    }
}