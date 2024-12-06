package edu.lasallebajio.gameofthronesapp.datasources.services

import edu.lasallebajio.gameofthronesapp.domain.dtos.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoryService {
    @GET("categories")
    suspend fun getCategories(): Response<CategoryResponse>
}