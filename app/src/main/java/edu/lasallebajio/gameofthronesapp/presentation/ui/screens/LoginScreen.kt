package edu.lasallebajio.gameofthronesapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import edu.lasallebajio.gameofthronesapp.datasources.services.AuthService
import edu.lasallebajio.gameofthronesapp.domain.dtos.AuthDto
import edu.lasallebajio.gameofthronesapp.domain.use_cases.SharedPref
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import edu.lasallebajio.gameofthronesapp.presentation.ui.utils.Visibility
import edu.lasallebajio.gameofthronesapp.presentation.ui.utils.Visibility_off
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun LoginScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF1A1A1A))
                )
            )
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Game of Thrones Quiz",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFE0E0E0), // Plateado
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFBDBDBD) // Gris claro
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB71C1C),
                    unfocusedBorderColor = Color(0xFF424242),
                    cursorColor = Color(0xFFB71C1C),
                    focusedLabelColor = Color(0xFFB71C1C),
                    unfocusedLabelColor = Color(0xFFBDBDBD)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFBDBDBD)
                    )
                },
                singleLine = true,
                visualTransformation =
                if (!isPasswordVisible) PasswordVisualTransformation()
                else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        val icon = if(!isPasswordVisible) Visibility else Visibility_off
                        Icon(imageVector = icon, contentDescription = "hide")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB71C1C),
                    unfocusedBorderColor = Color(0xFF424242),
                    cursorColor = Color(0xFFB71C1C),
                    focusedLabelColor = Color(0xFFB71C1C),
                    unfocusedLabelColor = Color(0xFFBDBDBD)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            try {
                                val authService = Retrofit.Builder()
                                    .baseUrl("http://68.183.30.211/api/v1/users/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(AuthService::class.java)

                                val loginDto = AuthDto(email = email, password = password)
                                val response = authService.login(loginDto)

                                Log.d("UserData", "Raw Response JSON: ${Gson().toJson(response.body())}")

                                if (response.isSuccessful) {
                                    val responseBody = response.body()
                                    if (responseBody != null) {
                                        withContext(Dispatchers.Main) {
                                            Log.d("UserData", "Raw Response JSON: ${Gson().toJson(response.body())}")

                                            Log.d("UserData", "User ID: ${responseBody.data.user._id}")
                                            Log.d("UserData", "User Name: ${responseBody.data.user.name}")
                                            Log.d("UserData", "User Best Scores: ${Gson().toJson(responseBody.data.user.bestScores)}")

                                            sharedPref.saveUserSharedPref(
                                                userId = responseBody.data.user._id,
                                                isLogged = true,
                                                name = responseBody.data.user.name,
                                                bestScores = responseBody.data.user.bestScores
                                            )
                                            navController.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("LoginScreenAPI", "Error: ${response.code()} - ${response.message()}")
                                }
                            } catch (e: Exception) {
                                Log.e("LoginScreenAPI", "Exception: ${e.localizedMessage}")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C), // Rojo sangre
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clickable(onClick = { navController.navigate("register") })
                    .indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple()
                    )
            ) {
                Text(
                    text = "House Stark awaits... Sign up to begin your quest.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFBDBDBD) // Gris claro
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    GameOfThronesAppTheme {
        LoginScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current)
        )
    }
}