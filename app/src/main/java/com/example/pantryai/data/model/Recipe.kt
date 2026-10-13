package com.example.pantryai.data.model

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val cookingTimeMinutes: Int,
    val difficulty: String,
    val category: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val emoji: String
)
