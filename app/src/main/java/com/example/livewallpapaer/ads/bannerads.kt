package com.example.livewallpapaer.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.livewallpapaer.ads_off_on
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BottomAppBarWithAd(context: Context) {
    if (ads_off_on) {
        val adView = remember {
            AdView(context).apply {
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // Test Banner
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context, AdSize.FULL_WIDTH
                    )
                )
            }
        }

        LaunchedEffect(Unit) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth(), factory = { adView })
    }
}