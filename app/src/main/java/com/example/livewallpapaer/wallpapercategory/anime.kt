package com.example.livewallpapaer.wallpapercategory

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.livewallpapaer.viewwallpapper
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun animepage(modifier: Modifier = Modifier, context: Context, anime: List<String>?) {
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
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            anime?.let { list ->
                items(list) { url ->
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
                                color = Color.Yellow.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
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
                                is coil.compose.AsyncImagePainter.State.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()  
                                            .padding(0.dp)
                                            .size(20.dp).align(Alignment.Center),
                                        strokeWidth = 3.dp,
                                        color = Color.Yellow
                                    )
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