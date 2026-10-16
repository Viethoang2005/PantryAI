
package com.example.pantryai.data.repository

import com.example.pantryai.data.model.Recipe
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

data class AssistantReply(
    val message: String,
    val recipes: List<Recipe>,
    val isAiSuccess: Boolean
)

@Singleton
class AssistantRepository @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val geminiRepository: GeminiRepository
) {

    suspend fun generateReply(input: String): AssistantReply {

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

        val allRecipes = recipeRepository.getRecipes()

        val matchedRecipes = if (keywords.isEmpty()) {
            emptyList()
        } else {
            allRecipes
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
                .map { it.first }
        }

        val recipeContext = if (allRecipes.isEmpty()) {
            "Hiện chưa có công thức nào trong PantryAI."
        } else {
            allRecipes.joinToString("\n\n") { recipe ->
                """
                Tên món: ${recipe.name}
                Mô tả: ${recipe.description}
                Nguyên liệu: ${recipe.ingredients.joinToString(", ")}
                Thời gian nấu: ${recipe.cookingTimeMinutes} phút
                Độ khó: ${recipe.difficulty}
                Cách làm:
                ${recipe.instructions.mapIndexed { index, step ->
                    "${index + 1}. $step"
                }.joinToString("\n")}
                """.trimIndent()
            }
        }

        val prompt = """
            Bạn là PantryAI, trợ lý nấu ăn bằng tiếng Việt.

            Hãy trả lời tự nhiên, thân thiện và dễ hiểu.
            Hỗ trợ tìm món ăn từ nguyên liệu người dùng có,
            hướng dẫn nấu ăn và gợi ý thay thế nguyên liệu.
            Ưu tiên thông tin từ danh sách công thức bên dưới.
            Không khẳng định món ăn có trong danh sách nếu
            dữ liệu không có món đó.
            Không tự khẳng định dinh dưỡng chính xác khi
            chưa có đủ dữ liệu.

            Danh sách công thức PantryAI:
            $recipeContext

            Câu hỏi của người dùng:
            $input
        """.trimIndent()

        return try {
            val reply = geminiRepository.ask(prompt)

            AssistantReply(
                message = reply,
                recipes = matchedRecipes,
                isAiSuccess = true
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AssistantReply(
                message = if (matchedRecipes.isNotEmpty()) {
                    "Hiện mình chưa kết nối được với AI. " +
                            "Bạn vẫn có thể tham khảo các công thức " +
                            "gợi ý bên dưới nhé!"
                } else {
                    "Hiện mình chưa kết nối được với AI. " +
                            "Bạn hãy kiểm tra mạng và thử lại nhé!"
                },
                recipes = matchedRecipes,
                isAiSuccess = false
            )
        }
    }
}
