package com.example.decideai_front.data.model


data class GroupDecisionRequest(
    val title: String,
    val options: List<String>,
    val invitedUserIds: List<String>
)

data class GroupDecisionSummary(
    val id: String,
    val title: String,
    val status: String,
    val myStatus: String,
    val winner: String?,
    val hasViewedResult: Boolean,
    val createdAt: String
)

data class GroupVoteRequest(
    val decisionId: String,
    val voteOption: String? = null,
    val decline: Boolean? = null
)

data class GroupVoteResponse(
    val status: String,
    val winner: String?
)

data class MarkAsViewedRequest(
    val decisionId: String
)