package com.example.livewallpapaer.RegisterPage

import android.R.attr.showText
import android.content.Intent
import android.credentials.GetCredentialException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.composables.Google
import com.example.livewallpapaer.MainActivity
import com.example.livewallpapaer.R
import com.example.livewallpapaer.ui.theme.quicksand
import com.example.livewallpapaer.util.AppPref
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.io.path.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class LoginPage : ComponentActivity() {
    var isloadingandcolor by mutableStateOf(false)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var lodinganimation by mutableStateOf(false)
    var TAG = "===="
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001


    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            gotoHomePage()
            lodinganimation = true
        }
    }

    fun gotoHomePage() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            Log.w("gotoHomePage", "Current user is null")
            return
        }
        val database = Firebase.database.getReference("users").child(currentUser.uid)
        val initialCoin = 10
        database.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                database.setValue(initialCoin).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AppPref.setInt(this@LoginPage, "coin", initialCoin)
                        Log.d("gotoHomePage", "New user: initial coins saved successfully")
                        navigateToMain()
                    } else {
                        Log.e("gotoHomePage", "Failed to save initial coins", task.exception)
                        Toast.makeText(this, "Failed to save coins", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val coins = try {
                    snapshot.getValue(Int::class.java) ?: 0
                } catch (e: Exception) {
                    Log.e("gotoHomePage", "Error reading coins", e)
                    0
                }
                AppPref.setInt(this@LoginPage, "coin", coins)
                Log.d("gotoHomePage", "Existing user: coins loaded = $coins")
                navigateToMain()
            }
        }.addOnFailureListener { exception ->
            Log.e("gotoHomePage", "Database get failed", exception)
            Toast.makeText(
                this@LoginPage,
                "Database error: ${exception.message ?: "Unknown error"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@LoginPage, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScrenn()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @Composable
    fun LoginScrenn() {

        val hasInternet = remember { mutableStateOf(isInternetAvailable(this)) }

        LaunchedEffect(Unit) {
            while (true) {
                hasInternet.value = isInternetAvailable(this@LoginPage)
                delay(1000)
            }
        }

        Log.d("90909090", "LoginScrenn: Internet connected")
        Image(
            painter = painterResource(R.drawable.signupscreenpage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (hasInternet.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Login",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFD700), // Yellow
                                Color(0xFFFF8C00), // Orange
                                Color(0xFFFF4500)  // Reddish Orange
                            )
                        )
                    ),
                    fontFamily = quicksand,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 25.dp))
                OutlinedTextField(
                    value = email, onValueChange = { email = it }, leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = Color(253, 221, 94, 250),
                        )
                    }, placeholder = {
                        Text(
                            "email", fontFamily = quicksand, color = Color(0xFFFF8C00)
                        )
                    }, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF8C00),
                        unfocusedBorderColor = Color(0xFFFF8C00),
                        cursorColor = Color(253, 221, 94, 250),
                        unfocusedContainerColor = Color(0, 0, 0),
                        focusedTextColor = Color(253, 253, 253),
                        unfocusedTextColor = Color(253, 253, 253),
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                OutlinedTextField(
                    value = password, onValueChange = { password = it }, leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(253, 221, 94, 250),
                        )
                    }, placeholder = {
                        Text(
                            "password", fontFamily = quicksand, color = Color(0xFFFF8C00)
                        )
                    }, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF8C00),
                        unfocusedBorderColor = Color(0xFFFF8C00),
                        cursorColor = Color(253, 221, 94, 250),
                        unfocusedContainerColor = Color(0, 0, 0),
                        focusedTextColor = Color(253, 253, 253),
                        unfocusedTextColor = Color(253, 253, 253),
                    )
                )
                Spacer(modifier = Modifier.padding(vertical = 25.dp))
                Button(
                    onClick = {
                        LoginActivity(this@LoginPage)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 63.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF34505))
                ) {
                    Text(
                        "Login",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = quicksand,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                Button(
                    onClick = {
                        googleLogin()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 63.dp)
                        .height(50.dp),
                    colors = if (isloadingandcolor) {
                        ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    } else {
                        ButtonDefaults.buttonColors(containerColor = Color.White)
                    }
                ) {
                    if (isloadingandcolor) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 6.dp,
                            color = Color.Red
                        )
                    } else {
                        Row {
                            Icon(
                                Google,
                                contentDescription = null,
                                tint = Color(0xFFFF8C00),
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(end = 10.dp))
                            Text(
                                "Google",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = quicksand, color = Color.Black
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                TextButton(onClick = {
                    val intent = Intent(this@LoginPage, SignupPage::class.java)
                    startActivity(intent)
                }) {
                    Text(
                        buildAnnotatedString {
                            append("Haven't Acoun? ")
                            withStyle(
                                style = SpanStyle(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFFD700), // Yellow
                                            Color(0xFFFF8C00), // Orange
                                            Color(0xFFFF4500)  // Reddish Orange
                                        )
                                    )
                                )
                            ) {
                                append("Register")
                            }
                        }, fontFamily = quicksand, color = Color.White
                    )
                }
            }
            if (lodinganimation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    loadinanimation("https://rklraibyrjctiuqhnyxj.supabase.co/storage/v1/object/public/wallpaperapp/project/From%20KlickPin%20CF%20Pin%20on%20Quick%20Saves.mp4")
                }
            }
        }
        else {
            Log.d("90909090", "LoginScrenn: no Internet connected")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.nonet),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        "No Internet Connection",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = quicksand,
                        color = Color.White
                    )
                }
            }
        }
    }

    fun LoginActivity(context: android.content.Context) {
        val auth = Firebase.auth
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                        gotoHomePage()
                    } else {
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // must be WEB client id
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // Unified entry point for your Google button
    fun googleLogin() {
        // choose new API only on Android 14+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            googleLoginNewAPIWithFallback()
        } else {
            googleLoginOldAPI()
        }
    }
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun googleLoginNewAPIWithFallback() {
        try {
            val credentialManager = CredentialManager.create(this)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = this@LoginPage,
                        request = request
                    )
                    handleNewApiCredential(result.credential)
                } catch (e: GetCredentialException) {
                    Log.e(
                        TAG,
                        "GetCredentialException: ${e.localizedMessage}\n${e.stackTraceToString()}"
                    )
                    // Automatic fallback to old API if new API fails
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginPage,
                            "Google sign-in (new API) failed — trying fallback...",
                            Toast.LENGTH_SHORT
                        ).show()
                        googleLoginOldAPI()
                    }
                } catch (t: Throwable) {
                    Log.e(
                        TAG,
                        "Unexpected error in new API: ${t.localizedMessage}\n${t.stackTraceToString()}"
                    )
                    runOnUiThread {
                        googleLoginOldAPI()
                    }
                }
            }
        } catch (e: Throwable) {
            // If anything at all goes wrong creating the manager, fallback.
            Log.e(
                TAG,
                "Failed to start new API flow: ${e.localizedMessage}\n${e.stackTraceToString()}"
            )
            googleLoginOldAPI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun handleNewApiCredential(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken)
            } else {
                Log.e(TAG, "New API returned null idToken")
                Toast.makeText(this, "Google sign-in failed (no id token).", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Credential type not supported by this flow.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun googleLoginOldAPI() {
        try {
            // ensure googleSignInClient is initialized (call setupGoogleSignInClient in onCreate)
            if (!::googleSignInClient.isInitialized) setupGoogleSignInClient()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } catch (t: Throwable) {
            Log.e(TAG, "googleLoginOldAPI failed: ${t.localizedMessage}\n${t.stackTraceToString()}")
            Toast.makeText(this, "Cannot start Google Sign-In", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Log.e(TAG, "GoogleSignIn: idToken is null; account=$account")
                    Toast.makeText(this, "Google sign-in failed (no token).", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: ApiException) {
                Log.w(TAG, "GoogleSignIn ApiException code=${e.statusCode}: ${e.localizedMessage}")
                Toast.makeText(this, "Google sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT)
                    .show()
            } catch (t: Throwable) {
                Log.e(
                    TAG,
                    "Unexpected onActivityResult error: ${t.localizedMessage}\n${t.stackTraceToString()}"
                )
            }
        }
    }

    /** Shared Firebase auth step */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    gotoHomePage()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Firebase authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: android.content.Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = cm.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }
}

@Composable
fun loadinanimation(mp4Uri: String) {
    val context = LocalContext.current

    val rawResourceUri = "android.resource://${context.packageName}/${R.raw.sharingan}"

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(rawResourceUri)
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}






