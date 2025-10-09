package com.example.livewallpapaer

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.livewallpapaer.ads.BottomAppBarWithAd
import com.example.livewallpapaer.util.AppPref
import com.example.livewallpapaer.wallpapercategory.myColor
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

enum class wallpaperType {
    HOME, LOCK, BOTH
}

class viewwallpapper : ComponentActivity() {

    var url: String? = null
    var key: String? = null
    var Like by mutableStateOf(false)

    var TAG = "======"

    private var rewardedAd: RewardedAd? = null

    var isDownloading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Like = key != null

        loadRewardedAd()
        url = intent.getStringExtra("url")
        key = intent.getStringExtra("key")
        Log.d("-----09", "onCreate: $key")

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = { mytopbar() }, bottomBar = {
                BottomAppBarWithAd(this@viewwallpapper)
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

        val coin by AppPref.getIntLiveData(this@viewwallpapper, "coin", 10).observeAsState(10)
        Log.d("====303", "wallpapershow: $coin")
        val sharedPreferences =
            context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("gif_url", url)
        editor.apply()
        if (isDownloading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
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
                model = ImageRequest.Builder(LocalContext.current).data(url)
                    .decoderFactory(GifDecoder.Factory()).build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(9.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 795.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    showRewardedAd()
                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp, end = 5.dp)
                    .width(95.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.Black
                ),
                border = BorderStroke(.4.dp, color = myColor)
            )
            {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text("Save", fontSize = 12.sp)
                }
            }
            Button(
                onClick = {
                    isShowBottomSheet = true
                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp, end = 5.dp)
                    .width(95.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.Black
                ),
                border = BorderStroke(.4.dp, color = myColor)
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.wallpaper),
                        contentDescription = "wallpaper",
                        modifier = Modifier.size(20.dp)
                    )
                    Text("wallpaper", fontSize = 12.sp)
                }
            }
            Button(
                onClick = {
                    shareImage(this@viewwallpapper, url!!)
                },
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 5.dp, end = 5.dp)
                    .width(95.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.Black
                ),
                border = BorderStroke(.4.dp, color = myColor)
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "wallpaper",
                        modifier = Modifier.size(20.dp)
                    )
                    Text("wallpaper", fontSize = 12.sp)
                }
            }
        }

        if (isShowBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                isShowBottomSheet = false
            }, containerColor = Color.Black.copy(.8f), sheetState = bottomSheetState) {
                if (url!!.endsWith(".gif")) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (coin >= 3) {
                                    val user = FirebaseAuth.getInstance().currentUser
                                    AppPref.setInt(this@viewwallpapper, "coin", coin - 3)
                                    if (user != null) {
                                        updateCoinInFirebase(user.uid, coin)
                                    }
                                } else {
                                    Toast.makeText(this@viewwallpapper, "----", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    64, 224, 208
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .padding(start = 10.dp, end = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.imageicon),
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    "3",
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 0.dp)
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    "Set wallpaper", fontSize = 22.sp, textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
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
                            modifier = Modifier.width(110.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            border = BorderStroke(.5.dp, color = Color.Yellow)
                        ) {
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.home),
                                    contentDescription = "Home",
                                    colorFilter = ColorFilter.tint(Color.White.copy()),
                                    modifier = Modifier.size(30.dp)
                                )
                                Text("Home")
                            }
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
                            modifier = Modifier.width(110.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            border = BorderStroke(.5.dp, color = Color.Yellow)
                        ) {
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.lock),
                                    contentDescription = "Lock",
                                    colorFilter = ColorFilter.tint(Color.White),
                                    modifier = Modifier.size(28.dp)
                                )
                                Text("Lock")
                            }
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
                            modifier = Modifier.width(110.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            border = BorderStroke(.5.dp, color = Color.Yellow)
                        ) {
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.wallpaper),
                                    contentDescription = "Wallpaper",
                                    colorFilter = ColorFilter.tint(Color.White),
                                    modifier = Modifier.size(30.dp)
                                )
                                Text("both")
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mytopbar() {
        TopAppBar(
            title = {
                Box(modifier = Modifier.padding(start = 20.dp)) {
                    if (url!!.endsWith(".gif")) {
                        Row {
                            Image(
                                painter = painterResource(R.drawable.crown1),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 5.dp, top = 15.dp)
                            )
                            Text(
                                "Premium",
                                fontSize = 30.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier.padding(top = 15.dp)
                            )
                        }
                    }
                }
            }, actions = {
                Box(modifier = Modifier.padding(end = 15.dp, top = 15.dp)) {
                    Button(
                        onClick = {
                            if (key == null) {
                                if (Like) {
                                    removeLike(key.toString())
                                } else {
                                    addLike(url.toString())
                                }
                            } else {
                                removeLike(key.toString())
                                finish()
                            }
                            Like = !Like
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = .4f)
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(45.dp)
                    ) {
                        if (key == null) {
                            if (Like) {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(27.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(27.dp)
                                )
                                Like = false
                            }
                        } else {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color.Red,
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
                            containerColor = Color.White.copy(alpha = .4f)
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
            wallpaperType.HOME -> wm.setBitmap(
                finalBitmap, null, true, WallpaperManager.FLAG_SYSTEM
            )

            wallpaperType.LOCK -> wm.setBitmap(
                finalBitmap, null, true, WallpaperManager.FLAG_LOCK
            )

            wallpaperType.BOTH -> wm.setBitmap(finalBitmap)
        }
    }

    suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? {
        val loader = ImageLoader(context)
        val request =
            ImageRequest.Builder(context).data(url).allowHardware(false) // needed to get Bitmap
                .build()
        val result = (loader.execute(request) as? SuccessResult)?.drawable
        return (result as? BitmapDrawable)?.bitmap
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
            Toast.makeText(this, "please wait!", Toast.LENGTH_SHORT).show()
            loadRewardedAd()
            return
        }
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed")
                rewardedAd = null
                loadRewardedAd() // preload again
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
            if (url!!.endsWith(".gif")) {
                isDownloading = true
                saveGifToGallery(this@viewwallpapper, url!!)
            } else {
                isDownloading = true
                downloadWallpaper(this@viewwallpapper, url!!)
            }
        }
    }

    fun downloadWallpaper(context: Context, imageUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    isDownloading = true
                }

                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val fileName = "wallpaper_${System.currentTimeMillis()}.jpg"
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )

                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }

                MediaScannerConnection.scanFile(
                    context, arrayOf(file.absolutePath), arrayOf("image/jpeg"), null
                )

                withContext(Dispatchers.Main) {
                    isDownloading = false
                    Toast.makeText(context, "Download successful: $fileName", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    isDownloading = false
                    Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    //updateConinln
    fun updateCoinInFirebase(userId: String, newCoin: Int) {
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userId).setValue(newCoin).addOnSuccessListener {
            setLiveWallpaper(this@viewwallpapper)
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Failed to update coin: ${e.message}")
        }
    }

    //live wallpaper set in home and lock
    @SuppressLint("NewApi")
    fun setLiveWallpaper(context: Context) {
        val manager = WallpaperManager.getInstance(context)
        val service = ComponentName(context, GifWallpaperService::class.java)

        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, service)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            try {
                context.startActivity(intent)
                Toast.makeText(context, "Setting live wallpaper...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Unable to set wallpaper: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Live wallpaper not supported on this device.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //like
    fun addLike(url: String) {
        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: "no-user"
        val ref = database.child("userslike").child(uid).child("likes").push()
        key = ref.key
        ref.setValue(url)
    }

    //unlike
    fun removeLike(wallpaperId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: "no-user"

        val ref = database.child("userslike").child(uid).child("likes").child(wallpaperId)
        ref.removeValue()
        key = null
    }

    //save gif
    fun saveGifToGallery(context: Context, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    isDownloading = true
                }
                // Download file
                val connection = URL(url).openConnection()
                connection.connect()
                val input = connection.getInputStream()

                // File name
                val fileName = "gif_${System.currentTimeMillis()}.gif"

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/gif")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/MyGifs"
                    )
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                uri?.let {
                    val output = context.contentResolver.openOutputStream(it)
                    input.copyTo(output!!)
                    output.close()
                }

                withContext(Dispatchers.Main) {
                    isDownloading = false
                    Toast.makeText(context, "GIF saved to Gallery", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isDownloading = false
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // share gif
    fun shareImage(context: Context, url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Download file
                val connection = URL(url).openConnection()
                connection.connect()
                val input = connection.getInputStream()

                // Detect file extension
                val extension = when {
                    url.endsWith(".gif", true) -> ".gif"
                    url.endsWith(".png", true) -> ".png"
                    else -> ".jpg"
                }

                // Save in cache
                val file = File(context.cacheDir, "shared_${System.currentTimeMillis()}$extension")
                val output = FileOutputStream(file)
                input.copyTo(output)
                output.close()
                input.close()

                // Get URI with FileProvider
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )

                // MIME type
                val mimeType = when (extension) {
                    ".gif" -> "image/gif"
                    ".png" -> "image/png"
                    else -> "image/jpeg"
                }

                // Share intent
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = mimeType
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                withContext(Dispatchers.Main) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
