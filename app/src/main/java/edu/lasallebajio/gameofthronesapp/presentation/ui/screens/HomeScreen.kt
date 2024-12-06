package edu.lasallebajio.gameofthronesapp.presentation.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import edu.lasallebajio.gameofthronesapp.datasources.services.CategoryService
import edu.lasallebajio.gameofthronesapp.datasources.services.QuizService
import edu.lasallebajio.gameofthronesapp.domain.entities.Category
import edu.lasallebajio.gameofthronesapp.domain.entities.User
import edu.lasallebajio.gameofthronesapp.domain.use_cases.SharedPref
import edu.lasallebajio.gameofthronesapp.presentation.components.QuizBox
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref
) {
    val coroutineScope = rememberCoroutineScope()
    var categories: List<Category> by remember { mutableStateOf(emptyList()) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val categoryService = Retrofit.Builder()
                    .baseUrl("http://68.183.30.211/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CategoryService::class.java)

                val response = categoryService.getCategories()

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.data?.data?.let {
                        // Actualizamos la lista de categorías
                        categories = it
                        Log.d("HomeScreenAPI", "Categories: $categories")
                    }
                } else {
                    Log.e("HomeScreenAPI", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeScreenAPI", "Exception: ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(Color.Black, Color(0xFF1A1A1A))
            ))
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Welcome Message
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Welcome back,",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFBDBDBD)
                    )
                    Text(
                        text = sharedPref.getNameSharedPref().toString(),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFFE0E0E0)
                    )
                    Text(
                        text = "Test your knowledge of the Seven Kingdoms",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFBDBDBD),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                IconButton(
                    onClick = {
                        sharedPref.removeUserSharedPref()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Top)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }

            Log.d("HomeScreen", "Categories: $categories")
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories.size) { categoryIndex ->
                    val category = categories[categoryIndex]

                    // Get the best score for this specific category
                    val maxScore = sharedPref.getBestScoreForCategory(category.name)
                    Log.d("HomeScreen", "Category: ${category.name}, Best Score: $maxScore")

                    QuizBox(
                        category = category,
                        maxScore = maxScore, // Use the retrieved best score
                        onClick = {
                            val selectedCategory = categories[categoryIndex]

                            scope.launch(Dispatchers.IO) {
                                try {
                                    val quizService = Retrofit.Builder()
                                        .baseUrl("http://68.183.30.211/api/v1/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                        .create(QuizService::class.java)

                                    val response = quizService.getQuestionsByCategory(category = selectedCategory.name)

                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful && response.body()?.status == "success") {
                                            val questions = response.body()!!.data.questions

                                            // Serialize questions to JSON
                                            val gson = Gson()
                                            val questionsJson = gson.toJson(questions)

                                            // Navigate and pass questions as argument
                                            navController.navigate("quiz/${selectedCategory.name}?questions=$questionsJson") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                            }
                                        } else {
                                            Log.e("QuizScreen", "Error: ${response.errorBody()}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("QuizScreen", "Exception: ${e.localizedMessage}")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    GameOfThronesAppTheme {
        HomeScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current)
        )
    }
}