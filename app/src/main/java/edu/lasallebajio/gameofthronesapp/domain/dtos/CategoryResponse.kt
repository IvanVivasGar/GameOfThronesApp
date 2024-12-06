package edu.lasallebajio.gameofthronesapp.domain.dtos

import edu.lasallebajio.gameofthronesapp.domain.entities.CategoryData

data class CategoryResponse(
    val status: String,
    val result: Int,
    val data: CategoryData
)