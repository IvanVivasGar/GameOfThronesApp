package edu.lasallebajio.gameofthronesapp.datasources.services

import edu.lasallebajio.gameofthronesapp.domain.dtos.QuizResponse
import edu.lasallebajio.gameofthronesapp.domain.dtos.QuizScoreDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizService {
    @GET("questions")
    suspend fun getQuestionsByCategory(@Query("category") category: String): Response<QuizResponse>

    @POST("users/addScore/{userId}")
    suspend fun submitScore(@Path("userId") userId: String, @Body scoreData: QuizScoreDto): Response<Unit>
}