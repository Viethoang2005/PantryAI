package com.example.pantryai.data.repository

import com.example.pantryai.data.local.FavoriteDAO
import com.example.pantryai.data.local.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDAO: FavoriteDAO
) {
    fun getAllFavorites() : Flow<List<FavoriteEntity>> {
        return favoriteDAO.getAllFavorites()
    }

    fun isFavorite(recipeId: Int) : Flow<Boolean> {
        return favoriteDAO.isFavorite(recipeId)
    }

    suspend fun addFavorite(recipeId: Int) {
        favoriteDAO.addFavorite(
            FavoriteEntity(recipeId = recipeId)
        )
    }

    suspend fun removeFavorite(recipeId: Int) {
        favoriteDAO.removeFavorite(recipeId = recipeId)
    }

    suspend fun toggleFavorite(
        recipeId: Int,
        currentFavorite: Boolean
    ) {
        if(currentFavorite) removeFavorite(recipeId)
        else addFavorite(recipeId)
    }
}