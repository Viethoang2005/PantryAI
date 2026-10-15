package com.example.pantryai.data.repository

import com.example.pantryai.data.local.ChatDAO
import com.example.pantryai.data.local.ChatMessageEntity
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatHistoryRepository @Inject constructor(
    private val chatDao: ChatDAO,
    private val recipeRepository: RecipeRepository
) {

    fun getMessages() : Flow<List<ChatMessage>> {
        return chatDao.getAllMessages().map { entities ->
            val recipesById = recipeRepository.getRecipes()
                .associateBy { it.id }

            entities.map { entity ->
                val recipeIds = entity.suggestedRecipeIds
                    .split(",")
                    .mapNotNull { it.toIntOrNull() }

                val recipes = recipeIds.mapNotNull { recipesById[it] }

                ChatMessage(
                    id = entity.id,
                    content = entity.content,
                    isFromUser = entity.isFromUser,
                    suggestedRecipes = recipes
                )
            }


        }
    }

    suspend fun ensureWelcomeMessage() {
        if(chatDao.getMessageCount() == 0) {
            saveMessage(
                content = "Xin chào! 👋 Bạn đang có những nguyên liệu nào? " +
                        "Mình sẽ gợi ý món ngon cho bạn.",
                isFromUser = false
            )
        }
    }

    suspend fun saveMessage(content: String, isFromUser: Boolean, suggestedRecipes: List<Recipe> = emptyList()) : Long {
        val entity = ChatMessageEntity(
            content = content,
            isFromUser = isFromUser,
            suggestedRecipeIds = suggestedRecipes.joinToString (","){
                it.id.toString()
            }
        )

        return chatDao.insertMessage(entity)
    }
}