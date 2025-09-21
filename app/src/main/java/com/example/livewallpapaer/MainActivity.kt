package com.example.livewallpapaer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.livewallpapaer.category.ProfilePage
import com.example.livewallpapaer.category.settingpage
import com.example.livewallpapaer.ui.theme.quicksand
import com.example.livewallpapaer.wallpapercategory.CardPage
import com.example.livewallpapaer.wallpapercategory.HomePage
import com.example.livewallpapaer.wallpapercategory.SkyPage
import com.example.livewallpapaer.wallpapercategory.WallpaperResponse
import com.example.livewallpapaer.wallpapercategory.animepage
import com.example.livewallpapaer.wallpapercategory.animolpage
import com.example.livewallpapaer.wallpapercategory.mountainsPage
import com.example.livewallpapaer.wallpapercategory.seaPage
import com.example.livewallpapaer.wallpapercategory.treepage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var showsearchbox by mutableStateOf(false)
    var nevigetionlist by mutableStateOf("Home")
    var search by mutableStateOf(false)

    var wallpapers by mutableStateOf<WallpaperResponse?>(null)

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState, drawerContent = {
                    MyDrawer()
                }) {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    Topbar(drawerState, scope)
                }, bottomBar = {
                    BottomAppBarWithAd()
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
                                settingpage()
                                search = false
                                scope.launch { drawerState.close() }
                            }

                            "Person" -> {
                                ProfilePage()
                                search = false
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
        val coroutineScope = rememberCoroutineScope()
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                // Use coroutine scope
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

        val list = listOf("home", "tree", "mountains", "car", "sea", "sky", "anime", "animol")

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyRow {
                items(list.size) { index ->
                    val name = list[index]
                    Button(
                        onClick = { filterwallpaper = name },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color(239, 226, 35, 255),
                            containerColor = Color.Transparent
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
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = quicksand
                        )
                    }
                }
            }
            if (showsearchbox) {
                mySearchBar()
            }
            when (filterwallpaper) {
                "home" -> HomePage(
                    modifier = Modifier,
                    context = this@MainActivity,
                    wallpapers?.home
                )

                "tree" -> treepage(modifier = Modifier, this@MainActivity, wallpapers?.tree)
                "sea" -> seaPage(modifier = Modifier, this@MainActivity, wallpapers?.sea)
                "car" -> CardPage(modifier = Modifier,  this@MainActivity, wallpapers?.car)
                "mountains" -> mountainsPage(modifier = Modifier, context = this@MainActivity, wallpapers?.mountains)
                "sky" -> SkyPage(modifier = Modifier, this@MainActivity, wallpapers?.sky)
                "animol" -> animolpage(modifier = Modifier, this@MainActivity, wallpapers?.animol)
                "anime" -> animepage(modifier = Modifier, this@MainActivity, wallpapers?.anime)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mySearchBar() {
        var searchQuery by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val allWallpapers = listOfNotNull(
            wallpapers?.home,
            wallpapers?.tree,
            wallpapers?.sea,
            wallpapers?.sky,
            wallpapers?.car,
            wallpapers?.mountains,
            wallpapers?.animol,
            wallpapers?.anime
        ).flatten()
        val filteredItems = if (searchQuery.isEmpty()) {
            allWallpapers
        } else {
            allWallpapers.filter { it.contains(searchQuery, ignoreCase = true) }
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { active = false },
            shape = RoundedCornerShape(12.dp),
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp, vertical = 5.dp)
                .border(
                    .05.dp, brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFEFE223), // Yellow
                            Color(0xFFFF8C00), // Orange
                            Color(0xFFFF4500)  // Reddish Orange
                        )
                    ), shape = RoundedCornerShape(12.dp)
                ),
            windowInsets = WindowInsets(
                left = 0.dp, right = 0.dp, top = 0.dp, bottom = 0.dp
            ),
            placeholder = {
                Text(
                    "Search",
                    fontFamily = quicksand,
                    color = Color.White,
                    fontSize = 18.sp,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.Yellow
                )
            },
            trailingIcon = {
                if (active) IconButton(onClick = {
                    searchQuery = ""
                    active = false
                }, colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Yellow)) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color.Black
            ),
            tonalElevation = 0.dp,
        ) {
//            LazyColumn {
//                item {
//                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
//                        items(filteredItems) { url ->
//                            Card(
//                                modifier = Modifier.padding(6.dp),
//                                onClick = {
//                                    val intent = Intent(context, Showwallpaper::class.java)
//                                    intent.putExtra("url", url)
//                                    context.startActivity(intent)
//                                }
//                            ) {
//                                AsyncImage(
//                                    model = url,
//                                    contentDescription = null,
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier.fillMaxSize()
//                                )
//                            }
//                        }
//                    }
//
//                }
//            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Topbar(drawerState: DrawerState, scope: CoroutineScope) {
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
                IconButton(onClick = {
                    showsearchbox = !showsearchbox
                    Log.d("======", "Topbar: $showsearchbox")
                }) {
//                    if (search) {
//                        if (!showsearchbox) {
//                            Icon(
//                                Icons.Default.Search,
//                                contentDescription = null,
//                                tint = Color.Yellow,
//                                modifier = Modifier
//                                    .size(40.dp)
//                                    .padding(end = 15.dp)
//                            )
//                        } else {
//                            Icon(
//                                Icons.Default.Close,
//                                contentDescription = null,
//                                tint = Color.Yellow,
//                                modifier = Modifier
//                                    .size(40.dp)
//                                    .padding(end = 15.dp)
//                            )
//                        }
//                    }
                }
            },
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
        NavItem("Person", Icons.Default.Person)
    )


    @Composable
    fun MyDrawer() {
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
                    ), textAlign = TextAlign.Center
                )
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp))
            navItemList.forEachIndexed { index, item ->
                NavigationDrawerItem(label = {
                    Text(
                        text = item.label,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = quicksand,
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
            HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp))
            Text(
                "category",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 20.dp, top = 15.dp),
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFD700), // Yellow
                            Color(0xFFFF8C00), // Orange
                            Color(0xFFFF4500)  // Reddish Orange
                        )
                    ), textAlign = TextAlign.Center
                )
            )
        }
    }

    @Composable
    fun BottomAppBarWithAd() {
        val context = LocalContext.current
        val adView = remember {
            AdView(context).apply {
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // Test Banner
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context,
                        AdSize.FULL_WIDTH
                    )
                )
            }
        }

        LaunchedEffect(Unit) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { adView }
        )
    }
}
