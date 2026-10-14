package com.example.pantryai.di

import android.content.Context
import androidx.room.Room
import com.example.pantryai.data.local.FavoriteDAO
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
        ).build()
    }

    @Provides
    fun provideFavoriteDao(
        database: PantryDatabase
    ) : FavoriteDAO {
        return database.favoriteDAO()
    }
}