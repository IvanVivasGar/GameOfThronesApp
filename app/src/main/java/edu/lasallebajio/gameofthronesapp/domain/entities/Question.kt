package edu.lasallebajio.gameofthronesapp.domain.entities

data class Question(
    val _id: String,
    val question: String,
    val options: List<String>,
    val correctOption: String,
    val category: String,
    val image: String
)