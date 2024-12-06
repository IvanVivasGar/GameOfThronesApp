package edu.lasallebajio.gameofthronesapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.lasallebajio.gameofthronesapp.domain.entities.Question
import edu.lasallebajio.gameofthronesapp.domain.use_cases.SharedPref
import edu.lasallebajio.gameofthronesapp.presentation.components.QuestionBox
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController,
    sharedPref: SharedPref,
    questions: List<Question>,
    category: String = navController.currentBackStackEntry?.arguments?.getString("category") ?: ""
) {
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFB71C1C))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading Questions...",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { navController.navigateUp() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB71C1C)
                    )
                ) {
                    Text("Go Back")
                }
            }
        }
    } else {
        var currentQuestionIndex by remember { mutableStateOf(0) }
        var score by remember { mutableStateOf(0) }
        var isNavigating by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

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
            if (currentQuestionIndex < questions.size) {
                QuestionBox(
                    question = questions[currentQuestionIndex],
                    onOptionSelected = { selectedOption ->
                        if (!isNavigating) {
                            isNavigating = true
                            val isCorrect = selectedOption == questions[currentQuestionIndex].correctOption
                            if (isCorrect) {
                                score++
                            }

                            coroutineScope.launch {
                                delay(500)
                                currentQuestionIndex++
                                isNavigating = false

                                if (currentQuestionIndex == questions.size) {
                                    delay(200)
                                    Log.i("QuizScreen", "Final Score: $score of ${questions.size} in category: $category")
                                    navController.navigate("results/$score/${questions.size}/$category")
                                }
                            }
                        }
                    }
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
fun QuizScreenPreview() {
    GameOfThronesAppTheme {
        QuizScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current),
            questions = emptyList(),
            category = ""
        )
    }
}