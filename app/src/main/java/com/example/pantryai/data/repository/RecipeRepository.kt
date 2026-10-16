package com.example.pantryai.data.repository

import com.example.pantryai.data.mock.FakeRecipes
import com.example.pantryai.data.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
) {
    fun getRecipes() : List<Recipe> {
        return FakeRecipes.recipes
    }

    fun getRecipeById(recipeId: Int) : Recipe? {
        return FakeRecipes.recipes.find { it.id == recipeId }
    }

    fun searchRecipes(query: String) : List<Recipe> {
        return filterRecipes(query = query)
    }

    fun filterRecipes(
        query: String = "",
        category: String? = null,
        difficulty: String? = null,
        maxCookingTimeMinutes: Int? = null
    ) : List<Recipe> {
        return FakeRecipes.recipes.filter { recipe ->
            val matchesQuery = query.isBlank() ||
                    recipe.name.contains(query.trim(), ignoreCase = true) ||
                    recipe.ingredients.any { ingredient ->
                        ingredient.contains(query.trim(), ignoreCase = true)
                    }

            val matchesCategory = category == null ||
                    recipe.category.equals(category, ignoreCase = true)

            val matchesDifficulty = difficulty == null ||
                    recipe.difficulty.equals(difficulty, ignoreCase = true)

            val matchesCookingTime = maxCookingTimeMinutes == null ||
                    recipe.cookingTimeMinutes <= maxCookingTimeMinutes

            matchesQuery && matchesCategory && matchesDifficulty && matchesCookingTime
        }
    }
}