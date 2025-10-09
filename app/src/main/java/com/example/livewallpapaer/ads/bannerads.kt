package com.example.livewallpapaer.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.livewallpapaer.ads_off_on
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

var text =
    "Welcome to our Live Wallpaper app, a modern and creative way to bring new life to your smartphone. Your phone is not just a tool; it’s something you use every day, and it deserves to look as unique and personal as you are. That’s why we created this app — to help you make your screen more beautiful, lively, and expressive. Our team has built a large collection of high-quality live wallpapers designed to move, glow, and inspire. Each wallpaper is made carefully to enhance the look of your screen while keeping performance smooth and battery usage minimal.\n" +
            "\n" +
            "When you use the same static background every day, your phone can start to feel dull and boring. Live wallpapers change that feeling completely. They add motion, color, and creativity to your home screen. You can enjoy soft flowing animations, glowing lights, or artistic designs that match your style and mood. Every time you unlock your phone, you will see something refreshing and alive. We have designed these wallpapers to work perfectly on all types of devices, whether it’s a new flagship phone or a simple budget model. They look beautiful without slowing down your device or using too much power.\n" +
            "\n" +
            "Our app is built with simplicity in mind. We understand that users prefer apps that are clean, fast, and easy to use. That’s why our interface is simple, friendly, and focused only on what really matters — personalization. You can browse, preview, and set wallpapers in just a few taps. There are no complicated menus or confusing buttons. Whether you are a tech expert or a beginner, you can use our app effortlessly. The goal is to make personalization quick, enjoyable, and accessible for everyone.\n" +
            "\n" +
            "Every wallpaper in our collection is designed to inspire creativity and express individuality. Some wallpapers are calm and peaceful, perfect for people who love minimal and relaxing visuals. Others are energetic and colorful, made for those who enjoy dynamic and glowing effects. You can explore nature themes, abstract art, neon lights, space designs, or calming animations — there’s something for everyone. We keep updating the collection regularly, so every time you open the app, you’ll find something new to enjoy.\n" +
            "\n" +
            "Performance and battery life are very important for us. Many people worry that live wallpapers will make their phone slow or use too much battery, but our app is built with smart technology that keeps everything efficient. The wallpapers pause automatically when the screen is off or when the wallpaper is not visible. This means they only use energy when needed, keeping your battery safe and your phone smooth. Even if you use our wallpapers all day, your device will still run fast and cool.\n" +
            "\n" +
            "Privacy is another key part of our mission. We know that users care deeply about how their information is used. That’s why we are very clear about our privacy policy. Our Live Wallpaper app does not collect, store, or share any personal information. We do not track your usage, location, or activity. Everything you do inside the app stays completely private and secure on your own device. We believe that trust is the foundation of every good app, and we promise to keep your experience safe, honest, and transparent.\n" +
            "\n" +
            "We also have great respect for artists and copyright rules. All wallpapers and designs inside the app are either created by our own design team or used with full permission from the original creators. We never steal or copy someone else’s work. If an artwork is made by another artist, we make sure they are credited properly. Our goal is to support creativity while promoting fairness and originality. We believe that respecting creators makes our app more meaningful and ethical.\n" +
            "\n" +
            "The app follows a simple design philosophy — simplicity is beauty. From the moment you open it, you’ll notice that everything feels light, smooth, and clean. You can find wallpapers easily, preview them instantly, and set them with one tap. There are no unwanted ads or unnecessary steps that waste your time. The app is designed to give you a smooth, enjoyable experience every time you use it.\n" +
            "\n" +
            "Personalization is at the heart of our Live Wallpaper app. Your phone is something you look at hundreds of times every day — when you text, call, work, or relax. Why not make it reflect your personality? With our live wallpapers, you can completely change the mood of your device in seconds. If you want calm energy, choose soft motion backgrounds. If you love excitement, pick bright and glowing animations. If you prefer elegance, go for minimal and stylish designs. You have full freedom to choose what represents you best.\n" +
            "\n" +
            "Our promise to every user is simple — we will always keep improving. We will continue to add new wallpapers, improve performance, and make the app even easier to use. We listen carefully to user feedback because we want this app to feel perfect for everyone. Our mission is to give you a safe, stylish, and fun way to make your smartphone truly yours.\n" +
            "\n" +
            "The Live Wallpaper app is not just another wallpaper app; it’s a creative platform where technology meets art. It gives your phone motion, life, and color. It makes your everyday experience more personal and inspiring. Whether you love simplicity or creativity, this app is designed for you. Every tap, every wallpaper, and every glow is made with care. We invite you to explore, personalize, and make your screen come alive with beauty, motion, and emotion.\n" +
            "\n" +
            "With our Live Wallpaper app, you can transform your phone into a moving piece of art — something that reflects your taste, your style, and your imagination. It’s safe, smooth, and effortless. So go ahead, explore our growing collection, find your favorite wallpapers, and make your phone truly yours every single day."

@Composable
fun BottomAppBarWithAd(context: Context) {
    if (ads_off_on) {
        val adView = remember {
            AdView(context).apply {
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // Test Banner
                setAdSize(
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                        context, AdSize.FULL_WIDTH
                    )
                )
            }
        }

        LaunchedEffect(Unit) {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp), factory = { adView })
    }
}