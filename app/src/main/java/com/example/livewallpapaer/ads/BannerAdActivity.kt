package com.example.livewallpapaer.ads

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.livewallpapaer.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class BannerAdActivity : ComponentActivity() {

    var coin by mutableStateOf(0)
    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null


    private val TAG = "====="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        LoadinterstitialAd()
//        loadInterstitialAd()

        setContent {
            Scaffold(modifier = Modifier.Companion.fillMaxSize(), topBar = {
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.img),
                        contentDescription = null,
                        modifier = Modifier.Companion.size(30.dp)
                    )
                    Text("$coin")
                }
            }) { innerPadding ->

                Box(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    val context = LocalContext.current
                    MyScreenWithNativeAd(context = context)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                )
                {
                    Column {
                        BannerAdView(modifier = Modifier.padding(innerPadding))
                        Button(
                            onClick = { showInterstitialAd() }, modifier = Modifier.align(
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Text("Show Interstitial Ad")
                        }
                    }
                    Column {
                        BannerAdView(modifier = Modifier.padding(innerPadding))
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { showinterstitialAd() }, modifier = Modifier.align(
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Text("Show Rewarded Ad")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MyScreenWithNativeAd(context: Context) {
        var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

        DisposableEffect(Unit) {
            val adLoader = AdLoader.Builder(
                context,
                "ca-app-pub-3940256099942544/2247696110"
            )
                .forNativeAd { ad ->
                    nativeAd = ad
                }
                .build()

            adLoader.loadAd(AdRequest.Builder().build())

            onDispose {
                nativeAd?.destroy()
            }
        }

        nativeAd?.let { ad ->
            NativeAdTemplate(nativeAd = ad)
        } ?: run {
            Text("Loading Ad...")
        }
    }

    @Composable
    fun NativeAdTemplate(nativeAd: NativeAd) {
        Card(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.Companion.padding(16.dp)) {
                Text(
                    text = "Ad",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.Companion.padding(bottom = 4.dp)
                )
                nativeAd.headline?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.Companion.padding(bottom = 8.dp)
                    )
                }
                nativeAd.images.firstOrNull()?.drawable?.let { drawable ->
                    AsyncImage(
                        model = drawable,
                        contentDescription = null,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Companion.Crop
                    )
                }
                nativeAd.body?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.Companion.padding(bottom = 8.dp)
                    )
                }
                nativeAd.callToAction?.let {
                    Button(onClick = {
                        nativeAd.performClick(nativeAd.extras) // tells AdMob this is a click
                    }) {
                        Text(text = it)
                    }
                }
            }
        }
    }


    @Composable
    fun BannerAdView(modifier: Modifier = Modifier.Companion) {
        val context = LocalContext.current
        val adView = remember {
            AdView(context).apply {
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // test banner
                setAdSize(AdSize.BANNER) // or adaptive size if needed
            }
        }

        LaunchedEffect(Unit) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { adView }
        )
    }


    fun LoadinterstitialAd() {
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    rewardedAd = null
                }
            },
        )
    }

    fun showinterstitialAd() {
        rewardedAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    rewardedAd = null
                    LoadinterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    rewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "Ad was clicked.")
                }
            }
        rewardedAd?.show(
            this,
            OnUserEarnedRewardListener { rewardItem ->
                Log.d(TAG, "User earned the reward.")

                // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                coin += 5
                Log.d(TAG, "User earned the reward. type: $rewardType amount: $")
            },
        )
    }


    fun loadInterstitialAd() {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                }
            },
        )
    }

    fun showInterstitialAd() {
        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    interstitialAd = null

                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    interstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdClicked() {
                    Log.d(TAG, "Ad was clicked.")
                }
            }
        interstitialAd?.show(this)
    }

}