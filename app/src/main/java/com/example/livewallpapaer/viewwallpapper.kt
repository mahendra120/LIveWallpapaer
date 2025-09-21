package com.example.livewallpapaer

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

enum class wallpaperType {
    HOME, LOCK, BOTH
}

class viewwallpapper : ComponentActivity() {
    var url: String? = null
    var likebutton by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        url = intent.getStringExtra("url")
        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { mytopbar() },
                bottomBar = {
                    BottomAppBarWithAd()
                }) { innerPadding ->
                Box(modifier = Modifier.padding()) {
                    wallpapershow()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun wallpapershow() {
        val bottomSheetState = rememberModalBottomSheetState()
        var isShowBottomSheet by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radiusX = 5.dp,
                    radiusY = 5.dp,
                    edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp)),
                ),
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 790.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp, end = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            )
            {
                Icon(
                    Icons.Default.Download, contentDescription = null
                )
            }
            Button(
                onClick = {
                    isShowBottomSheet = true
                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp, end = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    painter = painterResource(R.drawable.wallpaper),
                    contentDescription = "wallpaper", modifier = Modifier.size(27.dp)
                )
            }
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 4.dp, end = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "wallpaper", modifier = Modifier.size(27.dp)
                )
            }
        }
        if (isShowBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                isShowBottomSheet = false
            }, sheetState = bottomSheetState) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Button(
                        onClick = {
                            url?.let { imageUrl ->
                                scope.launch {
                                    val bitmap = loadBitmapFromUrl(context, imageUrl)
                                    if (bitmap != null) {
                                        setMyWallpaper(bitmap, wallpaperType.HOME)
                                    }
                                }
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.home),
                            contentDescription = "Home",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            url?.let { imageUrl ->
                                scope.launch {
                                    val bitmap = loadBitmapFromUrl(context, imageUrl)
                                    if (bitmap != null) {
                                        setMyWallpaper(bitmap, wallpaperType.LOCK)
                                    }
                                }
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.lock),
                            contentDescription = "Lock",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            url?.let { imageUrl ->
                                scope.launch {
                                    val bitmap = loadBitmapFromUrl(context, imageUrl)
                                    if (bitmap != null) {
                                        setMyWallpaper(bitmap, wallpaperType.BOTH)
                                    }
                                }
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.wallpaper),
                            contentDescription = "Wallpaper",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mytopbar() {
        TopAppBar(
            title = {}, actions = {
                Box(modifier = Modifier.padding(end = 15.dp, top = 15.dp)) {
                    Button(
                        onClick = {
                            likebutton = !likebutton
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(45.dp)
                    ) {
                        if (likebutton) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(27.dp)
                            )
                        } else {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = null, tint = Color.Black,
                                modifier = Modifier.size(27.dp)
                            )
                        }
                    }
                }
            }, navigationIcon = {
                Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
                    Button(
                        onClick = { finish() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )
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

    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    private fun setMyWallpaper(bitmap: Bitmap, type: wallpaperType) {
        val wm = WallpaperManager.getInstance(this@viewwallpapper)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // Calculate scale while keeping aspect ratio
        val bitmapRatio = bitmap.width.toFloat() / bitmap.height
        val screenRatio = screenWidth.toFloat() / screenHeight

        val scaledBitmap: Bitmap = if (bitmapRatio > screenRatio) {
            val height = screenHeight
            val width = (height * bitmapRatio).toInt()
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        } else {
            val width = screenWidth
            val height = (width / bitmapRatio).toInt()
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        }

        // Create a bitmap with screen size and draw the scaled bitmap centered
        val finalBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(finalBitmap)
        val left = (screenWidth - scaledBitmap.width) / 2f
        val top = (screenHeight - scaledBitmap.height) / 2f
        canvas.drawBitmap(scaledBitmap, left, top, null)

        // Set wallpaper
        when (type) {
            wallpaperType.HOME -> wm.setBitmap(finalBitmap, null, true, WallpaperManager.FLAG_SYSTEM)
            wallpaperType.LOCK -> wm.setBitmap(finalBitmap, null, true, WallpaperManager.FLAG_LOCK)
            wallpaperType.BOTH -> wm.setBitmap(finalBitmap)
        }
    }

    suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false) // needed to get Bitmap
            .build()
        val result = (loader.execute(request) as? SuccessResult)?.drawable
        return (result as? BitmapDrawable)?.bitmap
    }
}
