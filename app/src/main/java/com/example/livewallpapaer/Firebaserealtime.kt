package com.example.livewallpapaer

import android.util.Log
import com.example.livewallpapaer.wallpapercategory.WallpaperResponse
import com.google.firebase.database.FirebaseDatabase

object Firebaserealtime {
    private val db = FirebaseDatabase.getInstance().getReference("wallpaper")
    fun getwallpaper(onResult: (WallpaperResponse?) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            Log.d("=====", "getwallpaper:  ${snapshot.value}")
            val data = snapshot.getValue(WallpaperResponse::class.java)
            Log.d("====", "getwallpaper: $data")
            onResult(data)
        }.addOnFailureListener {
            onResult(null)
        }
    }
}
