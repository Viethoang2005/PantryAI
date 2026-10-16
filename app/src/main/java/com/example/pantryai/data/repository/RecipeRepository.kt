
package com.example.pantryai.data.repository

import com.example.pantryai.data.model.Recipe
import java.text.Normalizer
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor() {

    fun getRecipes(): List<Recipe> {
        return VietnameseRecipes.recipes
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        return getRecipes().find { it.id == recipeId }
    }

    fun searchRecipes(query: String): List<Recipe> {
        return filterRecipes(query = query)
    }

    private fun normalizeVietnamese(text: String): String {
        return Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
            .replace('đ', 'd')
    }

    fun filterRecipes(
        query: String = "",
        category: String? = null,
        difficulty: String? = null,
        maxCookingTimeMinutes: Int? = null
    ): List<Recipe> {

        val normalizedQuery = normalizeVietnamese(query.trim())

        return getRecipes().filter { recipe ->
            val normalizedName = normalizeVietnamese(recipe.name)

            val matchesNameOrIngredient =
                normalizedQuery.isBlank() ||
                        normalizedName.contains(normalizedQuery) ||
                        recipe.ingredients.any { ingredient ->
                            normalizeVietnamese(ingredient).contains(normalizedQuery)
                        }

            val matchesCategory = category == null ||
                    normalizeVietnamese(recipe.category) ==
                    normalizeVietnamese(category)

            val matchesDifficulty = difficulty == null ||
                    normalizeVietnamese(recipe.difficulty) ==
                    normalizeVietnamese(difficulty)

            val matchesCookingTime = maxCookingTimeMinutes == null ||
                    recipe.cookingTimeMinutes <= maxCookingTimeMinutes

            matchesNameOrIngredient &&
                    matchesCategory &&
                    matchesDifficulty &&
                    matchesCookingTime
        }
    }
}
