package com.example.decideai_front.data.model


data class GroupDecisionSummary(
    val id: String,
    val title: String,
    val status: String,
    val myStatus: String,
    val winner: String? = null,
    val createdAt: String,
    val hasVoted: Boolean = false,
    val myVote: String? = null,
    val hasViewedResult: Boolean? = false
)


data class GroupDecisionDetail(
    val id: String,
    val title: String,
    val options: List<String>,
    val status: String,
    val winner: String?,
    val participants: List<Participant>,
    val myStatus: String,
    val hasVoted: Boolean,
    val myVote: String?
)

data class Participant(
    val userId: String,
    val status: String,
    val hasVoted: Boolean = false,
    val vote: String? = null
)


data class CreateGroupRequest(
    val title: String,
    val options: List<String>,
    val invitedUserIds: List<String>
)

data class CreateGroupResponse(
    val id: String,
    val title: String,
    val status: String
)


data class VoteGroupRequest(
    val decisionId: String,
    val voteOption: String? = null,
    val decline: Boolean? = null
)

data class VoteGroupResponse(
    val status: String,
    val winner: String?,
    val myStatus: String,
    val hasVoted: Boolean,
    val myVote: String?
)


data class MarkViewedRequest(val decisionId: String)
data class NotificationTokenRequest(val token: String)