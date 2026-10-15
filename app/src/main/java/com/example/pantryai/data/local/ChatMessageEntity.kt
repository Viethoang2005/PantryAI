package com.example.pantryai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val isFromUser: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val suggestedRecipeIds: String = ""
)
