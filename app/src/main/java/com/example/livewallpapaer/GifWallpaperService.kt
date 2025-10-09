package com.example.livewallpapaer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PorterDuff
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
    override fun onCreateEngine(): Engine = GifEngine()

    inner class GifEngine : Engine() {
        private var gifDrawable: GifDrawable? = null
        private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
        private val client = OkHttpClient()
        private val handler = Handler(Looper.getMainLooper())
        private val matrix = Matrix()
        private var surfaceWidth = 0
        private var surfaceHeight = 0

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                handler.postDelayed(this, 16) // 60 FPS
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            loadGif()
        }

        private fun loadGif() {
            val sharedPreferences = this@GifWallpaperService.getSharedPreferences(
                "wallpaper_prefs",
                Context.MODE_PRIVATE
            )
            val gifUrl = sharedPreferences.getString("gif_url", null)

            if (gifUrl != null) {
                loadGifFromUrl(gifUrl)
            } else {
                loadLocalGif()
            }
        }

        private fun loadGifFromUrl(url: String) {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val request = Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "Mozilla/5.0")
                        .build()

                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        response.body?.bytes()?.let { gifBytes ->
                            val tempFile = File(this@GifWallpaperService.cacheDir, "wallpaper.gif")
                            FileOutputStream(tempFile).use { it.write(gifBytes) }

                            withContext(Dispatchers.Main) {
                                gifDrawable = GifDrawable(tempFile)
                                updateScaling()

                                if (isVisible) handler.post(drawRunnable)
                            }
                        }
                    } else loadLocalGif()
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadLocalGif()
                }
            }
        }

        private fun loadLocalGif() {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val inputStream = assets.open("default_wallpaper.gif")
                    val tempFile = File(this@GifWallpaperService.cacheDir, "local_gif.gif")
                    FileOutputStream(tempFile).use { fos ->
                        inputStream.copyTo(fos)
                    }

                    withContext(Dispatchers.Main) {
                        gifDrawable = GifDrawable(tempFile)
                        updateScaling()

                        if (isVisible) handler.post(drawRunnable)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun updateScaling() {
            gifDrawable?.let { gif ->
                val gifWidth = gif.intrinsicWidth
                val gifHeight = gif.intrinsicHeight

                if (surfaceWidth > 0 && surfaceHeight > 0 && gifWidth > 0 && gifHeight > 0) {
                    val scaleX = surfaceWidth.toFloat() / gifWidth
                    val scaleY = surfaceHeight.toFloat() / gifHeight
                    val scale = scaleX.coerceAtLeast(scaleY)

                    matrix.reset()
                    matrix.postScale(scale, scale)

                    val scaledWidth = gifWidth * scale
                    val scaledHeight = gifHeight * scale
                    val translateX = (surfaceWidth - scaledWidth) / 2
                    val translateY = (surfaceHeight - scaledHeight) / 2
                    matrix.postTranslate(translateX, translateY)
                }
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            surfaceWidth = width
            surfaceHeight = height
            updateScaling()
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                canvas?.let { c ->
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    c.save()
                    c.concat(matrix)
                    gifDrawable?.draw(c)
                    c.restore()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                canvas?.let { holder.unlockCanvasAndPost(it) }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                gifDrawable?.start()
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
                gifDrawable?.stop()
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            handler.removeCallbacks(drawRunnable)
            super.onSurfaceDestroyed(holder)
        }

        override fun onDestroy() {
            handler.removeCallbacks(drawRunnable)
            coroutineScope.cancel()
            gifDrawable?.recycle()
            super.onDestroy()
        }
    }
}