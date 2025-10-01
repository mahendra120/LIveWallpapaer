package com.example.livewallpapaer

import android.util.Log
import com.example.livewallpapaer.wallpapercategory.LikeItem
import com.example.livewallpapaer.wallpapercategory.WallpaperResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Firebaserealtime {
    private val db = FirebaseDatabase.getInstance().getReference("wallpaper")
    fun getwallpaper(onResult: (WallpaperResponse?) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            Log.d("---0-0-0-00-", "getwallpaper:  ${snapshot.value}")
            val data = snapshot.getValue(WallpaperResponse::class.java)
            Log.d("====", "getwallpaper: $data")
            onResult(data)
        }.addOnFailureListener {
            onResult(null)
        }
    }


    fun getUserLikes(onResult: (List<LikeItem>) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().reference
            .child("userslike").child(uid).child("likes")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likes = mutableListOf<LikeItem>()
                for (child in snapshot.children) {
                    val url = child.getValue(String::class.java)
                    val key = child.key
                    if (url != null && key != null) {
                        likes.add(LikeItem(key, url))
                    }
                }
                onResult(likes)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

