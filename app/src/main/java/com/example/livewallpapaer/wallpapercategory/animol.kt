package com.example.livewallpapaer.wallpapercategory

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.livewallpapaer.viewwallpapper

@Composable
fun animolpage(modifier: Modifier = Modifier, context: Context, animol: List<String>?) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
        animol?.let { list ->
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
                        .border(
                            1.dp,
                            color = Color.Yellow.copy(.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .decoderFactory(GifDecoder.Factory()) // 👈 this makes GIF animate
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}