package com.example.livewallpapaer.RegisterPage

import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.composables.Google
import com.example.livewallpapaer.MainActivity
import com.example.livewallpapaer.R
import com.example.livewallpapaer.ui.theme.quicksand
import com.example.livewallpapaer.util.AppPref
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.core.Context
import com.google.firebase.database.database
import kotlinx.coroutines.launch

class LoginPage : ComponentActivity() {
    var isloadingandcolor by mutableStateOf(false)
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            gotoHomePage()
        }
    }
    fun gotoHomePage() {
        val currentUser = Firebase.auth.currentUser
        val database = Firebase.database.getReference("users").child(currentUser!!.uid)
        val initialCoin = 10
        database.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                database.setValue(initialCoin)
                AppPref.setInt(this@LoginPage, "coin", initialCoin)
            } else {
                val coins = snapshot.getValue(Int::class.java) ?: 0
                AppPref.setInt(this@LoginPage, "coin", coins)
            }
            val intent = Intent(this@LoginPage, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener { exception ->
            Toast.makeText(this@LoginPage, "${exception.message}", Toast.LENGTH_SHORT).show()
        }
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
        Image(
            painter = painterResource(R.drawable.signupscreenpage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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
                    isloadingandcolor = true
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

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun googleLogin() {
        val credentialManager = CredentialManager.create(this) // ✅ initialize here

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id)) // ✅ use Web client ID
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@LoginPage, // use Activity context
                    request = request
                )

                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                Log.e("=====", "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w("====", "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = Firebase.auth // ✅ must initialize
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("=====", "signInWithCredential:success")
                    val user = auth.currentUser
                    gotoHomePage()
                } else {
                    Log.w("====", "signInWithCredential:failure", task.exception)
                }
            }
    }
}
