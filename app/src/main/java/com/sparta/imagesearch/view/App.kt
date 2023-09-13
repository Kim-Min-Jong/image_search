package com.sparta.imagesearch.view

import android.app.Application
import com.sparta.imagesearch.preference.PreferenceUtils

class App: Application() {
    override fun onCreate() {
        prefs = PreferenceUtils(applicationContext)
        super.onCreate()
    }
    companion object {
        lateinit var prefs: PreferenceUtils
    }
}