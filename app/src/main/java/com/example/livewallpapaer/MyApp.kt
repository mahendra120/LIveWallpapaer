package com.example.livewallpapaer

import android.app.Application
import com.example.livewallpapaer.ads.AdManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AdManager.getInstance(this@MyApp)
    }
}