package com.example.pantryai.data.repository

import androidx.room.withTransaction
import com.example.pantryai.data.local.dao.ChatConversationDAO
import com.example.pantryai.data.local.entity.ChatConversationEntity
import com.example.pantryai.data.local.dao.ChatDAO
import com.example.pantryai.data.local.PantryDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatConversationRepository @Inject constructor(
    private val conversationDAO: ChatConversationDAO,
    private val chatDAO: ChatDAO,
    private val database: PantryDatabase
) {

    fun getAllConversations() : Flow<List<ChatConversationEntity>> {
        return conversationDAO.getAllConversations()
    }

    suspend fun createConversation(title: String = "Cuộc trò chuyện mới") : Long {
        val now = System.currentTimeMillis()

        return conversationDAO.insertConversation(
            ChatConversationEntity(
                title = title,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun renameConversation(conversationId: Long, newTitle: String) {
        val conversation = conversationDAO.getConversationById(conversationId) ?: return

        val title = newTitle.trim()
        if(title.isBlank()) return

        conversationDAO.updateConversation(
            conversation.copy(
                title = title,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteConversation(conversationId: Long) {
        database.withTransaction {
            chatDAO.deleteMessagesForConversation(conversationId)

            val conversation = conversationDAO.getConversationById(conversationId)
            if(conversation != null) conversationDAO.deleteConversation(conversation)
        }
    }

    suspend fun updateLastMessageTime(conversationId: Long, updatedAt: Long) {
        conversationDAO.updateLastMessageTime(conversationId, updatedAt)
    }
}