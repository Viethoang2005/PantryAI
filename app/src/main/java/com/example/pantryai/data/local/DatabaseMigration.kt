package com.example.pantryai.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `chat_messages` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `content` TEXT NOT NULL,
                `isFromUser` INTEGER NOT NULL,
                `createdAt` INTEGER NOT NULL,
                `suggestedRecipeIds` TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}