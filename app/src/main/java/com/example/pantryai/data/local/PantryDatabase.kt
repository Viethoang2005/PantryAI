package com.example.pantryai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class PantryDatabase: RoomDatabase() {
    abstract fun favoriteDAO() : FavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: PantryDatabase? = null

        fun getDatabase(context: Context) : PantryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PantryDatabase::class.java,
                    "pantryAi_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}