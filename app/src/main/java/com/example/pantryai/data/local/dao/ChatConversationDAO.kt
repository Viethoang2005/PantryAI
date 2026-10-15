package com.example.pantryai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pantryai.data.local.entity.ChatConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatConversationDAO {

    @Query("select * from chat_conversations order by updatedAt desc, id desc")
    fun getAllConversations() : Flow<List<ChatConversationEntity>>

    @Query("select * from chat_conversations where id = :id")
    suspend fun getConversationById(id: Long) : ChatConversationEntity?

    @Insert
    suspend fun insertConversation(conversationEntity: ChatConversationEntity) : Long

    @Update
    suspend fun updateConversation(conversationEntity: ChatConversationEntity)


    @Delete
    suspend fun deleteConversation(conversationEntity: ChatConversationEntity)

    @Query("select count(*) from chat_messages where conversationId = :conversationId")
    suspend fun getMessageCountForConversation(conversationId: Long) : Int

    @Query(" UPDATE chat_conversations SET updatedAt = :updatedAt WHERE id = :conversationId")
    suspend fun updateLastMessageTime(
        conversationId: Long,
        updatedAt: Long
    )
}