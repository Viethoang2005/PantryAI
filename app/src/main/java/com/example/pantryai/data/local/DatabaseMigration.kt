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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
                    CREATE TABLE IF NOT EXISTS chat_conversations (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
        )

        db.execSQL(
            """
                    INSERT INTO chat_conversations
                        (id, title, createdAt, updatedAt)
                    VALUES (
                        1,
                        'Cuộc trò chuyện trước đây',
                        CAST(strftime('%s', 'now') AS INTEGER) * 1000,
                        CAST(strftime('%s', 'now') AS INTEGER) * 1000
                    )
                    """.trimIndent()
        )


        db.execSQL(
            """
                    ALTER TABLE chat_messages
                    ADD COLUMN conversationId INTEGER NOT NULL DEFAULT 1
                    """.trimIndent()
        )
    }
}