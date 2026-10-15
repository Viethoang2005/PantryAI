package com.example.pantryai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pantryai.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDAO {
    @Query("select * from chat_messages order by createdAt asc, id asc")
    fun getAllMessages() : Flow<List<ChatMessageEntity>>

    @Insert
    suspend fun insertMessage(message: ChatMessageEntity) : Long

    @Query("delete from chat_messages")
    suspend fun deleteAllMessages()

    @Query("select count(*) from chat_messages")
    suspend fun getMessageCount() : Int

    @Query("select * from chat_messages where conversationId = :conversationId order by createdAt asc, id asc")
    fun getMessagesForConversation(conversationId: Long) : Flow<List<ChatMessageEntity>>

    @Query("delete from chat_messages where conversationId = :conversationId")
    suspend fun deleteMessagesForConversation(
        conversationId: Long
    )

    @Query("SELECT COUNT(*) FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun getMessageCountForConversation(
        conversationId: Long
    ): Int
}