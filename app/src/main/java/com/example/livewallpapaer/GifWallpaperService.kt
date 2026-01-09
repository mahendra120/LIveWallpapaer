package com.example.livewallpapaer

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.io.FileOutputStream

class GifWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return GifEngine()
    }
    inner class GifEngine : Engine() {
        private var gifDrawable: GifDrawable? = null
        private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
        private val client = OkHttpClient()
        private val handler = Handler(Looper.getMainLooper())

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                handler.postDelayed(this, 11) // ~60fps
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)

            val sharedPreferences = this@GifWallpaperService.getSharedPreferences(
                "wallpaper_prefs",
                Context.MODE_PRIVATE
            )
            val gifUrl = sharedPreferences.getString("gif_url", null)

            if (gifUrl != null) {
                loadGifFromUrl(gifUrl)
            } else {

            }
        }

        private fun loadGifFromUrl(url: String) {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        val inputStream = response.body?.byteStream()
                        if (inputStream != null) {
                            val tempFile = File(cacheDir, "temp_gif.gif")
                            FileOutputStream(tempFile).use { fos ->
                                inputStream.copyTo(fos)
                            }
                            withContext(Dispatchers.Main) {
                                gifDrawable = GifDrawable(tempFile.absolutePath)
                                if (isVisible) {
                                    handler.post(drawRunnable)  
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            val canvas = holder.lockCanvas()
            if (canvas != null && gifDrawable != null) {
                gifDrawable!!.setBounds(0, 0, canvas.width, canvas.height)
                gifDrawable!!.draw(canvas)
                holder.unlockCanvasAndPost(canvas)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            if (visible && gifDrawable != null) {
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onDestroy() {
            handler.removeCallbacks(drawRunnable)
            coroutineScope.cancel()
            gifDrawable?.recycle()
            super.onDestroy()
        }
    }
}

