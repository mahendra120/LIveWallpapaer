package com.example.livewallpapaer.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.database

object AppPref {

    private var sp: SharedPreferences? = null

    private fun init(context: Context) {
        if (sp != null) return
        sp = context.getSharedPreferences("lecture", MODE_PRIVATE)
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        init(context)
        return sp!!.getInt(key, defaultValue)
    }

    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        init(context)
        return sp!!.getString(key, defaultValue) ?: defaultValue
    }

    fun setString(context: Context, key: String, value: String = "") {
        init(context)
        sp!!.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun setInt(context: Context, key: String, value: Int = 0) {
        init(context)
        sp!!.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun addInt(context: Context, key: String, value: Int = 0) {
        init(context)
        sp!!.edit().apply {
            putInt(key, getInt(context, key) + value)
            apply()
        }
    }

    fun getIntLiveData(context: Context, key: String, defaultValue: Int = 0): LiveData<Int> {
        init(context)
        return sp!!.intLiveData(key, defaultValue)
    }
}