package com.example.livewallpapaer.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManager private constructor(context: Context) {

    val interstitialAdManager = InterstitialAdManager(context)
    private val rewardedAdManager = RewardedAdManager(context)

    init {
        loadAd()
    }

    fun loadAd() {
        interstitialAdManager.loadAd()
        rewardedAdManager.loadAd()
    }

    fun showInterstitialAd(onAdClosed: () -> Unit) {
        interstitialAdManager.showAd(onAdClosed)
    }

    fun showRewardedAd(onRewardEarned: () -> Unit) {
        rewardedAdManager.showAd(onRewardEarned)
    }

    companion object {
        @Volatile
        private var instance: AdManager? = null

        fun getInstance(context: Context): AdManager {
            return instance ?: synchronized(this) {
                instance ?: AdManager(context.applicationContext).also { instance = it }
            }
        }
    }
}


class InterstitialAdManager(private val context: Context) {
    private var interstitialAd: InterstitialAd? = null

    fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d("Interstitial", "Ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    Log.d("Interstitial", "Failed: ${error.message}")
                    Log.d("Interstitial", "Error code: ${error.code}")
                }
            }
        )
    }
    fun isAdLoaded(): Boolean = interstitialAd != null
    fun showAd(onAdClosed: () -> Unit) {
        val activity = context as? Activity ?: run {
            onAdClosed()
            return
        }
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onAdClosed()
                    interstitialAd = null
                    loadAd() // reload for next time
                }
            }
            ad.show(activity)
        } ?: run {
            onAdClosed()
            loadAd()
        }
    }
}


class RewardedAdManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null

    fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("Rewarded", "Ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    Log.d("Rewarded", "Failed: ${error.message}")
                }
            }
        )
    }

    fun showAd(onRewardEarned: () -> Unit) {
        val activity = context as? Activity ?: run {
            onRewardEarned()
            return
        }

        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadAd() // reload next ad
                }
            }
            ad.show(activity) {
                // Reward user
                onRewardEarned()
            }
        } ?: run {
            onRewardEarned()
            loadAd()
        }
    }
}


