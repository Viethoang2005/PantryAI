package com.example.pantryai.features.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pantryai.data.local.FavoriteEntity
import com.example.pantryai.data.local.PantryDatabase
import com.example.pantryai.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository : FavoriteRepository
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteEntity>> =
        repository.getAllFavorites().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun isFavorite(recipeId: Int) = repository.isFavorite(recipeId)

    fun toggleFavorite(recipeId: Int, currentFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId = recipeId, currentFavorite = currentFavorite)
        }
    }
}