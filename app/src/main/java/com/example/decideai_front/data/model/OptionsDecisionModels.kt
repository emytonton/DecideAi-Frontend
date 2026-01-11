package com.example.decideai_front.data.model


data class OptionsDecisionRequest(
    val listId: String? = null,
    val tempOptions: List<String>? = null
)

data class OptionsDecisionResponse(
    val result: String
)



data class CustomListResponse(
    val id: String,
    val title: String,
    val options: List<String>,
    val createdAt: String
)


data class SaveListRequest(
    val title: String,
    val options: List<String>
)