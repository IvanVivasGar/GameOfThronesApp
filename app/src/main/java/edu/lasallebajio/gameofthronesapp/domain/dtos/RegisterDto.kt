package edu.lasallebajio.gameofthronesapp.domain.dtos

data class RegisterDto(
    val name: String,
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val role: String = "user"
)