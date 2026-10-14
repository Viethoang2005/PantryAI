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
        if(query.isBlank()) return getRecipes()

        return FakeRecipes.recipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true) ||
                    recipe.ingredients.any { ingredient ->
                        ingredient.contains(query, ignoreCase = true)
                    }
        }
    }
}