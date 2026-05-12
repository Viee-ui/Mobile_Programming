package com.example.lap8.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lap8.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tạo tài khoản mới", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Điền đầy đủ thông tin để bắt đầu",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Nhập lại mật khẩu") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
                            }

                            password != confirmPassword -> {
                                Toast.makeText(context, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show()
                            }

                            password.length < 6 -> {
                                Toast.makeText(context, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                            firebaseAuth.signOut()
                                            navController.navigate(Screen.SignIn.route) {
                                                popUpTo(Screen.SignUp.route) { inclusive = true }
                                                launchSingleTop = true
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                it.exception?.message ?: "Đăng ký thất bại",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Đăng ký")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Quay lại đăng nhập")
                }
            }
        }
    }
}