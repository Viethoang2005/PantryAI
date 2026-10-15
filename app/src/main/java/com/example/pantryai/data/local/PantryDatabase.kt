package com.example.pantryai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.concurrent.Volatile

@Database(entities = [FavoriteEntity::class, ChatMessageEntity::class], version = 2, exportSchema = false)
abstract class PantryDatabase: RoomDatabase() {
    abstract fun favoriteDAO() : FavoriteDAO
    abstract fun chatDAO() : ChatDAO

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
                    .addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
        }
    }
}