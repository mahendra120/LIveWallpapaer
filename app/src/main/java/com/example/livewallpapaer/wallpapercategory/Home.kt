package com.example.livewallpapaer.wallpapercategory

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.livewallpapaer.viewwallpapper
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var myColor = Color.Yellow

val rainbowColors = listOf(
    Color(0xFF0F2027), // Deep Navy
    Color(0xFF203A43), // Steel Blue
    Color(0xFF2C5364), // Teal Gray
    Color(0xFF4B79A1), // Cool Blue
    Color(0xFF283E51)  // Space Black
)

@Composable
fun HomePage(modifier: Modifier = Modifier, context: Context, home: List<String>?) {
    var isRefreshing by remember { mutableStateOf(false) }
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
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            home?.let { list ->
                items(list) { url ->
                    if (url.endsWith(".gif")) {
                        Card(
                            onClick = {
                                val intent = Intent(context, viewwallpapper::class.java)
                                intent.putExtra("url", url)
                                context.startActivity(intent)
                            },
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .size(height = 335.dp, width = Dp.Unspecified)
                                .border(
                                    1.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = rainbowColors
                                    ),
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
                                Icons()
                            }
                        }
                    } else {
                        Card(
                            onClick = {
                                val intent = Intent(context, viewwallpapper::class.java)
                                intent.putExtra("url", url)
                                context.startActivity(intent)
                            },
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .size(height = 335.dp, width = Dp.Unspecified)
                                .border(
                                    1.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = rainbowColors
                                    ),
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
    }
}