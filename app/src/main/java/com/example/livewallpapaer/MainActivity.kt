package com.example.livewallpapaer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.livewallpapaer.ads.AdsScreen
import com.example.livewallpapaer.ads.BottomAppBarWithAd
import com.example.livewallpapaer.ads.InterstitialAdManager
import com.example.livewallpapaer.ads.checkhome
import com.example.livewallpapaer.category.settingpage
import com.example.livewallpapaer.ui.theme.montserrat
import com.example.livewallpapaer.ui.theme.quicksand
import com.example.livewallpapaer.util.AppPref
import com.example.livewallpapaer.util.SharedPrefLiveData
import com.example.livewallpapaer.wallpapercategory.CardPage
import com.example.livewallpapaer.wallpapercategory.HomePage
import com.example.livewallpapaer.wallpapercategory.Like
import com.example.livewallpapaer.wallpapercategory.SkyPage
import com.example.livewallpapaer.wallpapercategory.WallpaperResponse
import com.example.livewallpapaer.wallpapercategory.animepage
import com.example.livewallpapaer.wallpapercategory.animolpage
import com.example.livewallpapaer.wallpapercategory.mountainsPage
import com.example.livewallpapaer.wallpapercategory.seaPage
import com.example.livewallpapaer.wallpapercategory.treepage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var ads_off_on by mutableStateOf(false)

class MainActivity : ComponentActivity() {

    var nevigetionlist: String? by mutableStateOf("Home")
    var search by mutableStateOf(false)
    var wallpapers by mutableStateOf<WallpaperResponse?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        if (checkhome) {
            nevigetionlist = intent.getStringExtra("name")
        }
        loadCoin()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState, drawerContent = {
                    MyDrawer()
                }) {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    Topbar(drawerState, scope, scrollBehavior)
                }, bottomBar = {
                    BottomAppBarWithAd(this@MainActivity)
                }, containerColor = Color.Black) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        when (nevigetionlist) {
                            "Home" -> {
                                Homepage()
                                search = true
                                scope.launch { drawerState.close() }
                            }

                            "Setting" -> {
                                settingpage(this@MainActivity)
                                search = false
                                scope.launch { drawerState.close() }
                            }

                            "Ads" -> {
                                val intent = Intent(this@MainActivity, AdsScreen::class.java)
                                startActivity(intent)
                                nevigetionlist = "Home"
                            }

                            "Like" -> {
                                Like(this@MainActivity, modifier = Modifier)
                                scope.launch { drawerState.close() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Homepage() {

        var filterwallpaper by remember { mutableStateOf("home") }
        var isRefreshing by remember { mutableStateOf(false) }
        val hasInternet = remember { mutableStateOf(isInternetAvailable(this)) }
        val coroutineScope = rememberCoroutineScope()
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                coroutineScope.launch {
                    delay(1500)
                    isRefreshing = false
                }
            }
        )
        {
            LaunchedEffect(Unit) {
                Firebaserealtime.getwallpaper {
                    wallpapers = it
                    Log.d("====", "Homepage: $wallpapers")
                }
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                hasInternet.value = isInternetAvailable(this@MainActivity)
                delay(3000) // 3 sec ma ek var check
            }
        }
        val list = listOf("home", "tree", "mountains", "car", "sea", "sky", "anime", "animal")
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyRow {
                items(list.size) { index ->
                    val name = list[index]
                    Button(
                        onClick = { filterwallpaper = name },
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color(
                                239,
                                226,
                                35,
                                255
                            ), containerColor = Color.Transparent
                        ),
                        border = BorderStroke(
                            1.dp,
                            Brush.horizontalGradient(
                                listOf(Color(0xFFEFE223), Color(0xFFFF8C00), Color(0xFFFF4500))
                            )
                        )
                    ) {
                        Text(
                            name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = montserrat
                        )
                    }
                }
            }
            if (hasInternet.value) {
                when (filterwallpaper) {
                    "home" -> HomePage(
                        modifier = Modifier,
                        context = this@MainActivity,
                        wallpapers?.home
                    )

                    "tree" -> treepage(modifier = Modifier, this@MainActivity, wallpapers?.tree)
                    "sea" -> seaPage(modifier = Modifier, this@MainActivity, wallpapers?.sea)
                    "car" -> CardPage(modifier = Modifier, this@MainActivity, wallpapers?.car)
                    "mountains" -> mountainsPage(
                        modifier = Modifier,
                        context = this@MainActivity,
                        wallpapers?.mountains
                    )

                    "sky" -> SkyPage(modifier = Modifier, this@MainActivity, wallpapers?.sky)
                    "animal" -> animolpage(
                        modifier = Modifier,
                        this@MainActivity,
                        wallpapers?.animol
                    )

                    "anime" -> animepage(modifier = Modifier, this@MainActivity, wallpapers?.anime)
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.nonet),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Text(
                            "No Internet Connection",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = quicksand,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    fun loadCoin() {
        val currentUser = Firebase.auth.currentUser!!
        val database = Firebase.database.getReference("users").child(currentUser.uid)
        database.get()
            .addOnSuccessListener {
                val coin = (it.value as? Long)?.toInt() ?: 0
                AppPref.setInt(this@MainActivity, "coin", coin)
            }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Topbar(
        drawerState: DrawerState,
        scope: CoroutineScope,
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "infinite")
        val color by infiniteTransition.animateColor(
            initialValue = Color.Green,
            targetValue = Color.Blue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "color"
        )

        val coin by AppPref.getIntLiveData(this@MainActivity, "coin", 10).observeAsState(10)
        Log.d("======", "Topbar: $coin")
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
                        nevigetionlist = "Ads"
                    },
                    modifier = Modifier.padding(bottom = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                )
                {
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
                            modifier = Modifier
                                .size(33.dp)
                        )
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
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)) {
                    Icon(Icons.Default.List, contentDescription = null)
                }
            })
    }

    data class NavItem(
        val label: String, val icon: ImageVector
    )

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Setting", Icons.Default.Settings),
        NavItem("Like", Icons.Default.FavoriteBorder),
        NavItem("Ads", Icons.Default.LiveTv)
    )

    @Composable
    fun MyDrawer() {
        val infiniteTransition = rememberInfiniteTransition(label = "infinite")
        val color by infiniteTransition.animateColor(
            initialValue = Color.Green,
            targetValue = Color.Blue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "color"
        )
        ModalDrawerSheet(drawerContentColor = Color.White, drawerContainerColor = Color.Black) {
            Text(
                "Live Wallpaper",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp),
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFD700), // Yellow
                            Color(0xFFFF8C00), // Orange
                            Color(0xFFFF4500)  // Reddish Orange
                        )
                    )
                ),
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp))
            navItemList.forEachIndexed { index, item ->
                NavigationDrawerItem(label = {
                    Text(
                        text = item.label,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = montserrat,
                        modifier = Modifier.padding(all = 5.dp),
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFFD700), // Yellow
                                    Color(0xFFFF8C00), // Orange
                                    Color(0xFFFF4500)  // Reddish Orange
                                )
                            )
                        )
                    )
                }, icon = {
                    Icon(item.icon, contentDescription = null, tint = Color.White)
                }, selected = false, onClick = {
                    nevigetionlist = item.label
                }, modifier = Modifier.padding(5.dp))
            }
        }
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = cm.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }

    fun SharedPreferences.intLiveData(key: String, defValue: Int = 0): LiveData<Int> {
        return SharedPrefLiveData(this, key, defValue)
    }
}
