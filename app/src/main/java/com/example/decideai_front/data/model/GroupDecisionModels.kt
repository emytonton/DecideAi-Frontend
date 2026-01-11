package com.example.decideai_front.data.model


data class GroupDecisionRequest(
    val title: String,
    val options: List<String>,
    val invitedFriends: List<String>
)

data class GroupDecisionResponse(
    val id: String,
    val title: String,
    val options: List<GroupOption>,
    val status: String,
    val winner: String? = null
)

data class GroupOption(
    val name: String,
    val votes: Int = 0
)

data class VoteRequest(
    val decisionId: String,
    val optionName: String
)