package com.example.livewallpapaer

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpenAdManager(val context: Context) {

    val LOG_TAG = "====="

    val TAG = "===="
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false

    /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
    private var loadTime: Long = 0

    init {
        loadAd()
    }

    fun loadAd() {
        Log.d(TAG, "loadAd: ++++++++++++++++++++=====")
        AppOpenAd.load(
            context,
            "ca-app-pub-3940256099942544/9257395921",
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    // Called when an app open ad has loaded.
                    Log.d(LOG_TAG, "App open ad loaded.")

                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Called when an app open ad has failed to load.
                    Log.d(LOG_TAG, "App open ad failed to load with error: " + loadAdError.message)

                    isLoadingAd = false
                }
            },
        )
    }

    fun showAdIfAvailable(activity: Activity) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.d(TAG, "The app open ad is already showing.")
            return
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (appOpenAd == null) {
            Log.d(TAG, "The app open ad is not ready yet.")
            return
        }
        isShowingAd = true
        appOpenAd?.show(activity)
        appOpenAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when full screen content is dismissed.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    appOpenAd = null
                    isShowingAd = false

                    // Load an ad.
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when full screen content failed to show.
                    Log.d(TAG, adError.message)
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    appOpenAd = null
                    isShowingAd = false

                    // Load an ad.
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "The ad recorded an impression.")
                }

                override fun onAdClicked() {
                    // Called when ad is clicked.
                    Log.d(TAG, "The ad was clicked.")
                }
            }
        appOpenAd?.show(activity)
    }

    /** Check if ad was loaded more than n hours ago. */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        // For time interval details, see: https://support.google.com/admob/answer/9341964
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

}