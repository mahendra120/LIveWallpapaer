package com.example.livewallpapaer.wallpapercategory

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.livewallpapaer.Firebaserealtime.getUserLikes
import com.example.livewallpapaer.R
import com.example.livewallpapaer.viewwallpapper

@Composable
fun Like(context: Context, modifier: Modifier) {
    var likewallpaperlist by remember { mutableStateOf<List<LikeItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        getUserLikes { list ->
            likewallpaperlist = list
            Log.d("0098787", "Like: ${likewallpaperlist}")
        }
    }

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
        items(likewallpaperlist) { item ->
            val url = item.url
            val key = item.key

            Log.d("0088667767", "Like: $key")
            if (url.endsWith(".gif")) {
                Card(
                    onClick = {
                        val intent = Intent(context, viewwallpapper::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("key", key)
                        context.startActivity(intent)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .size(height = 335.dp, width = Dp.Unspecified)
                        .border(
                            1.dp,
                            color = Color.Yellow.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                )
                {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .decoderFactory(GifDecoder.Factory())
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .size(50.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .align(Alignment.Center),
                                            strokeWidth = 4.dp,
                                            color = myColor
                                        )
                                    }
                                }

                                else -> SubcomposeAsyncImageContent(alignment = Alignment.Center)
                            }
                        }

                        Icon(
                            painter = painterResource(R.drawable.crown),
                            contentDescription = null,
                            modifier = Modifier
                                .size(45.dp)
                                .align(Alignment.TopEnd)
                                .padding(end = 5.dp),
                            tint = Color.Yellow
                        )
                    }
                }
            } else {
                Card(
                    onClick = {
                        val intent = Intent(context, viewwallpapper::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("key", key)
                        context.startActivity(intent)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .size(height = 335.dp, width = Dp.Unspecified)
                        .border(
                            1.dp,
                            color = Color.Yellow.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                )
                {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .decoderFactory(GifDecoder.Factory()) // For GIF support
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .size(50.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.Center),
                                        strokeWidth = 4.dp,
                                        color = myColor
                                    )
                                }
                            }

                            else -> SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }
    }
}
