package com.example.pantryai.di

import android.content.Context
import androidx.room.Room
import com.example.pantryai.data.local.ChatDAO
import com.example.pantryai.data.local.FavoriteDAO
import com.example.pantryai.data.local.MIGRATION_1_2
import com.example.pantryai.data.local.PantryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePantryDatabase(
        @ApplicationContext context: Context
    ) : PantryDatabase {
        return Room.databaseBuilder(
            context,
            PantryDatabase::class.java,
            "pantryAi_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideFavoriteDao(
        database: PantryDatabase
    ) : FavoriteDAO {
        return database.favoriteDAO()
    }

    @Provides
    fun provideChatDao(
        database: PantryDatabase
    ) : ChatDAO{
        return database.chatDAO()
    }
}