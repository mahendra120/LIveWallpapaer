package com.example.livewallpapaer.RegisterPage

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livewallpapaer.R
import com.example.livewallpapaer.ui.theme.quicksand
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SignupPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignUp()
        }
    }

    @Composable
    fun SignUp() {
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var Password by remember { mutableStateOf("") }

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
            text(name, { name = it }, "Name", Icons.Default.AccountCircle)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            text(surname, { surname = it }, "Surname", Icons.Default.Person)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            text(email, { email = it }, "Email", Icons.Default.Email)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            text(Password, { Password = it }, "password", Icons.Default.Lock)
            Spacer(modifier = Modifier.padding(vertical = 22.dp))
            Button(
                onClick = {
                    SignUp1(this@SignupPage, email, Password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 65.dp)
                    .height(50.dp),
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
                    val intent = Intent(this@SignupPage, LoginPage::class.java)
                    startActivity(intent)
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

    fun SignUp1(context: Context, email: String, password: String) {
        val auth = Firebase.auth
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SIGNUP", "createUserWithEmail:success")
                        Toast.makeText(context, "Signup Success", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
        }
    }


    @Composable
    fun text(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        leadingIcon: ImageVector
    ) {
        OutlinedTextField(
            value = value, onValueChange = onValueChange, leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color(253, 221, 94, 250),
                )
            }, placeholder = {
                Text(
                    label, fontFamily = quicksand, color = Color(253, 253, 253)
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
    }
}
