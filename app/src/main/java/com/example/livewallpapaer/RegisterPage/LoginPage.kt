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
import androidx.compose.ui.tooling.preview.Preview
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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginPage : ComponentActivity() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@LoginPage, MainActivity::class.java)
            startActivity(intent)
            finish()
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
    @Preview(showSystemUi = true)
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
                    LoginActivity()
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Row {
                    Icon(
                        Google,
                        contentDescription = null,
                        tint = Color(0xFFFF8C00),
                        modifier = Modifier
                            .padding(top = 4.dp)
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

    fun LoginActivity() {
        val auth = Firebase.auth
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("====", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this@LoginPage, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("====", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        } else {
            Toast.makeText(this@LoginPage, "feli all box", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun googleLogin() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id)) // must be WEB client ID
            .setFilterByAuthorizedAccounts(false) // allow all accounts
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            try {
                // Try to get credentials (Sign-In)
                val result = credentialManager.getCredential(this@LoginPage, request)
                handleSignIn(result.credential)

            } catch (e: GetCredentialException) {
                Log.e("====", "No saved credentials found: ${e.localizedMessage}")

                try {
                    val signUpRequest = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val signUpResult =
                        credentialManager.getCredential(this@LoginPage, signUpRequest)
                    handleSignIn(signUpResult.credential)
                } catch (e2: GetCredentialException) {
                    Log.e("====", "User canceled or no accounts: ${e2.localizedMessage}")
                    Toast.makeText(
                        this@LoginPage,
                        "No Google account available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w("====", "Credential is not of type Google ID!")
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val auth = Firebase.auth
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("=====", "signInWithCredential:success")
                val user = auth.currentUser
                Toast.makeText(this@LoginPage, "Login success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginPage, MainActivity::class.java)
                startActivity(intent)
            } else {
                // If sign in fails, display a message to the user
                Log.w("=====", "signInWithCredential:failure", task.exception)
                Toast.makeText(this@LoginPage, "Login failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
