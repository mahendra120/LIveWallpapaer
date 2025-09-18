package com.example.livewallpapaer.RegisterPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.example.livewallpapaer.R
import com.example.livewallpapaer.ui.theme.quicksand
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignupPage : ComponentActivity() {
    var name by mutableStateOf("")
    var surname by mutableStateOf("")
    var email by mutableStateOf("")
    var Password by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignUp()
        }
    }

    @Composable
    @Preview(showSystemUi = true)
    fun SignUp() {
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
            Spacer(modifier = Modifier.padding(top = 25.dp))
            Text(
                "Signup",
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
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it }, leadingIcon = {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "name", fontFamily = quicksand, color = Color(253, 253, 253)
                    )
                }, shape = RoundedCornerShape(20.dp), // bigger radius
                colors = OutlinedTextFieldDefaults.colors(
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
                value = surname, onValueChange = { surname = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "surname", fontFamily = quicksand, color = Color(253, 253, 253)
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
                value = email, onValueChange = { email = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "email", fontFamily = quicksand, color = Color(253, 253, 253)
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
                value = Password, onValueChange = { Password = it }, leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(253, 221, 94, 250),
                    )
                }, placeholder = {
                    Text(
                        "password", fontFamily = quicksand, color = Color(253, 253, 253)
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
                    SignUp1(email, Password)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 65.dp).height(50.dp) ,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF34505))
            ) {
                Text(
                    "Sing up",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = quicksand,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            TextButton(
                onClick = {
                    finish()
                },
                modifier = Modifier,
            ) {
                Text(
                    buildAnnotatedString {
                        append("Have Account? ")
                        withStyle(
                            style = SpanStyle(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFFD700), // Yellow
                                        Color(0xFFFF8C00), // Orange
                                        Color(0xFFFF4500)  // Reddish Orange
                                    )
                                )
                            ),
                        ) {
                            append("Login")
                        }
                    }, fontFamily = quicksand, color = Color.White
                )
            }
        }
    }

    fun SignUp1(email: String, password: String) {
        val auth = Firebase.auth
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("======", "createUserWithEmail:success")
                        startActivity(Intent(this, LoginPage::class.java))
                        finish()
                    } else {
                        Log.w("======", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this@SignupPage, "feli all box", Toast.LENGTH_SHORT).show()
        }
    }
}
