package edu.lasallebajio.gameofthronesapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import edu.lasallebajio.gameofthronesapp.presentation.ui.utils.CircleUserRound
import edu.lasallebajio.gameofthronesapp.presentation.ui.utils.LockKeyhole

@Composable
fun LoginScreen(innerPadding : PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Login Screen")
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Usuario") },
            leadingIcon = { Icon(imageVector = CircleUserRound, contentDescription = "Username") },
            modifier = Modifier.background(Color.White)
                .fillMaxWidth()
            )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Contraseña") },
            leadingIcon = { Icon(imageVector = LockKeyhole, contentDescription = "Username") },
            modifier = Modifier.background(Color.White)
                .fillMaxWidth())
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesion")
        }

        Text(
            text = "Crea una cuenta",
            modifier = Modifier.clickable {  }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    GameOfThronesAppTheme {
        LoginScreen(innerPadding = PaddingValues(0.dp))
    }
}