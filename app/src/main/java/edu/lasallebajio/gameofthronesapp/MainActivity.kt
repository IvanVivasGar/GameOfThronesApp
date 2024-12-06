package edu.lasallebajio.gameofthronesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.lasallebajio.gameofthronesapp.domain.entities.Question
import edu.lasallebajio.gameofthronesapp.domain.use_cases.SharedPref
import edu.lasallebajio.gameofthronesapp.presentation.ui.screens.HomeScreen
import edu.lasallebajio.gameofthronesapp.presentation.ui.screens.LoginScreen
import edu.lasallebajio.gameofthronesapp.presentation.ui.screens.QuizScreen
import edu.lasallebajio.gameofthronesapp.presentation.ui.screens.RegisterScreen
import edu.lasallebajio.gameofthronesapp.presentation.ui.screens.ResultsScreen
import edu.lasallebajio.gameofthronesapp.presentation.ui.theme.GameOfThronesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameOfThronesAppTheme {
                val navController = rememberNavController()
                val sharedPref = SharedPref(LocalContext.current)
                val isLogged = sharedPref.getIsLoggedSharedPref()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination =  if (isLogged) "home" else "login"
                    ){
                        composable(route = "login") {
                            LoginScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                        composable(route = "register") {
                            RegisterScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                        composable(route = "home") {
                            HomeScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                        composable(
                            route = "quiz/{category}?questions={questions}",
                            arguments = listOf(
                                navArgument("category") { type = NavType.StringType },
                                navArgument("questions") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: ""
                            val questionsJson = backStackEntry.arguments?.getString("questions")
                            val gson = Gson()

                            val questions: List<Question> = if (!questionsJson.isNullOrEmpty()) {
                                gson.fromJson(questionsJson, object : TypeToken<List<Question>>() {}.type)
                            } else {
                                emptyList()
                            }

                            QuizScreen(
                                navController = navController,
                                sharedPref = sharedPref,
                                questions = questions,
                                category = category
                            )
                        }
                        composable(
                            route = "results/{score}/{total}/{category}",
                            arguments = listOf(
                                navArgument("score") { type = NavType.IntType },
                                navArgument("total") { type = NavType.IntType },
                                navArgument("category") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val score = backStackEntry.arguments?.getInt("score") ?: 0
                            val total = backStackEntry.arguments?.getInt("total") ?: 0
                            val category = backStackEntry.arguments?.getString("category") ?: ""
                            ResultsScreen(score = score, total = total, category = category, navController = navController, sharedPref = sharedPref)
                        }
                    }
                }
            }
        }
    }
}