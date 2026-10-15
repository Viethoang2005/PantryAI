package com.example.pantryai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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
}