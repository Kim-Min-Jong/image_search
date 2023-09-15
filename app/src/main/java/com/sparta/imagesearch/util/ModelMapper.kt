package com.sparta.imagesearch.util

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.extension.StringExtension.dateToString
import java.util.UUID

object ModelMapper {
    fun ResponseClip.toIntegratedModels(): List<IntegratedModel> {
        val list = arrayListOf<IntegratedModel>()
        this.documents?.forEach {
            list.add(
                IntegratedModel(
                    it?.thumbnail,
                    "[Clip] " + it?.title,
                    it?.datetime!!.dateToString(),
                    isEnd = this.meta?.isEnd
                )
            )
        }
        return list
    }
    fun ResponseImage.toIntegratedModel(): List<IntegratedModel>  {
        val list = arrayListOf<IntegratedModel>()
        this.documents.forEach {
            list.add(
                IntegratedModel(
                    it.thumbnailUrl,
                    "[Image] " + it.displaySitename,
                    it.datetime.dateToString(),
                    it.height,
                    it.width,
                    isEnd = this.meta.isEnd
                )
            )
        }
        return list
    }

}