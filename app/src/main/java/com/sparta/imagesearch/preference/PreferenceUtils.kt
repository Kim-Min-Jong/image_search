package com.sparta.imagesearch.preference

import android.app.Activity
import android.content.Context

import com.sparta.imagesearch.data.model.IntegratedModel
import org.json.JSONArray
import org.json.JSONException

class PreferenceUtils(context: Context) {
    private val prefName = "prefs"
    private val prefs = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE)

    var model:String?
        get() = prefs.getString("session", null)
        set(value){
            prefs.edit().putString("session", value).apply()
        }

    fun setModel(key: String, value: IntegratedModel?) {
        val jsonArray = JSONArray()
        val editor = prefs.edit()
        jsonArray.run {
            put(value?.thumbnailUrl)
            put(value?.title)
            put(value?.dateTime)
            put(value?.width)
            put(value?.height)
            put(value?.isLiked)
        }
        if(value != null){
            editor.putString(key, jsonArray.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getModel(key: String) : List<String> {
        // json 값을 받아와 리스트로 바꿀 준비
        val jsonList = prefs.getString(key, null)
        val list = ArrayList<String>()
        jsonList?.let { json ->
            try {
                val jsonArray = JSONArray(json)
                for(i in 0 until jsonArray.length()) {
                    val str = jsonArray.optString(i)
                    list.add(str)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }

    fun removeModel(key: String) {
        val editor = prefs.edit()
        editor.remove(key).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}