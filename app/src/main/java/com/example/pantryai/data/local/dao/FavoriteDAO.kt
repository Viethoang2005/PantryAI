package com.example.pantryai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pantryai.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {

    @Query("select * from favorites order by recipeId DESC")
    fun getAllFavorites() : Flow<List<FavoriteEntity>>

    @Query("select exists(select 1 from favorites where recipeId = :recipeId)")
    fun isFavorite(recipeId: Int) : Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("delete from favorites where recipeId = :recipeId")
    suspend fun removeFavorite(recipeId: Int)
}