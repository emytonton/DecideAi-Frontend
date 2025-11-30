package com.example.decideai_front.data.model

data class OptionsDecisionRequest(
    val tempOptions: List<String>
)

data class OptionsDecisionResponse(
    val result: String
)