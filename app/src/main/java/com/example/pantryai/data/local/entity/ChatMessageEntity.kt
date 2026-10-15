package com.example.pantryai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val isFromUser: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val suggestedRecipeIds: String = "",
    @ColumnInfo(defaultValue = "1")
    val conversationId: Long = 1L
)
