package com.example.livewallpapaer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livewallpapaer.ads.AdsScreen
import com.example.livewallpapaer.ui.theme.LIveWallpapaerTheme
import com.example.livewallpapaer.ui.theme.quicksand

class PremiumActivity : ComponentActivity() {

    var expanded1monthcard by mutableStateOf(false)
    var expanded6monthcard by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                PremiumScreen()
            }
        }
    }

    @Composable
    fun PremiumScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 28.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.crown1),
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(95.dp)
                        .padding(top = 32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Go Premium",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(17.dp))

                PremiumCard(
                    onClick = { expanded1monthcard = !expanded1monthcard },
                    colorDark = true,
                    month = "1 Month",
                    price = "59",
                    discountedPrice = "99",
                    expanded = expanded1monthcard,
                )
                Spacer(Modifier.padding(10.dp))
                PremiumCard(
                    onClick = { expanded6monthcard = !expanded6monthcard },
                    colorDark = false,
                    month = "6 Month",
                    price = "359",
                    discountedPrice = "599",
                    expanded = expanded6monthcard,
                )

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val intent = Intent(this@PremiumActivity, AdsScreen::class.java)
                        startActivity(intent)
                        finish()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Home", color = Color.White)
                }
            }
        }
    }

    @Composable
    fun PremiumCard(
        onClick: () -> Unit,
        colorDark: Boolean,
        month: String,
        price: String,
        discountedPrice: String,
        expanded: Boolean,
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = if (colorDark) Color(0xFF2C2C2C) else Color(0xFFFFD54F)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = month,
                        color = if (colorDark) Color.White else Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "-40% ",
                            color = if (colorDark) Color.White else Color.DarkGray.copy(0.8f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " ₹$discountedPrice ",
                            color = if (colorDark) Color.LightGray else Color.DarkGray.copy(0.8f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₹$price",
                            color = if (colorDark) Color.Yellow else Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)) {
                        Text(
                            "- No Ads",
                            color = Color.Yellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "- Unlimited Wallpapers",
                            color = Color.Yellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "- Exclusive Content",
                            color = Color.Yellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

