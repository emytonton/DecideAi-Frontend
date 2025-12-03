package com.example.decideai_front.data.remote


import com.example.decideai_front.data.model.LoginRequest
import com.example.decideai_front.data.model.LoginResponse
import com.example.decideai_front.data.model.OptionsDecisionRequest
import com.example.decideai_front.data.model.OptionsDecisionResponse
import com.example.decideai_front.data.model.RegisterRequest
import com.example.decideai_front.data.model.RegisterResponse
import com.example.decideai_front.data.model.SoloDecisionRequest
import com.example.decideai_front.data.model.SoloDecisionResponse
import com.example.decideai_front.data.model.UpdateProfileRequest
import com.example.decideai_front.data.model.UserResponse
import com.example.decideai_front.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.*

interface DecideAiService {
    @POST("api/v1/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    @POST(value = "api/v1/users/signup")
    suspend fun signup(
        @Body request : RegisterRequest
    ): Response<RegisterResponse>
    @POST("api/v1/decision/solo")
    suspend fun makeSoloDecision(
        @Header("Authorization") token: String,
        @Body request: SoloDecisionRequest
    ): Response<SoloDecisionResponse>
    @POST("api/v1/custom/decide")
    suspend fun makeOptionsDecision(
        @Header("Authorization") token: String,
        @Body request: OptionsDecisionRequest
    ): Response<OptionsDecisionResponse>
    @GET("api/v1/users/me")
    suspend fun getMyProfile(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PUT("api/v1/users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>

    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(
        @Header("Authorization") token: String
    ): Response<Unit>

    // 1. Listar Amigos e Pendências
    @GET("api/v1/social/friends")
    suspend fun getFriends(
        @Header("Authorization") token: String
    ): Response<FriendsListResponse>

    // 2. Enviar Solicitação
    @POST("api/v1/social/friends/request")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Body request: SendFriendRequest
    ): Response<Unit>

    // 3. Responder Solicitação (Aceitar/Recusar)
    @POST("api/v1/social/friends/response")
    suspend fun answerFriendRequest(
        @Header("Authorization") token: String,
        @Body request: AnswerFriendRequest
    ): Response<Unit>

    // 4. Remover Amigo (Usa @HTTP para permitir Body no DELETE)
    @HTTP(method = "DELETE", path = "api/v1/social/friends", hasBody = true)
    suspend fun removeFriend(
        @Header("Authorization") token: String,
        @Body request: RemoveFriendRequest
    ): Response<Unit>

    // 5. Buscar Usuários
    @GET("api/v1/users/search")
    suspend fun searchUsers(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Response<List<UserResponse>>
}

