package edu.lasallebajio.gameofthronesapp.domain.dtos

import edu.lasallebajio.gameofthronesapp.domain.entities.QuizData

data class QuizResponse(
    val status: String,
    val results: Int,
    val data: QuizData
)