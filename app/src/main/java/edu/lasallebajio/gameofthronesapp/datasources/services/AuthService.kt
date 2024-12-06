package edu.lasallebajio.gameofthronesapp.datasources.services

import edu.lasallebajio.gameofthronesapp.domain.dtos.AuthDto
import edu.lasallebajio.gameofthronesapp.domain.dtos.AuthResponse
import edu.lasallebajio.gameofthronesapp.domain.dtos.RegisterDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body login: AuthDto) : Response<AuthResponse>

    @POST("signup")
    suspend fun registerUser(@Body register: RegisterDto): Response<AuthResponse>
}