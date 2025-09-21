package com.example.livewallpapaer

import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import pl.droidsonroids.gif.GifDrawable

class GifWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return GifEngine()
    }

    inner class GifEngine : Engine() {
        private var gifDrawable: GifDrawable? = null
        private var handler = Handler(Looper.getMainLooper())
        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                handler.postDelayed(this, 16) // ~60fps
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            try {
                // Replace with your GIF, from assets or file.
                gifDrawable = GifDrawable(assets, "wallpaper.gif")
            } catch (e: Exception) {
                e.printStackTrace()
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
            if (visible) {
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onDestroy() {
            handler.removeCallbacks(drawRunnable)
            gifDrawable?.recycle()
            super.onDestroy()
        }
    }
}
