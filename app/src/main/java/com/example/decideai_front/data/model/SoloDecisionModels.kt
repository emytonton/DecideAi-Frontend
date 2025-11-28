package com.example.decideai_front.data.model

data class SoloDecisionRequest(
    val category: String,
    val filter1: String? = null,
    val filter2: String? = null
)

data class SoloDecisionResponse(
    val id: String,
    val title: String,
    val category: String,
    val details: String,
    val imageUrl: String? = null
)