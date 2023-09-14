package com.sparta.imagesearch.preference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.sparta.imagesearch.data.model.IntegratedModel
import com.sparta.imagesearch.extension.GsonExtension.gsonToIntegrateModel

class PreferenceUtils(context: Context) {
    private val prefName = "prefs"
    private val prefs = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE)
    val pref: SharedPreferences
        get() = prefs

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

    fun getAllModels(): MutableCollection<out Any?> {
        return prefs.all.values
    }

    fun removeModel(key: String) {
        val editor = prefs.edit()
        editor.remove(key).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}