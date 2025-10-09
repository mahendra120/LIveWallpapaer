package com.example.livewallpapaer.category

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import coil.compose.rememberAsyncImagePainter
import com.example.livewallpapaer.R
import com.example.livewallpapaer.RegisterPage.LoginPage
import com.example.livewallpapaer.ads.text
import com.example.livewallpapaer.ui.theme.montserrat
import com.example.livewallpapaer.util.AppPref
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun settingpage(context: Context) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val name = AppPref.getString(context = context, key = "name")
    val surname = AppPref.getString(context = context, key = "surname")
    val email = AppPref.getString(context = context, key = "email")

    val coroutineScope = rememberCoroutineScope()

    selectedImageUri = AppPref.getString(context = context, key = "imageuri").let {
        if (it.isNotEmpty()) Uri.parse(it) else null
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = it
            AppPref.setString(context = context, key = "imageuri", value = it.toString())
        }
    }
    val user = FirebaseAuth.getInstance().currentUser
    user?.displayName
    user?.email

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    onClick = {
                        if (user?.photoUrl == null) {
                            launcher.launch("image/*")
                        }
                    },
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(50)),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, color = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    val photoUrl = user?.photoUrl
                    if (photoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(photoUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                        )
                    } else {
                        if (selectedImageUri == null) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(65.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                selectedImageUri?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
                if (user?.displayName == null) {
                    Text(
                        "surname : $surname",
                        fontFamily = montserrat,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    if (user?.displayName == null) "name :  $name" else "name : ${user.displayName}",
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    if (user?.email == null) "Email :  $email" else "Email : ${user.email}",
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.padding(15.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, color = Color.White)
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Column(Modifier.clickable { expanded = !expanded }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "about",
                                fontFamily = montserrat,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(15.dp)
                            )
                            Image(
                                painter = painterResource(R.drawable.info),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    Color.White
                                )
                            )
                        }
                        AnimatedVisibility(expanded) {
                            Text(
                                text = text,
                                fontFamily = montserrat,
                                color = Color.White,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center, modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
                if (user?.displayName == null) {
                    Card(
                        onClick = {
                            val intent = Intent(context, LoginPage::class.java)
                            intent.putExtra("login", true)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 17.dp)
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.signuppage),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val credentialManager = CredentialManager.create(context)
                                    val clearRequest = ClearCredentialStateRequest()
                                    credentialManager.clearCredentialState(clearRequest)
                                    FirebaseAuth.getInstance().signOut()
                                    Log.d("SignOut", "User signed out successfully.")
                                    val intent = Intent(context, LoginPage::class.java)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Log.e("SignOut", "Error: ${e.localizedMessage}")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, color = Color.White)
                    ) {
                        Text(
                            text = "Sign Out",
                            fontFamily = montserrat,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(7.dp),
                            style = androidx.compose.ui.text.TextStyle(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFEFE223), // yellow
                                        Color(0xFFFF8C00), // orange
                                        Color(0xFFFF4500)  // red
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
