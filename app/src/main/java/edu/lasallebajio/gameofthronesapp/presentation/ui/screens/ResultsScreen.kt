package edu.lasallebajio.gameofthronesapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.lasallebajio.gameofthronesapp.datasources.services.QuizService
import edu.lasallebajio.gameofthronesapp.domain.dtos.QuizScoreDto
import edu.lasallebajio.gameofthronesapp.domain.entities.BestScore
import edu.lasallebajio.gameofthronesapp.domain.use_cases.SharedPref
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ResultsScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref,
    score: Int = 0,
    total: Int = 0,
    category: String = ""
) {
    val scope = rememberCoroutineScope()
    val backgroundBrush = if (score == total) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A1A1A),
                Color(0xFF420000)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.Black,
                Color(0xFF212121)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
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
                text = if (score == total)
                    "You Are Worthy of the Iron Throne!"
                else
                    "Your Watch Has Not Yet Ended...",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFE0E0E0),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = if (score == total)
                    "Perfect score! You know Westeros better than the Maesters!"
                else
                    "The realm awaits your further study. $score/$total shows promise.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFBDBDBD),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        try {
                            val userId = sharedPref.getUserIdSharedPref()
                            if (!userId.isNullOrEmpty()) {
                                val quizService = Retrofit.Builder()
                                    .baseUrl("http://68.183.30.211/api/v1/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(QuizService::class.java)

                                val scoreData = QuizScoreDto(
                                    category = category,
                                    score = score
                                )

                                // Enviar el puntaje
                                val response = quizService.submitScore(userId, scoreData)
                                Log.d("ResultsScreen", "Response: $response")

                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        // Si la respuesta es exitosa, recupera las puntuaciones anteriores del usuario
                                        val bestScores = sharedPref.getBestScoresSharedPref()

                                        // Actualiza la lista de puntuaciones con el nuevo puntaje
                                        val updatedScores = bestScores.toMutableList()

                                        // Si ya hay un puntaje para la categoría, actualízalo
                                        val existingScore = updatedScores.find { it.category.equals(category, ignoreCase = true) }
                                        if (existingScore != null) {
                                            // Actualiza el puntaje si el nuevo es más alto
                                            if (score > existingScore.score) {
                                                updatedScores.remove(existingScore)
                                                updatedScores.add(existingScore.copy(score = score))
                                            }
                                        } else {
                                            // Si no hay puntaje para esta categoría, agrégalo
                                            updatedScores.add(BestScore(_id = userId, score = score, category = category))
                                        }

                                        // Guarda el usuario con la nueva lista de puntuaciones
                                        sharedPref.saveUserSharedPref(
                                            userId = userId,
                                            isLogged = true,  // Puedes mantener el valor de isLogged si no cambia
                                            name = sharedPref.getNameSharedPref() ?: "Usuario desconocido",  // Usa el nombre guardado
                                            bestScores = updatedScores
                                        )

                                        // Navegar a la pantalla de inicio
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        Log.e("ResultsScreen", "Error: ${response.code()} - ${response.message()}")
                                    }
                                }
                            } else {
                                Log.e("ResultsScreen", "Usuario no encontrado")
                            }
                        } catch (e: Exception) {
                            Log.e("ResultsScreen", "Exception: ${e.localizedMessage}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB71C1C),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Return to Winterfell",
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Score: $score/$total",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFBDBDBD),
                modifier = Modifier.alpha(0.7f)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ResultsScreenPreview() {
    GameOfThronesAppTheme {
        ResultsScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current),
            score = 0,
            total = 0,
            category = ""
        )
    }
}