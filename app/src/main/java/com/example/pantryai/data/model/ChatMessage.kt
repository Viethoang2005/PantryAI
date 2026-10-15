package com.example.pantryai.data.model

data class ChatMessage(
    val id: Long,
    val content: String,
    val isFromUser: Boolean,
    val suggestedRecipes: List<Recipe> = emptyList()
)
