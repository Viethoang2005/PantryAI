package com.example.pantryai.navigation

object Screen {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val RECIPES = "recipes"
    const val ASSISTANT = "assistant"
    const val FAVORITES = "favorites"
    const val RECIPE_DETAIL = "recipe_detail/{recipeId}"
    const val CHAT_HISTORY = "chat_history"

    fun recipeDetail(recipeId: Int) : String {
        return "recipe_detail/$recipeId"
    }

}