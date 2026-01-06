package com.example.decideai_front.data.remote

import com.example.decideai_front.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface DecideAiService {
    // --- AUTH ---
    @POST("api/v1/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST(value = "api/v1/users/signup")
    suspend fun signup(@Body request : RegisterRequest): Response<RegisterResponse>

    // --- DECISÃO SOLO ---
    @POST("api/v1/decision/solo")
    suspend fun makeSoloDecision(@Header("Authorization") token: String, @Body request: SoloDecisionRequest): Response<SoloDecisionResponse>

    // --- DECISÃO OPÇÕES ---
    @POST("api/v1/custom/decide")
    suspend fun makeOptionsDecision(@Header("Authorization") token: String, @Body request: OptionsDecisionRequest): Response<OptionsDecisionResponse>

    @POST("api/v1/custom/lists")
    suspend fun createList(@Header("Authorization") token: String, @Body request: SaveListRequest): Response<CustomListResponse>

    @GET("api/v1/custom/lists")
    suspend fun getUserLists(@Header("Authorization") token: String): Response<List<CustomListResponse>>

    @PUT("api/v1/custom/lists/{id}")
    suspend fun updateList(@Header("Authorization") token: String, @Path("id") id: String, @Body request: SaveListRequest): Response<Unit>

    @DELETE("api/v1/custom/lists/{id}")
    suspend fun deleteList(@Header("Authorization") token: String, @Path("id") id: String): Response<Unit>

    // --- PERFIL ---
    @GET("api/v1/users/me")
    suspend fun getMyProfile(@Header("Authorization") token: String): Response<UserResponse>

    @PUT("api/v1/users/me")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body request: UpdateProfileRequest): Response<UserResponse>

    @DELETE("api/v1/users/me")
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<Unit>

    // --- SOCIAL ---
    @GET("api/v1/social/friends")
    suspend fun getFriends(@Header("Authorization") token: String): Response<FriendsListResponse>

    @POST("api/v1/social/friends/request")
    suspend fun sendFriendRequest(@Header("Authorization") token: String, @Body request: SendFriendRequest): Response<Unit>

    @POST("api/v1/social/friends/response")
    suspend fun answerFriendRequest(@Header("Authorization") token: String, @Body request: AnswerFriendRequest): Response<Unit>

    @HTTP(method = "DELETE", path = "api/v1/social/friends", hasBody = true)
    suspend fun removeFriend(@Header("Authorization") token: String, @Body request: RemoveFriendRequest): Response<Unit>

    @GET("api/v1/users/search")
    suspend fun searchUsers(@Header("Authorization") token: String, @Query("q") query: String): Response<List<UserResponse>>

    // --- DECISÃO EM GRUPO (CORRIGIDO) ---

    @GET("api/v1/group")
    suspend fun getGroupDecisions(
        @Header("Authorization") token: String
    ): Response<List<GroupDecisionSummary>>

    @GET("api/v1/group/{id}")
    suspend fun getGroupDecisionDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GroupDecisionDetail>

    // AQUI MUDOU: Agora espera um CreateGroupResponse em vez de Unit
    @POST("api/v1/group")
    suspend fun createGroupDecision(
        @Header("Authorization") token: String,
        @Body request: CreateGroupRequest
    ): Response<CreateGroupResponse>

    @POST("api/v1/group/answer")
    suspend fun voteGroupDecision(
        @Header("Authorization") token: String,
        @Body request: VoteGroupRequest
    ): Response<VoteGroupResponse>

    @PATCH("api/v1/group/viewed")
    suspend fun markGroupResultAsViewed(
        @Header("Authorization") token: String,
        @Body request: MarkViewedRequest
    ): Response<Unit>

    @PATCH("api/v1/users/notification-token")
    suspend fun updateNotificationToken(
        @Header("Authorization") token: String,
        @Body request: NotificationTokenRequest
    ): Response<Unit>
}