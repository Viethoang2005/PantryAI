package com.example.pantryai.data.repository

import com.example.pantryai.data.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantRepository @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    fun generateReply(input: String) : Pair<String, List<Recipe>> {
        val stopWords = setOf(
            "toi", "tôi", "co", "có", "va", "và",
            "voi", "với", "muon", "muốn", "nau", "nấu",
            "mon", "món", "gi", "gì", "lam", "làm",
            "cho", "minh", "mình", "cac", "các",
            "nhung", "những", "nguyen", "liệu"
        )

        val keywords = Regex("[\\p{L}\\p{N}]+")
            .findAll(input.lowercase())
            .map { it.value }
            .filter { it.length >= 3 && it !in stopWords }
            .toSet()


        if (keywords.isEmpty()) {
            return "Bạn hãy nhập tên nguyên liệu cụ thể nhé! 🥗" to emptyList()
        }

        val matchedRecipes = recipeRepository.getRecipes()
            .map { recipe ->
                val searchableText = (
                        listOf(recipe.name) + recipe.ingredients
                        ).joinToString(" ").lowercase()

                val score = keywords.count { keyword ->
                    keyword in searchableText
                }

                recipe to score
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .take(3)

        if (matchedRecipes.isEmpty()) {
            return Pair("Mình chưa tìm thấy công thức phù hợp trong danh sách hiện tại. Bạn thử nhập nguyên liệu khác nhé! 🥗",
                    emptyList()
            )
        }

        val recipes = matchedRecipes.map { it.first }

        val reply =  "Mình tìm được ${matchedRecipes.size} công thức có thể phù hợp với nguyên liệu của bạn. Nhấn vào món ăn để xem nguyên liệu và cách làm nhé! 🍳"

        return Pair(reply, recipes)
    }
}