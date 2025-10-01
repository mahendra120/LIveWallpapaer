package com.example.livewallpapaer.ads

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.livewallpapaer.MainActivity
import com.example.livewallpapaer.PremiumActivity
import com.example.livewallpapaer.R
import com.example.livewallpapaer.ui.theme.quicksand
import com.example.livewallpapaer.util.AppPref
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

var checkhome by mutableStateOf(false)

class AdsScreen : ComponentActivity() {

    private var rewardedAd: RewardedAd? = null
    private val TAG = "RewardedAdTest"

    var isShowExitAlert by mutableStateOf(false)

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loadRewardedAd()
        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(), topBar = {
                    Topbar()
                }, bottomBar = {
                    BottomAppBarWithAd(this@AdsScreen)
                }, containerColor = Color.Black
            ) { innerPadding ->
                if (isShowExitAlert) {
                    ShowDialog()
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color.Black)
                ) {
                    RewardAds()
                }
            }
        }
    }

    @Composable
    fun RewardAds() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                onClick = {
                    val intent = Intent(this@AdsScreen, PremiumActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .height(300.dp)
                    .weight(0.5f)
                    .padding(8.dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    2.dp,
                    Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFC107)))
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A).copy(alpha = 0.8f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp, end = 20.dp, start = 20.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.crown),
                        contentDescription = null,
                        modifier = Modifier
                            .size(125.dp)
                            .padding(top = 10.dp)
                    )
                    Text(
                        "PREMIUM",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
                            ),
                            shadow = Shadow(
                                color = Color(0xFFFFD700),
                                offset = Offset(2f, 2f),
                                blurRadius = 8f
                            )
                        )
                    )

                    Text(
                        "Unlock all features & remove ads",
                        fontSize = 15.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Card(
                onClick = { isShowExitAlert = true },
                modifier = Modifier
                    .height(300.dp)
                    .weight(0.5f)
                    .padding(8.dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    2.dp,
                    Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFC107)))
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A).copy(alpha = 0.8f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 35.dp, end = 20.dp, start = 20.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.ads),
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 5.dp)
                    )
                    Text(
                        "WATCH ADS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFFD700), Color(0xFFFF8C00))
                            ),
                            shadow = Shadow(
                                color = Color(0xFFFFD700),
                                offset = Offset(2f, 2f),
                                blurRadius = 8f
                            )
                        )
                    )
                    Text(
                        "Earn free coins instantly",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this, "ca-app-pub-3940256099942544/5224354917", // Test Rewarded ID
            AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded ✅")
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e(TAG, "Ad failed to load ❌: ${adError.message}")
                    rewardedAd = null
                }
            })
    }

    private fun showRewardedAd() {
        if (rewardedAd == null) {
            Toast.makeText(this, "Ad not ready please wait!", Toast.LENGTH_SHORT).show()
            loadRewardedAd()
            return
        }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed")
                rewardedAd = null
                loadRewardedAd() // preload again

                addRewaredToFirebase()

            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e(TAG, "Ad failed to show ❌: ${adError.message}")
                rewardedAd = null
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen")
            }
        }
        rewardedAd?.show(this) { rewardItem ->
            AppPref.addInt(this@AdsScreen, "coin")
            val userId = Firebase.auth.currentUser?.uid ?: return@show
            Log.d("?=======", "showRewardedAd: $userId")
            updateCoin(userId)
            isShowExitAlert = false
        }
    }

    private fun addRewaredToFirebase() {
        val currentUser = Firebase.auth.currentUser!!
        val database = Firebase.database.getReference("users").child(currentUser.uid)
        val currentCoin = AppPref.getInt(this@AdsScreen, "coin")
        database.setValue(currentCoin + 1)
            .addOnSuccessListener {
                AppPref.setInt(this@AdsScreen, "coin", currentCoin + 1)
            }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Topbar() {
        val coin by AppPref.getIntLiveData(this@AdsScreen, "coin", 10).observeAsState(10)
        TopAppBar(
            title = {
                Text(
                    "Live Wallpaper",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = quicksand,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFD700), // Yellow
                                Color(0xFFFF8C00), // Orange
                                Color(0xFFFF4500)  // Reddish Orange
                            )
                        )
                    ),
                    modifier = Modifier.padding(start = 17.dp)
                )
            },
            actions = {
                Button(
                    onClick = {

                    },
                    modifier = Modifier.padding(bottom = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Row(modifier = Modifier.padding(end = 0.dp)) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier
                                .size(27.dp)
                                .padding(top = 7.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.imageicon),
                            contentDescription = null,
                            modifier = Modifier.size(33.dp)
                        )
                        Log.d("======", "Topbar: $coin")
                        if (coin >= 100) {
                            Text(
                                text = "${coin}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = quicksand,
                                color = Color.White,
                                modifier = Modifier.padding(start = 3.dp, top = 5.dp)
                            )
                        } else {
                            Text(
                                text = "${coin}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = quicksand,
                                color = Color.White,
                                modifier = Modifier.padding(start = 3.dp, top = 5.dp)
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
            navigationIcon = {
                IconButton(onClick = {
                    checkhome = true
                    val intent = Intent(this@AdsScreen, MainActivity::class.java)
                    intent.putExtra("name", "Home")
                    startActivity(intent)
                    finish()
                }, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            })
    }

    //update coin code
    fun updateCoin(userId: String) {
        val coin = AppPref.getInt(this@AdsScreen, "coin")
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.child(userId).child("coin").setValue(coin)
            .addOnSuccessListener {
                Log.d("FB111", "Coin updated = $coin")
                isShowExitAlert = false
            }
            .addOnFailureListener {
                Log.e("FB", "Failed to update coin", it)
            }
    }


    @Composable
    fun ShowDialog() {
        Dialog(
            onDismissRequest = { isShowExitAlert = false }
        ) {
            Card(
                modifier = Modifier
                    .size(320.dp, 380.dp)
                    .border(
                        1.dp,
                        color = Color.Yellow.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .shadow(12.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF121212) // Dark background
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Icon
                    Image(
                        painter = painterResource(R.drawable.imageicon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .padding(top = 8.dp)
                    )
                    Text(
                        text = "Watch an Ad & Earn 1 Coin",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = quicksand,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFFD700), // Gold
                                    Color(0xFFFF8C00), // Orange
                                    Color(0xFFFF4500)  // Reddish Orange
                                )
                            )
                        )
                    )
                    Text(
                        text = "Get free rewards by watching a short ad.",
                        fontSize = 16.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )

                    // Buttons Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showRewardedAd() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700)
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.imageicon), // your coin icon
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Get", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { isShowExitAlert = false },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

}
