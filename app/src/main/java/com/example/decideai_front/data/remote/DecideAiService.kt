package com.example.decideai_front.data.remote


import com.example.decideai_front.data.model.LoginRequest
import com.example.decideai_front.data.model.LoginResponse
import com.example.decideai_front.data.model.RegisterRequest
import com.example.decideai_front.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface DecideAiService {
    @POST("api/v1/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    @POST(value = "api/v1/users/signup")
    suspend fun signup(
        @Body request : RegisterRequest
    ): Response<RegisterResponse>
}

