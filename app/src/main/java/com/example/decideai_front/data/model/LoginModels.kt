package com.example.decideai_front.data.model


data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val accessToken: String,
    val user: UserInfo
)

data class UserInfo(
    val username: String,
    val email: String
)


data class ErrorResponse(
    val message: String
)