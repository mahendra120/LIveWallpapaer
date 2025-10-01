package com.example.livewallpapaer.wallpapercategory

import java.io.Serializable

data class WallpaperResponse(
    val home: List<String>? = null,
    val tree: List<String>? = null,
    val sea: List<String>? = null,
    val sky: List<String>? = null,
    val car: List<String>? = null,
    val mountains: List<String>? = null,
    val animol: List<String>? = null,
    val anime: List<String>? = null,
)

data class LikeItem(
    val key: String,
    val url: String
)
