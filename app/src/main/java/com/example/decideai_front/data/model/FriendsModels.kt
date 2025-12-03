package com.example.decideai_front.data.model

data class FriendsListResponse(
    val friends: List<UserResponse>,
    val pendingReceived: List<FriendRequest>
)


data class FriendRequest(
    val requestId: String,
    val createdAt: String,
    val sender: UserResponse
)


data class SendFriendRequest(
    val receiverId: String
)


data class AnswerFriendRequest(
    val requestId: String,
    val action: String
)


data class RemoveFriendRequest(
    val friendId: String
)