package edu.lasallebajio.gameofthronesapp.domain.use_cases

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.lasallebajio.gameofthronesapp.domain.entities.BestScore

class SharedPref(
    context: Context
) {
    private val sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Save user with best scores
    fun saveUserSharedPref(
        userId: String,
        isLogged: Boolean,
        name: String,
        bestScores: List<BestScore>
    ) {
        val editor = sharedPref.edit()
        editor.putString("userId", userId)
        editor.putBoolean("isLogged", isLogged)
        editor.putString("name", name)

        // Explicitly convert best scores to JSON and save
        val bestScoresJson = gson.toJson(bestScores)
        Log.d("SharedPref", "Saving bestScoresJson: $bestScoresJson")
        editor.putString("bestScores", bestScoresJson)

        editor.apply()
    }

    // Get best scores for the logged-in user
    fun getBestScoresSharedPref(): List<BestScore> {
        val bestScoresJson = sharedPref.getString("bestScores", null)
        Log.d("SharedPref", "Stored bestScoresJson: $bestScoresJson")
        return if (bestScoresJson != null) {
            try {
                val type = object : TypeToken<List<BestScore>>() {}.type
                val parsedScores = gson.fromJson<List<BestScore>>(bestScoresJson, type)
                Log.d("SharedPref", "Parsed bestScores: $parsedScores")
                parsedScores
            } catch (e: Exception) {
                Log.e("SharedPref", "Error parsing bestScores: ${e.localizedMessage}")
                emptyList()
            }
        } else {
            Log.d("SharedPref", "No bestScores found")
            emptyList()
        }
    }

    // Get best score for a specific category
    fun getBestScoreForCategory(category: String): Int {
        val bestScores = getBestScoresSharedPref()
        Log.d("SharedPref", "Checking best score for category: $category")
        Log.d("SharedPref", "Available best scores: $bestScores")

        return bestScores
            .find { it.category.lowercase() == category.lowercase() }
            ?.score
            ?: run {
                Log.d("SharedPref", "No score found for category $category")
                0
            }
    }

    // Obtener ID del usuario
    fun getUserIdSharedPref(): String? {
        return sharedPref.getString("userId", "0")
    }

    // Obtener estado de sesión
    fun getIsLoggedSharedPref(): Boolean {
        return sharedPref.getBoolean("isLogged", false)
    }

    // Obtener nombre del usuario
    fun getNameSharedPref(): String? {
        return sharedPref.getString("name", "Usuario desconocido") // Valor predeterminado
    }

    // Eliminar usuario
    fun removeUserSharedPref() {
        val editor = sharedPref.edit()
        editor.clear() // Borra todos los datos
        editor.apply()
    }
}