package com.example.decideai_front.data.model

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val avatar: String?
)
data class UpdateProfileRequest(
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null
)