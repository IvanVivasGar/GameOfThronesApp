package edu.lasallebajio.gameofthronesapp.domain.entities

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val role: String,
    val bestScores: List<BestScore>
)