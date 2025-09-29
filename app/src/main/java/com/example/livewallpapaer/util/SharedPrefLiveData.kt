package com.example.livewallpapaer.util

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class SharedPrefLiveData(
    private val sp: SharedPreferences,
    private val key: String,
    private val defValue: Int
) : LiveData<Int>() {

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
        if (changedKey == key) {
            value = prefs.getInt(key, defValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = sp.getInt(key, defValue) // initial value
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        sp.unregisterOnSharedPreferenceChangeListener(listener)
    }
}

fun SharedPreferences.intLiveData(key: String, defValue: Int = 0): LiveData<Int> {
    return SharedPrefLiveData(this, key, defValue)
}

