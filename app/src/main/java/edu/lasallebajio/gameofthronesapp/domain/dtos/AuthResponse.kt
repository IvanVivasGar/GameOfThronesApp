package edu.lasallebajio.gameofthronesapp.domain.dtos

import edu.lasallebajio.gameofthronesapp.domain.entities.UserData

data class AuthResponse(
    val status: String,
    val token: String,
    val data: UserData
)