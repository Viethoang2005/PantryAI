package com.example.pantryai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pantryai.data.local.dao.ChatConversationDAO
import com.example.pantryai.data.local.dao.ChatDAO
import com.example.pantryai.data.local.dao.FavoriteDAO
import com.example.pantryai.data.local.entity.ChatConversationEntity
import com.example.pantryai.data.local.entity.ChatMessageEntity
import com.example.pantryai.data.local.entity.FavoriteEntity
import kotlin.concurrent.Volatile

@Database(entities = [
    FavoriteEntity::class,
    ChatMessageEntity::class,
    ChatConversationEntity::class], version = 3, exportSchema = false)
abstract class PantryDatabase: RoomDatabase() {
    abstract fun favoriteDAO() : FavoriteDAO
    abstract fun chatDAO() : ChatDAO
    abstract fun chatConversationDAO() : ChatConversationDAO

    companion object {
        @Volatile
        private var INSTANCE: PantryDatabase? = null


        fun getDatabase(context: Context) : PantryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PantryDatabase::class.java,
                    "pantryAi_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { INSTANCE = it }
            }
        }
    }
}