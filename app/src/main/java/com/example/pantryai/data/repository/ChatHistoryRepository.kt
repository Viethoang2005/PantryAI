package com.example.pantryai.data.repository

import androidx.room.withTransaction
import com.example.pantryai.data.local.dao.ChatDAO
import com.example.pantryai.data.local.entity.ChatMessageEntity
import com.example.pantryai.data.local.PantryDatabase
import com.example.pantryai.data.model.ChatMessage
import com.example.pantryai.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatHistoryRepository @Inject constructor(
    private val chatDao: ChatDAO,
    private val recipeRepository: RecipeRepository,
    private val database: PantryDatabase,
    private val conversationRepository: ChatConversationRepository
) {

    fun getMessages(conversationId: Long) : Flow<List<ChatMessage>> {
        return chatDao.getMessagesForConversation(conversationId)
            .map { entities ->
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

    suspend fun ensureWelcomeMessage(conversationId: Long) {
        if (chatDao.getMessageCountForConversation(conversationId) == 0) {
            saveMessage(
                conversationId = conversationId,
                content = "Xin chào! 👋 Bạn đang có những nguyên liệu nào? " +
                        "Mình sẽ gợi ý món ngon cho bạn.",
                isFromUser = false
            )
        }
    }

    suspend fun saveMessage(conversationId: Long,content: String, isFromUser: Boolean, suggestedRecipes: List<Recipe> = emptyList()) : Long {
        val now = System.currentTimeMillis()

        return database.withTransaction {
            val entity = ChatMessageEntity(
                content = content,
                isFromUser = isFromUser,
                suggestedRecipeIds = suggestedRecipes.joinToString (","){
                    it.id.toString()
                },
                conversationId = conversationId
            )

            val messageId = chatDao.insertMessage(entity)


            conversationRepository.updateLastMessageTime(
                conversationId = conversationId,
                updatedAt = now
            )

            messageId
        }
    }

    suspend fun clearHistory(conversationId: Long) {
        database.withTransaction {
            chatDao.deleteMessagesForConversation(conversationId = conversationId)
        }
        ensureWelcomeMessage(conversationId)
    }
}