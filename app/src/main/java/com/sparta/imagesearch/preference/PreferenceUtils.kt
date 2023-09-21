package com.sparta.imagesearch.preference

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel

// 보관함 모델, 마지막 검색어, 보관함 저장 순서를 저장하기 위한 SharedPreference Util 클래스
class PreferenceUtils(context: Context) {
    private val prefName = MODEL_PREFS_NAME
    private val orderingPrefsName = ORDERING_PREFS_NAME
    private val searchPrefsName = SEARCH_PREFS_NAME

    private val prefs = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE)
    private val orderingPrefs = context.getSharedPreferences(orderingPrefsName, Activity.MODE_PRIVATE)
    private val searchPrefs = context.getSharedPreferences(searchPrefsName, Activity.MODE_PRIVATE)

    var keyword: String?
        get() = searchPrefs.getString(SEARCH_PREFS_NAME, null)
        set(value) {
            searchPrefs.edit().putString(SEARCH_PREFS_NAME, value).apply()
        }
    var orderingId: Long
        get() = orderingPrefs.getLong(ORDERING_PREFS_NAME, 0L)
        set(value) {
            orderingPrefs.edit().putLong(ORDERING_PREFS_NAME, value).apply()
        }

    fun setModel(key: String, value: IntegratedModel?) {
        val editor = prefs.edit()
        val model = Gson().toJson(value, IntegratedModel::class.java)
        if(value != null){
            editor.putString(key, model)
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getModel(key: String): IntegratedModel? {
        // json 값을 받아와 리스트로 바꿀 준비
        val jsonList = prefs.getString(key, null)
        val gson = GsonBuilder()
        return gson.gsonToIntegrateModel(jsonList)
    }

    fun getAllModels(): MutableCollection<out Any?> = prefs.all.values

    fun getAllKeys(): MutableSet<String> = prefs.all.keys

    fun removeModel(key: String) {
        val editor = prefs.edit()
        editor.remove(key).apply()
    }

    fun removeSearchKeyword() {
        val editor = searchPrefs.edit()
        editor.remove(SEARCH_PREFS_NAME).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
        orderingPrefs.edit().clear().apply()
    }

    companion object {
        const val MODEL_PREFS_NAME = "prefs"
        const val ORDERING_PREFS_NAME = "ordering"
        const val SEARCH_PREFS_NAME = "searchKey"
    }
}