package com.sparta.imagesearch.util

import com.google.gson.GsonBuilder
import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.data.model.clip.ResponseClip
import com.sparta.imagesearch.data.model.image.ResponseImage
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel
import com.sparta.imagesearch.extension.StringExtension.dateToString
import com.sparta.imagesearch.extension.StringExtension.stringToDate

/**
 * Repository에서는 DataSource의 데이터를 그대로 전달해주는데 데이터가 바뀌게 된다면, UI 단이나 ViewModel단에서
 * 형변환 로직을 처리 해야 하는 부담이 있음
 * 특히 ViewModel 단에서는 비즈니스 로직이 수행 되어야 하는데 타입이 다르면, 처리하는 데이터의 복잡성이 높아짐
 *  그래서 Repository에서 ViewModel로 값을 넘겨 주기전, Repository 단에서 Mapping을 해서 데이터를 제공
 */

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